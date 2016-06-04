/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zeppelin.socket;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.conf.ZeppelinConfiguration.ConfVars;
import org.apache.zeppelin.display.AngularObject;
import org.apache.zeppelin.display.AngularObjectRegistry;
import org.apache.zeppelin.display.AngularObjectRegistryListener;
import org.apache.zeppelin.interpreter.InterpreterGroup;
import org.apache.zeppelin.interpreter.InterpreterOutput;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.InterpreterSetting;
import org.apache.zeppelin.interpreter.remote.RemoteAngularObjectRegistry;
import org.apache.zeppelin.interpreter.remote.RemoteInterpreterProcessListener;
import org.apache.zeppelin.notebook.*;
import org.apache.zeppelin.notebook.socket.Message;
import org.apache.zeppelin.notebook.socket.Message.OP;
import org.apache.zeppelin.notebook.NotebookEventObserver.*;
import org.apache.zeppelin.scheduler.Job;
import org.apache.zeppelin.scheduler.Job.Status;
import org.apache.zeppelin.server.ZeppelinServer;
import org.apache.zeppelin.ticket.TicketContainer;
import org.apache.zeppelin.user.AuthenticationInfo;
import org.apache.zeppelin.utils.SecurityUtils;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Zeppelin websocket service.
 */
public class NotebookServer extends AppMainServer implements JobListenerFactory,
    AngularObjectRegistryListener, RemoteInterpreterProcessListener, Observer{
  private static final Logger LOG = LoggerFactory.getLogger(NotebookServer.class);

  /**
   * Job manager service type
   */
  protected enum JOB_MANAGER_SERVICE {
    JOB_MANAGER_PAGE("JOB_MANAGER_PAGE");
    private String serviceTypeKey;
    JOB_MANAGER_SERVICE(String serviceType) {
      this.serviceTypeKey = serviceType;
    }
    String getKey() {
      return this.serviceTypeKey;
    }
  }

  final Queue<WebAppSocket> connectedSockets = new ConcurrentLinkedQueue<>();

  private Notebook notebook() {
    return ZeppelinServer.notebook;
  }

  @Override
  public void onMessage(WebAppSocket conn, String msg) {
    Notebook notebook = notebook();
    try {
      Message messagereceived = deserializeMessage(msg);
      LOG.debug("RECEIVE << " + messagereceived.op);
      LOG.debug("RECEIVE PRINCIPAL << " + messagereceived.principal);
      LOG.debug("RECEIVE TICKET << " + messagereceived.ticket);
      LOG.debug("RECEIVE ROLES << " + messagereceived.roles);

      if (LOG.isTraceEnabled()) {
        LOG.trace("RECEIVE MSG = " + messagereceived);
      }

      String ticket = TicketContainer.instance.getTicket(messagereceived.principal);
      if (ticket != null && !ticket.equals(messagereceived.ticket))
        throw new Exception("Invalid ticket " + messagereceived.ticket + " != " + ticket);

      ZeppelinConfiguration conf = ZeppelinConfiguration.create();
      boolean allowAnonymous = conf.
          getBoolean(ConfVars.ZEPPELIN_ANONYMOUS_ALLOWED);
      if (!allowAnonymous && messagereceived.principal.equals("anonymous")) {
        throw new Exception("Anonymous access not allowed ");
      }

      HashSet<String> userAndRoles = new HashSet<String>();
      userAndRoles.add(messagereceived.principal);
      if (!messagereceived.roles.equals("")) {
        HashSet<String> roles = gson.fromJson(messagereceived.roles,
                new TypeToken<HashSet<String>>(){}.getType());
        if (roles != null) {
          userAndRoles.addAll(roles);
        }
      }
      /** Lets be elegant here */
      switch (messagereceived.op) {
          case LIST_NOTES:
            unicastNoteList(conn);
            break;
          case RELOAD_NOTES_FROM_REPO:
            broadcastReloadedNoteList();
            break;
          case GET_HOME_NOTE:
            sendHomeNote(conn, userAndRoles, notebook);
            break;
          case GET_NOTE:
            sendNote(conn, userAndRoles, notebook, messagereceived);
            break;
          case NEW_NOTE:
            createNote(conn, userAndRoles, notebook, messagereceived);
            break;
          case DEL_NOTE:
            removeNote(conn, userAndRoles, notebook, messagereceived);
            break;
          case CLONE_NOTE:
            cloneNote(conn, userAndRoles, notebook, messagereceived);
            break;
          case IMPORT_NOTE:
            importNote(conn, userAndRoles, notebook, messagereceived);
            break;
          case COMMIT_PARAGRAPH:
            updateParagraph(conn, userAndRoles, notebook, messagereceived);
            break;
          case RUN_PARAGRAPH:
            runParagraph(conn, userAndRoles, notebook, messagereceived);
            break;
          case CANCEL_PARAGRAPH:
            cancelParagraph(conn, userAndRoles, notebook, messagereceived);
            break;
          case MOVE_PARAGRAPH:
            moveParagraph(conn, userAndRoles, notebook, messagereceived);
            break;
          case INSERT_PARAGRAPH:
            insertParagraph(conn, userAndRoles, notebook, messagereceived);
            break;
          case PARAGRAPH_REMOVE:
            removeParagraph(conn, userAndRoles, notebook, messagereceived);
            break;
          case PARAGRAPH_CLEAR_OUTPUT:
            clearParagraphOutput(conn, userAndRoles, notebook, messagereceived);
            break;
          case NOTE_UPDATE:
            updateNote(conn, userAndRoles, notebook, messagereceived);
            break;
          case COMPLETION:
            completion(conn, userAndRoles, notebook, messagereceived);
            break;
          case PING:
            break; //do nothing
          case ANGULAR_OBJECT_UPDATED:
            angularObjectUpdated(conn, userAndRoles, notebook, messagereceived);
            break;
          case ANGULAR_OBJECT_CLIENT_BIND:
            angularObjectClientBind(conn, userAndRoles, notebook, messagereceived);
            break;
          case ANGULAR_OBJECT_CLIENT_UNBIND:
            angularObjectClientUnbind(conn, userAndRoles, notebook, messagereceived);
            break;
          case LIST_CONFIGURATIONS:
            sendAllConfigurations(conn, userAndRoles, notebook);
            break;
          case CHECKPOINT_NOTEBOOK:
            checkpointNotebook(conn, notebook, messagereceived);
            break;
          case LIST_NOTEBOOK_JOBS:
            sendNotebookJobInfo(conn);
            break;
          case LIST_UPDATE_NOTEBOOK_JOBS:
            sendUpdateNotebookJobInfo(conn, messagereceived);
            break;
          case UNSUBSCRIBE_JOBMANAGER:
            unsubscribeJobManager(conn);
            break;
          default:
            break;
      }
    } catch (Exception e) {
      LOG.error("Can't handle message", e);
    }
  }

  private void addConnectionToNote(String noteId, WebAppSocket socket) {
    addConnectionToKey(noteId, socket);
  }

  private void removeConnectionFromNote(String noteId, WebAppSocket socket) {
    removeConnectionFromKey(noteId, socket);
  }

  private void removeNote(String noteId) {
    removeKey(noteId);
  }

  private void removeConnectionFromAllNote(WebAppSocket socket) {
    removeConnectionFromAllKey(socket);
  }

  private String getOpenNoteId(WebAppSocket socket) {
    return getOpenKey(socket);
  }

  private void broadcastToNoteBindedInterpreter(String interpreterGroupId, Message m) {
    Notebook notebook = notebook();
    List<Note> notes = notebook.getAllNotes();
    for (Note note : notes) {
      List<String> ids = note.getNoteReplLoader().getInterpreters();
      for (String id : ids) {
        if (id.equals(interpreterGroupId)) {
          broadcast(note.id(), m);
        }
      }
    }
  }

  public List<Map<String, String>> generateNotebooksInfo(boolean needsReload) {
    Notebook notebook = notebook();

    ZeppelinConfiguration conf = notebook.getConf();
    String homescreenNotebookId = conf.getString(ConfVars.ZEPPELIN_NOTEBOOK_HOMESCREEN);
    boolean hideHomeScreenNotebookFromList = conf
            .getBoolean(ConfVars.ZEPPELIN_NOTEBOOK_HOMESCREEN_HIDE);

    if (needsReload) {
      try {
        notebook.reloadAllNotes();
      } catch (IOException e) {
        LOG.error("Fail to reload notes from repository", e);
      }
    }

    List<Note> notes = notebook.getAllNotes();
    List<Map<String, String>> notesInfo = new LinkedList<>();
    for (Note note : notes) {
      Map<String, String> info = new HashMap<>();

      if (hideHomeScreenNotebookFromList && note.id().equals(homescreenNotebookId)) {
        continue;
      }

      info.put("id", note.id());
      info.put("name", note.getName());
      notesInfo.add(info);
    }

    return notesInfo;
  }

  public void broadcastNote(Note note) {
    broadcast(note.id(), new Message(OP.NOTE).put("note", note));
  }

  public void broadcastNoteList() {
    List<Map<String, String>> notesInfo = generateNotebooksInfo(false);
    broadcastAll(new Message(OP.NOTES_INFO).put("notes", notesInfo));
  }

  public void unicastNoteList(WebAppSocket conn) {
    List<Map<String, String>> notesInfo = generateNotebooksInfo(false);
    unicast(new Message(OP.NOTES_INFO).put("notes", notesInfo), conn);
  }

  public void broadcastReloadedNoteList() {
    List<Map<String, String>> notesInfo = generateNotebooksInfo(true);
    broadcastAll(new Message(OP.NOTES_INFO).put("notes", notesInfo));
  }

  public void unsubscribeJobManager(WebAppSocket conn) {
    LOG.info("unsubscribe");
    removeConnectionFromAllKey(conn);
  }

  public void sendNotebookJobInfo(WebAppSocket conn) throws IOException {

    addConnectionToKey(JOB_MANAGER_SERVICE.JOB_MANAGER_PAGE.getKey(), conn);
    List<Map<String, Object>> notebookJobs = generateNotebooksJobInfo(false);
    Map<String, Object> response = new HashMap<>();

    response.put("lastResponseUnixTime", System.currentTimeMillis());
    response.put("jobs", notebookJobs);

    conn.send(serializeMessage(new Message(OP.LIST_NOTEBOOK_JOBS)
            .put("notebookJobs", response)));
  }

  public void sendUpdateNotebookJobInfo(WebAppSocket conn, Message fromMessage)
      throws IOException {
    double lastUpdateUnixTimeRaw = (double) fromMessage.get("lastUpdateUnixTime");
    long lastUpdateUnixTime = new Double(lastUpdateUnixTimeRaw).longValue();
    List<Map<String, Object>> notebookJobs;
    notebookJobs = generateUpdateNotebooksJobInfo(false, lastUpdateUnixTime);
    Map<String, Object> response = new HashMap<>();

    response.put("lastResponseUnixTime", System.currentTimeMillis());
    response.put("jobs", notebookJobs);

    conn.send(serializeMessage(new Message(OP.LIST_UPDATE_NOTEBOOK_JOBS)
            .put("notebookRunningJobs", response)));
  }

  private Map<String, Object> getParagraphPacketItem(Paragraph paragraph) {
    Map<String, Object> paragraphItem = new HashMap<>();

    // set paragraph id
    paragraphItem.put("id", paragraph.getId());

    // set paragraph name
    String paragraphName = paragraph.getTitle();
    if (paragraphName != null) {
      paragraphItem.put("name", paragraphName);
    } else {
      paragraphItem.put("name", paragraph.getId());
    }

    // set status for paragraph.
    paragraphItem.put("status", paragraph.getStatus().toString());

    return paragraphItem;
  }

  private long getUnixTimeLastRunParagraph(Paragraph paragraph) {

    Date lastRunningDate = null;
    long lastRunningUnixTime = 0;

    Date paragaraphDate = paragraph.getDateStarted();
    if (paragaraphDate == null) {
      paragaraphDate = paragraph.getDateCreated();
    }

    // set last update unixtime(ms).
    if (lastRunningDate == null) {
      lastRunningDate = paragaraphDate;
    } else {
      if (lastRunningDate.after(paragaraphDate) == true) {
        lastRunningDate = paragaraphDate;
      }
    }

    lastRunningUnixTime = lastRunningDate.getTime();

    return lastRunningUnixTime;
  }

  public List<Map<String, Object>> generateNotebooksJobInfo(boolean needsReload) {
    Notebook notebook = notebook();

    ZeppelinConfiguration conf = notebook.getConf();
    String homescreenNotebookId = conf.getString(ConfVars.ZEPPELIN_NOTEBOOK_HOMESCREEN);
    boolean hideHomeScreenNotebookFromList = conf
            .getBoolean(ConfVars.ZEPPELIN_NOTEBOOK_HOMESCREEN_HIDE);

    if (needsReload) {
      try {
        notebook.reloadAllNotes();
      } catch (IOException e) {
        LOG.error("Fail to reload notes from repository");
      }
    }

    List<Note> notes = notebook.getAllNotes();
    List<Map<String, Object>> notesInfo = new LinkedList<>();
    for (Note note : notes) {
      boolean isNotebookRunning = false;
      Map<String, Object> info = new HashMap<>();

      if (hideHomeScreenNotebookFromList && note.id().equals(homescreenNotebookId)) {
        continue;
      }

      String CRON_TYPE_NOTEBOOK_KEYWORD = "cron";
      info.put("notebookId", note.id());
      String notebookName = note.getName();
      if (notebookName != null) {
        info.put("notebookName", note.getName());
      } else {
        info.put("notebookName", "Note " + note.id());
      }

      if (note.getConfig().containsKey(CRON_TYPE_NOTEBOOK_KEYWORD) == true
              && !note.getConfig().get(CRON_TYPE_NOTEBOOK_KEYWORD).equals("")) {
        info.put("notebookType", "cron");
      }
      else {
        info.put("notebookType", "normal");
      }

      long lastRunningUnixTime = 0;

      List<Map<String, Object>> paragraphsInfo = new LinkedList<>();
      for (Paragraph paragraph : note.getParagraphs()) {
        if (paragraph.getStatus().isRunning() == true) {
          isNotebookRunning = true;
        }

        Map<String, Object> paragraphItem = getParagraphPacketItem(paragraph);

        lastRunningUnixTime = getUnixTimeLastRunParagraph(paragraph);

        paragraphsInfo.add(paragraphItem);
      }

      // Interpreter is set does not exist.
      String interpreterGroupName = null;
      if (note.getNoteReplLoader().getInterpreterSettings() != null
              && note.getNoteReplLoader().getInterpreterSettings().size() >= 1) {
        interpreterGroupName = note.getNoteReplLoader().getInterpreterSettings().get(0).getGroup();
      }

      // notebook json object root information.
      info.put("interpreter", interpreterGroupName);
      info.put("isRunningJob", isNotebookRunning);
      info.put("unixTimeLastRun", lastRunningUnixTime);
      info.put("paragraphs", paragraphsInfo);
      notesInfo.add(info);
    }
    return notesInfo;
  }


  public List<Map<String, Object>> generateUpdateNotebooksJobInfo(
          boolean needsReload, long lastUpdateServerUnixTime) {
    Notebook notebook = notebook();

    ZeppelinConfiguration conf = notebook.getConf();
    String homescreenNotebookId = conf.getString(ConfVars.ZEPPELIN_NOTEBOOK_HOMESCREEN);
    boolean hideHomeScreenNotebookFromList = conf
            .getBoolean(ConfVars.ZEPPELIN_NOTEBOOK_HOMESCREEN_HIDE);

    if (needsReload) {
      try {
        notebook.reloadAllNotes();
      } catch (IOException e) {
        LOG.error("Fail to reload notes from repository");
      }
    }

    List<Note> notes = notebook.getAllNotes();
    List<Map<String, Object>> notesInfo = new LinkedList<>();
    for (Note note : notes) {
      boolean isNotebookRunning = false;
      boolean isUpdateNotebook = false;

      Map<String, Object> info = new HashMap<>();

      if (hideHomeScreenNotebookFromList && note.id().equals(homescreenNotebookId)) {
        continue;
      }

      // set const keyword for cron type
      String CRON_TYPE_NOTEBOOK_KEYWORD = "cron";
      info.put("notebookId", note.id());
      String notebookName = note.getName();
      if (notebookName != null) {
        info.put("notebookName", note.getName());
      } else {
        info.put("notebookName", note.id());
      }

      if (note.getConfig().containsKey(CRON_TYPE_NOTEBOOK_KEYWORD) == true
              && !note.getConfig().get(CRON_TYPE_NOTEBOOK_KEYWORD).equals("")) {
        info.put("notebookType", "cron");
      }
      else {
        info.put("notebookType", "normal");
      }

      long lastRunningUnixTime = 0;

      List<Map<String, Object>> paragraphsInfo = new LinkedList<>();
      for (Paragraph paragraph : note.getParagraphs()) {

        // check date for update time.
        Date startedDate = paragraph.getDateStarted();
        Date createdDate = paragraph.getDateCreated();
        Date finishedDate = paragraph.getDateFinished();

        if (startedDate != null && startedDate.getTime() > lastUpdateServerUnixTime) {
          isUpdateNotebook = true;
        }
        if (createdDate != null && createdDate.getTime() > lastUpdateServerUnixTime) {
          isUpdateNotebook = true;
        }
        if (finishedDate != null && finishedDate.getTime() > lastUpdateServerUnixTime) {
          isUpdateNotebook = true;
        }

        Map<String, Object> paragraphItem = getParagraphPacketItem(paragraph);

        lastRunningUnixTime = getUnixTimeLastRunParagraph(paragraph);

        if (paragraph.getStatus().isRunning() == true) {
          isNotebookRunning = true;
          isUpdateNotebook = true;
        }
        paragraphsInfo.add(paragraphItem);
      }

      // Insert only data that has changed.
      if (isUpdateNotebook != true) {
        continue;
      }

      // Interpreter is set does not exist.
      String interpreterGroupName = null;
      if (note.getNoteReplLoader().getInterpreterSettings() != null
              && note.getNoteReplLoader().getInterpreterSettings().size() >= 1) {
        interpreterGroupName = note.getNoteReplLoader().getInterpreterSettings().get(0).getGroup();
      }

      // set notebook root information.
      info.put("interpreter", interpreterGroupName);
      info.put("isRunningJob", isNotebookRunning);
      info.put("unixTimeLastRun", lastRunningUnixTime);
      info.put("paragraphs", paragraphsInfo);
      notesInfo.add(info);
    }
    return notesInfo;
  }

  public boolean broadUpdateNote(String noteId) {

    Note note = notebook().getNote(noteId);

    if (note == null) {
      LOG.info("broadUpdateNote - not found note");
      return false;
    }

    List<Map<String, Object>> notesList = new LinkedList<>();

    Map<String, Object> noteItem = new HashMap<>();

    noteItem.put("notebookId", note.id());
    String notebookName = note.getName();
    if (notebookName != null) {
      noteItem.put("notebookName", note.getName());
    } else {
      noteItem.put("notebookName", note.id());
    }

    // set const keyword for cron type
    String CRON_TYPE_NOTEBOOK_KEYWORD = "cron";
    if (note.getConfig().containsKey(CRON_TYPE_NOTEBOOK_KEYWORD) == true
            && !note.getConfig().get(CRON_TYPE_NOTEBOOK_KEYWORD).equals("")) {
      noteItem.put("notebookType", "cron");
    }
    else {
      noteItem.put("notebookType", "normal");
    }


    long lastRunningUnixTime = 0;
    boolean isNotebookRunning = false;
    List<Map<String, Object>> paragraphsInfo = new LinkedList<>();
    for (Paragraph paragraph : note.getParagraphs()) {
      Map<String, Object> paragraphItem = getParagraphPacketItem(paragraph);

      if (paragraph.getStatus().isRunning() == true) {
        isNotebookRunning = true;
      }

      lastRunningUnixTime = getUnixTimeLastRunParagraph(paragraph);

      paragraphsInfo.add(paragraphItem);
    }

    // Interpreter is set does not exist.
    String interpreterGroupName = null;
    if (note.getNoteReplLoader().getInterpreterSettings() != null
            && note.getNoteReplLoader().getInterpreterSettings().size() >= 1) {
      interpreterGroupName = note.getNoteReplLoader().getInterpreterSettings().get(0).getGroup();
    }

    // set notebook root information.
    noteItem.put("interpreter", interpreterGroupName);
    noteItem.put("isRunningJob", isNotebookRunning);
    noteItem.put("unixTimeLastRun", lastRunningUnixTime);
    noteItem.put("paragraphs", paragraphsInfo);
    notesList.add(noteItem);

    Map<String, Object> response = new HashMap<>();

    response.put("lastResponseUnixTime", System.currentTimeMillis());
    response.put("jobs", notesList);

    broadcast(JOB_MANAGER_SERVICE.JOB_MANAGER_PAGE.getKey(),
            new Message(OP.LIST_UPDATE_NOTEBOOK_JOBS).put("notebookRunningJobs", response));

    return true;
  }

  public boolean broadRemovedNote(String noteId) {

    List<Map<String, Object>> notesList = new LinkedList<>();
    Map<String, Object> noteItem = new HashMap<>();

    // set notebook root information.
    noteItem.put("notebookId", noteId);
    noteItem.put("isRemoved", true);
    notesList.add(noteItem);

    Map<String, Object> response = new HashMap<>();

    response.put("lastResponseUnixTime", System.currentTimeMillis());
    response.put("jobs", notesList);

    broadcast(JOB_MANAGER_SERVICE.JOB_MANAGER_PAGE.getKey(),
            new Message(OP.LIST_UPDATE_NOTEBOOK_JOBS).put("notebookRunningJobs", response));

    return true;
  }

  /**
   * Notebook Observer Event Listener
   */
  @Override
  public void update(Observable observer, Object notebookChnagedEvent) {
    NotebookChnagedEvent noteEvent = (NotebookChnagedEvent) notebookChnagedEvent;
    try {
      if (noteEvent.getAction() == NotebookEventObserver.ACTIONS.REMOVED) {
        broadRemovedNote(noteEvent.getNoteId());
      } else {
        broadUpdateNote(noteEvent.getNoteId());
      }
    } catch (Exception e) {
      LOG.info("socket error job {}", e.getMessage());
    }
  }

  void permissionError(WebAppSocket conn, String op, Set<String> userAndRoles,
                       Set<String> allowed) throws IOException {
    LOG.info("Cannot {}. Connection readers {}. Allowed readers {}",
            op, userAndRoles, allowed);

    String userName = userAndRoles.iterator().next();

    conn.send(serializeMessage(new Message(OP.AUTH_INFO).put("info",
            "Insufficient privileges to " + op + " notebook.\n\n" +
                    "Allowed users or roles: " + allowed.toString() + "\n\n" +
                    "But the user " + userName + " belongs to: " + userAndRoles.toString())));
  }

  private void sendNote(WebAppSocket conn, HashSet<String> userAndRoles, Notebook notebook,
      Message fromMessage) throws IOException {

    LOG.info("New operation from {} : {} : {} : {} : {}", conn.getRequest().getRemoteAddr(),
            conn.getRequest().getRemotePort(),
            fromMessage.principal, fromMessage.op, fromMessage.get("id")
    );

    String noteId = (String) fromMessage.get("id");
    if (noteId == null) {
      return;
    }

    Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (note != null) {
      if (!notebookAuthorization.isReader(noteId, userAndRoles)) {
        permissionError(conn, "read", userAndRoles, notebookAuthorization.getReaders(noteId));
        return;
      }
      addConnectionToNote(note.id(), conn);
      conn.send(serializeMessage(new Message(OP.NOTE).put("note", note)));
      sendAllAngularObjects(note, conn);
    }
  }

  private void sendHomeNote(WebAppSocket conn, HashSet<String> userAndRoles,
                            Notebook notebook) throws IOException {
    String noteId = notebook.getConf().getString(ConfVars.ZEPPELIN_NOTEBOOK_HOMESCREEN);

    Note note = null;
    if (noteId != null) {
      note = notebook.getNote(noteId);
    }

    if (note != null) {
      NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
      if (!notebookAuthorization.isReader(noteId, userAndRoles)) {
        permissionError(conn, "read", userAndRoles, notebookAuthorization.getReaders(noteId));
        return;
      }
      addConnectionToNote(note.id(), conn);
      conn.send(serializeMessage(new Message(OP.NOTE).put("note", note)));
      sendAllAngularObjects(note, conn);
    } else {
      removeConnectionFromAllNote(conn);
      conn.send(serializeMessage(new Message(OP.NOTE).put("note", null)));
    }
  }

  private void updateNote(WebAppSocket conn, HashSet<String> userAndRoles,
                          Notebook notebook, Message fromMessage)
      throws SchedulerException, IOException {
    String noteId = (String) fromMessage.get("id");
    String name = (String) fromMessage.get("name");
    Map<String, Object> config = (Map<String, Object>) fromMessage
        .get("config");
    if (noteId == null) {
      return;
    }
    if (config == null) {
      return;
    }

    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "update", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    Note note = notebook.getNote(noteId);
    if (note != null) {
      boolean cronUpdated = isCronUpdated(config, note.getConfig());
      note.setName(name);
      note.setConfig(config);
      if (cronUpdated) {
        notebook.refreshCron(note.id());
      }

      note.persist();
      broadcastNote(note);
      broadcastNoteList();
    }
  }

  private boolean isCronUpdated(Map<String, Object> configA,
      Map<String, Object> configB) {
    boolean cronUpdated = false;
    if (configA.get("cron") != null && configB.get("cron") != null
        && configA.get("cron").equals(configB.get("cron"))) {
      cronUpdated = true;
    } else if (configA.get("cron") == null && configB.get("cron") == null) {
      cronUpdated = false;
    } else if (configA.get("cron") != null || configB.get("cron") != null) {
      cronUpdated = true;
    }

    return cronUpdated;
  }
  private void createNote(WebAppSocket conn, HashSet<String> userAndRoles,
                          Notebook notebook, Message message)
      throws IOException {
    Note note = notebook.createNote();
    note.addParagraph(); // it's an empty note. so add one paragraph
    if (message != null) {
      String noteName = (String) message.get("name");
      if (noteName == null || noteName.isEmpty()){
        noteName = "Note " + note.getId();
      }
      note.setName(noteName);
    }

    note.persist();
    addConnectionToNote(note.id(), (WebAppSocket) conn);
    conn.send(serializeMessage(new Message(OP.NEW_NOTE).put("note", note)));
    broadcastNoteList();
  }

  private void removeNote(WebAppSocket conn, HashSet<String> userAndRoles,
                          Notebook notebook, Message fromMessage)
      throws IOException {
    String noteId = (String) fromMessage.get("id");
    if (noteId == null) {
      return;
    }

    Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isOwner(noteId, userAndRoles)) {
      permissionError(conn, "remove", userAndRoles, notebookAuthorization.getOwners(noteId));
      return;
    }

    notebook.removeNote(noteId);
    removeNote(noteId);
    broadcastNoteList();
  }

  private void updateParagraph(WebAppSocket conn, HashSet<String> userAndRoles,
                               Notebook notebook, Message fromMessage) throws IOException {
    String paragraphId = (String) fromMessage.get("id");
    if (paragraphId == null) {
      return;
    }

    Map<String, Object> params = (Map<String, Object>) fromMessage
        .get("params");
    Map<String, Object> config = (Map<String, Object>) fromMessage
        .get("config");
    String noteId = getOpenNoteId(conn);
    final Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "write", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    Paragraph p = note.getParagraph(paragraphId);
    p.settings.setParams(params);
    p.setConfig(config);
    p.setTitle((String) fromMessage.get("title"));
    p.setText((String) fromMessage.get("paragraph"));
    note.persist();
    broadcast(note.id(), new Message(OP.PARAGRAPH).put("paragraph", p));
  }

  private void cloneNote(WebAppSocket conn, HashSet<String> userAndRoles,
                         Notebook notebook, Message fromMessage)
      throws IOException, CloneNotSupportedException {
    String noteId = getOpenNoteId(conn);
    String name = (String) fromMessage.get("name");
    Note newNote = notebook.cloneNote(noteId, name);
    addConnectionToNote(newNote.id(), (WebAppSocket) conn);
    conn.send(serializeMessage(new Message(OP.NEW_NOTE).put("note", newNote)));
    broadcastNoteList();
  }

  protected Note importNote(WebAppSocket conn, HashSet<String> userAndRoles,
                            Notebook notebook, Message fromMessage)
      throws IOException {
    Note note = null;
    if (fromMessage != null) {
      String noteName = (String) ((Map) fromMessage.get("notebook")).get("name");
      String noteJson = gson.toJson(fromMessage.get("notebook"));
      note = notebook.importNote(noteJson, noteName);
      note.persist();
      broadcastNote(note);
      broadcastNoteList();
    }
    return note;
  }

  private void removeParagraph(WebAppSocket conn, HashSet<String> userAndRoles,
                               Notebook notebook, Message fromMessage) throws IOException {
    final String paragraphId = (String) fromMessage.get("id");
    if (paragraphId == null) {
      return;
    }
    String noteId = getOpenNoteId(conn);
    final Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "write", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    /** We dont want to remove the last paragraph */
    if (!note.isLastParagraph(paragraphId)) {
      note.removeParagraph(paragraphId);
      note.persist();
      broadcastNote(note);
    }
  }

  private void clearParagraphOutput(WebAppSocket conn, HashSet<String> userAndRoles,
                                    Notebook notebook, Message fromMessage) throws IOException {
    final String paragraphId = (String) fromMessage.get("id");
    if (paragraphId == null) {
      return;
    }
    String noteId = getOpenNoteId(conn);
    final Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "write", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    note.clearParagraphOutput(paragraphId);
    broadcastNote(note);
  }

  private void completion(WebAppSocket conn, HashSet<String> userAndRoles, Notebook notebook,
      Message fromMessage) throws IOException {
    String paragraphId = (String) fromMessage.get("id");
    String buffer = (String) fromMessage.get("buf");
    int cursor = (int) Double.parseDouble(fromMessage.get("cursor").toString());
    Message resp = new Message(OP.COMPLETION_LIST).put("id", paragraphId);
    if (paragraphId == null) {
      conn.send(serializeMessage(resp));
      return;
    }

    final Note note = notebook.getNote(getOpenNoteId(conn));
    List<String> candidates = note.completion(paragraphId, buffer, cursor);
    resp.put("completions", candidates);
    conn.send(serializeMessage(resp));
  }

  /**
   * When angular object updated from client
   *
   * @param conn the web socket.
   * @param notebook the notebook.
   * @param fromMessage the message.
   */
  private void angularObjectUpdated(WebAppSocket conn, HashSet<String> userAndRoles,
                                    Notebook notebook, Message fromMessage) {
    String noteId = (String) fromMessage.get("noteId");
    String paragraphId = (String) fromMessage.get("paragraphId");
    String interpreterGroupId = (String) fromMessage.get("interpreterGroupId");
    String varName = (String) fromMessage.get("name");
    Object varValue = fromMessage.get("value");
    AngularObject ao = null;
    boolean global = false;
    // propagate change to (Remote) AngularObjectRegistry
    Note note = notebook.getNote(noteId);
    if (note != null) {
      List<InterpreterSetting> settings = note.getNoteReplLoader()
          .getInterpreterSettings();
      for (InterpreterSetting setting : settings) {
        if (setting.getInterpreterGroup(note.id()) == null) {
          continue;
        }
        if (interpreterGroupId.equals(setting.getInterpreterGroup(note.id()).getId())) {
          AngularObjectRegistry angularObjectRegistry = setting
              .getInterpreterGroup(note.id()).getAngularObjectRegistry();
          // first trying to get local registry
          ao = angularObjectRegistry.get(varName, noteId, paragraphId);
          if (ao == null) {
            // then try notebook scope registry
            ao = angularObjectRegistry.get(varName, noteId, null);
            if (ao == null) {
              // then try global scope registry
              ao = angularObjectRegistry.get(varName, null, null);
              if (ao == null) {
                LOG.warn("Object {} is not binded", varName);
              } else {
                // path from client -> server
                ao.set(varValue, false);
                global = true;
              }
            } else {
              // path from client -> server
              ao.set(varValue, false);
              global = false;
            }
          } else {
            ao.set(varValue, false);
            global = false;
          }
          break;
        }
      }
    }

    if (global) { // broadcast change to all web session that uses related
      // interpreter.
      for (Note n : notebook.getAllNotes()) {
        List<InterpreterSetting> settings = note.getNoteReplLoader()
            .getInterpreterSettings();
        for (InterpreterSetting setting : settings) {
          if (setting.getInterpreterGroup(n.id()) == null) {
            continue;
          }
          if (interpreterGroupId.equals(setting.getInterpreterGroup(n.id()).getId())) {
            AngularObjectRegistry angularObjectRegistry = setting
                .getInterpreterGroup(n.id()).getAngularObjectRegistry();
            this.broadcastExcept(
                n.id(),
                new Message(OP.ANGULAR_OBJECT_UPDATE).put("angularObject", ao)
                    .put("interpreterGroupId", interpreterGroupId)
                    .put("noteId", n.id())
                    .put("paragraphId", ao.getParagraphId()),
                conn);
          }
        }
      }
    } else { // broadcast to all web session for the note
      this.broadcastExcept(
          note.id(),
          new Message(OP.ANGULAR_OBJECT_UPDATE).put("angularObject", ao)
              .put("interpreterGroupId", interpreterGroupId)
              .put("noteId", note.id())
              .put("paragraphId", ao.getParagraphId()),
          conn);
    }
  }

  /**
   * Push the given Angular variable to the target
   * interpreter angular registry given a noteId
   * and a paragraph id
   * @param conn
   * @param notebook
   * @param fromMessage
   * @throws Exception
   */
  protected void angularObjectClientBind(WebAppSocket conn, HashSet<String> userAndRoles,
                                         Notebook notebook, Message fromMessage)
      throws Exception {
    String noteId = fromMessage.getType("noteId");
    String varName = fromMessage.getType("name");
    Object varValue = fromMessage.get("value");
    String paragraphId = fromMessage.getType("paragraphId");
    Note note = notebook.getNote(noteId);

    if (paragraphId == null) {
      throw new IllegalArgumentException("target paragraph not specified for " +
        "angular value bind");
    }

    if (note != null) {
      final InterpreterGroup interpreterGroup = findInterpreterGroupForParagraph(note,
              paragraphId);

      final AngularObjectRegistry registry = interpreterGroup.getAngularObjectRegistry();
      if (registry instanceof RemoteAngularObjectRegistry) {

        RemoteAngularObjectRegistry remoteRegistry = (RemoteAngularObjectRegistry) registry;
        pushAngularObjectToRemoteRegistry(noteId, paragraphId, varName, varValue, remoteRegistry,
                interpreterGroup.getId(), conn);

      } else {
        pushAngularObjectToLocalRepo(noteId, paragraphId, varName, varValue, registry,
                interpreterGroup.getId(), conn);
      }
    }
  }

  /**
   * Remove the given Angular variable to the target
   * interpreter(s) angular registry given a noteId
   * and an optional list of paragraph id(s)
   * @param conn
   * @param notebook
   * @param fromMessage
   * @throws Exception
   */
  protected void angularObjectClientUnbind(WebAppSocket conn, HashSet<String> userAndRoles,
                                           Notebook notebook, Message fromMessage)
      throws Exception{
    String noteId = fromMessage.getType("noteId");
    String varName = fromMessage.getType("name");
    String paragraphId = fromMessage.getType("paragraphId");
    Note note = notebook.getNote(noteId);

    if (paragraphId == null) {
      throw new IllegalArgumentException("target paragraph not specified for " +
              "angular value unBind");
    }

    if (note != null) {
      final InterpreterGroup interpreterGroup = findInterpreterGroupForParagraph(note,
              paragraphId);

      final AngularObjectRegistry registry = interpreterGroup.getAngularObjectRegistry();

      if (registry instanceof RemoteAngularObjectRegistry) {
        RemoteAngularObjectRegistry remoteRegistry = (RemoteAngularObjectRegistry) registry;
        removeAngularFromRemoteRegistry(noteId, paragraphId, varName, remoteRegistry,
                interpreterGroup.getId(), conn);
      } else {
        removeAngularObjectFromLocalRepo(noteId, paragraphId, varName, registry,
                interpreterGroup.getId(), conn);
      }
    }
  }

  private InterpreterGroup findInterpreterGroupForParagraph(Note note, String paragraphId)
      throws Exception {
    final Paragraph paragraph = note.getParagraph(paragraphId);
    if (paragraph == null) {
      throw new IllegalArgumentException("Unknown paragraph with id : " + paragraphId);
    }
    return paragraph.getCurrentRepl().getInterpreterGroup();
  }

  private void pushAngularObjectToRemoteRegistry(String noteId, String paragraphId,
     String varName, Object varValue, RemoteAngularObjectRegistry remoteRegistry,
     String interpreterGroupId, WebAppSocket conn) {

    final AngularObject ao = remoteRegistry.addAndNotifyRemoteProcess(varName, varValue,
            noteId, paragraphId);

    this.broadcastExcept(
            noteId,
            new Message(OP.ANGULAR_OBJECT_UPDATE).put("angularObject", ao)
                    .put("interpreterGroupId", interpreterGroupId)
                    .put("noteId", noteId)
                    .put("paragraphId", paragraphId),
            conn);
  }

  private void removeAngularFromRemoteRegistry(String noteId, String paragraphId,
    String varName, RemoteAngularObjectRegistry remoteRegistry,
    String interpreterGroupId, WebAppSocket conn) {
    final AngularObject ao = remoteRegistry.removeAndNotifyRemoteProcess(varName, noteId,
            paragraphId);
    this.broadcastExcept(
            noteId,
            new Message(OP.ANGULAR_OBJECT_REMOVE).put("angularObject", ao)
                    .put("interpreterGroupId", interpreterGroupId)
                    .put("noteId", noteId)
                    .put("paragraphId", paragraphId),
            conn);
  }

  private void pushAngularObjectToLocalRepo(String noteId, String paragraphId, String varName,
    Object varValue, AngularObjectRegistry registry,
    String interpreterGroupId, WebAppSocket conn) {
    AngularObject angularObject = registry.get(varName, noteId, paragraphId);
    if (angularObject == null) {
      angularObject = registry.add(varName, varValue, noteId, paragraphId);
    } else {
      angularObject.set(varValue, true);
    }

    this.broadcastExcept(
            noteId,
            new Message(OP.ANGULAR_OBJECT_UPDATE).put("angularObject", angularObject)
                    .put("interpreterGroupId", interpreterGroupId)
                    .put("noteId", noteId)
                    .put("paragraphId", paragraphId),
            conn);
  }

  private void removeAngularObjectFromLocalRepo(String noteId, String paragraphId, String varName,
    AngularObjectRegistry registry, String interpreterGroupId, WebAppSocket conn) {
    final AngularObject removed = registry.remove(varName, noteId, paragraphId);
    if (removed != null) {
      this.broadcastExcept(
              noteId,
              new Message(OP.ANGULAR_OBJECT_REMOVE).put("angularObject", removed)
                      .put("interpreterGroupId", interpreterGroupId)
                      .put("noteId", noteId)
                      .put("paragraphId", paragraphId),
              conn);
    }
  }

  private void moveParagraph(WebAppSocket conn, HashSet<String> userAndRoles, Notebook notebook,
      Message fromMessage) throws IOException {
    final String paragraphId = (String) fromMessage.get("id");
    if (paragraphId == null) {
      return;
    }

    final int newIndex = (int) Double.parseDouble(fromMessage.get("index")
        .toString());
    String noteId = getOpenNoteId(conn);
    final Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "write", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    note.moveParagraph(paragraphId, newIndex);
    note.persist();
    broadcastNote(note);
  }

  private void insertParagraph(WebAppSocket conn, HashSet<String> userAndRoles,
                               Notebook notebook, Message fromMessage) throws IOException {
    final int index = (int) Double.parseDouble(fromMessage.get("index")
            .toString());
    String noteId = getOpenNoteId(conn);
    final Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "write", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    note.insertParagraph(index);
    note.persist();
    broadcastNote(note);
  }

  private void cancelParagraph(WebAppSocket conn, HashSet<String> userAndRoles, Notebook notebook,
      Message fromMessage) throws IOException {
    final String paragraphId = (String) fromMessage.get("id");
    if (paragraphId == null) {
      return;
    }

    String noteId = getOpenNoteId(conn);
    final Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "write", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    Paragraph p = note.getParagraph(paragraphId);
    p.abort();
  }

  private void runParagraph(WebAppSocket conn, HashSet<String> userAndRoles, Notebook notebook,
      Message fromMessage) throws IOException {
    final String paragraphId = (String) fromMessage.get("id");
    if (paragraphId == null) {
      return;
    }

    String noteId = getOpenNoteId(conn);
    final Note note = notebook.getNote(noteId);
    NotebookAuthorization notebookAuthorization = notebook.getNotebookAuthorization();
    if (!notebookAuthorization.isWriter(noteId, userAndRoles)) {
      permissionError(conn, "write", userAndRoles, notebookAuthorization.getWriters(noteId));
      return;
    }

    Paragraph p = note.getParagraph(paragraphId);
    String text = (String) fromMessage.get("paragraph");
    p.setText(text);
    p.setTitle((String) fromMessage.get("title"));
    if (!fromMessage.principal.equals("anonymous")) {
      AuthenticationInfo authenticationInfo = new AuthenticationInfo(fromMessage.principal,
          fromMessage.ticket);
      p.setAuthenticationInfo(authenticationInfo);

    } else {
      p.setAuthenticationInfo(new AuthenticationInfo());
    }

    Map<String, Object> params = (Map<String, Object>) fromMessage
       .get("params");
    p.settings.setParams(params);
    Map<String, Object> config = (Map<String, Object>) fromMessage
       .get("config");
    p.setConfig(config);
    // if it's the last paragraph, let's add a new one
    boolean isTheLastParagraph = note.getLastParagraph().getId()
        .equals(p.getId());
    if (!Strings.isNullOrEmpty(text) && isTheLastParagraph) {
      note.addParagraph();
    }

    note.persist();
    try {
      note.run(paragraphId);
    } catch (Exception ex) {
      LOG.error("Exception from run", ex);
      if (p != null) {
        p.setReturn(
            new InterpreterResult(InterpreterResult.Code.ERROR, ex.getMessage()),
            ex);
        p.setStatus(Status.ERROR);
        broadcast(note.id(), new Message(OP.PARAGRAPH).put("paragraph", p));
      }
    }
  }

  private void sendAllConfigurations(WebAppSocket conn, HashSet<String> userAndRoles,
                                     Notebook notebook) throws IOException {
    ZeppelinConfiguration conf = notebook.getConf();

    Map<String, String> configurations = conf.dumpConfigurations(conf,
        new ZeppelinConfiguration.ConfigurationKeyPredicate() {
          @Override
          public boolean apply(String key) {
            return !key.contains("password") &&
                !key.equals(
                        ConfVars
                    .ZEPPELIN_NOTEBOOK_AZURE_CONNECTION_STRING
                    .getVarName());
          }
        });

    conn.send(serializeMessage(new Message(OP.CONFIGURATIONS_INFO)
        .put("configurations", configurations)));
  }

  private void checkpointNotebook(WebAppSocket conn, Notebook notebook,
      Message fromMessage) throws IOException {
    String noteId = (String) fromMessage.get("noteId");
    String commitMessage = (String) fromMessage.get("commitMessage");
    notebook.checkpointNote(noteId, commitMessage);
  }

  /**
   * This callback is for the paragraph that runs on ZeppelinServer
   * @param noteId
   * @param paragraphId
   * @param output output to append
   */
  @Override
  public void onOutputAppend(String noteId, String paragraphId, String output) {
    Message msg = new Message(OP.PARAGRAPH_APPEND_OUTPUT)
            .put("noteId", noteId)
            .put("paragraphId", paragraphId)
            .put("data", output);
    Paragraph paragraph = notebook().getNote(noteId).getParagraph(paragraphId);
    broadcast(noteId, msg);
  }

  /**
   * This callback is for the paragraph that runs on ZeppelinServer
   * @param noteId
   * @param paragraphId
   * @param output output to update (replace)
   */
  @Override
  public void onOutputUpdated(String noteId, String paragraphId, String output) {
    Message msg = new Message(OP.PARAGRAPH_UPDATE_OUTPUT)
            .put("noteId", noteId)
            .put("paragraphId", paragraphId)
            .put("data", output);
    Paragraph paragraph = notebook().getNote(noteId).getParagraph(paragraphId);
    broadcast(noteId, msg);
  }

  /**
   * Need description here.
   *
   */
  public static class ParagraphListenerImpl implements ParagraphJobListener {
    private NotebookServer appMainServer;
    private Note note;

    public ParagraphListenerImpl(NotebookServer appMainServer, Note note) {
      this.appMainServer = appMainServer;
      this.note = note;
    }

    @Override
    public void onProgressUpdate(Job job, int progress) {
      appMainServer.broadcast(
          note.id(),
          new Message(OP.PROGRESS).put("id", job.getId()).put("progress",
              job.progress()));
    }

    @Override
    public void beforeStatusChange(Job job, Status before, Status after) {
    }

    @Override
    public void afterStatusChange(Job job, Status before, Status after) {
      if (after == Status.ERROR) {
        if (job.getException() != null) {
          LOG.error("Error", job.getException());
        }
      }

      if (job.isTerminated()) {
        LOG.info("Job {} is finished", job.getId());
        try {
          note.persist();
        } catch (IOException e) {
          LOG.error(e.toString(), e);
        }
      }
      appMainServer.broadcastNote(note);
    }

    /**
     * This callback is for praragraph that runs on RemoteInterpreterProcess
     * @param paragraph
     * @param out
     * @param output
     */
    @Override
    public void onOutputAppend(Paragraph paragraph, InterpreterOutput out, String output) {
      Message msg = new Message(OP.PARAGRAPH_APPEND_OUTPUT)
              .put("noteId", paragraph.getNote().getId())
              .put("paragraphId", paragraph.getId())
              .put("data", output);

      appMainServer.broadcast(paragraph.getNote().getId(), msg);
    }

    /**
     * This callback is for paragraph that runs on RemoteInterpreterProcess
     * @param paragraph
     * @param out
     * @param output
     */
    @Override
    public void onOutputUpdate(Paragraph paragraph, InterpreterOutput out, String output) {
      Message msg = new Message(OP.PARAGRAPH_UPDATE_OUTPUT)
              .put("noteId", paragraph.getNote().getId())
              .put("paragraphId", paragraph.getId())
              .put("data", output);

      appMainServer.broadcast(paragraph.getNote().getId(), msg);
    }
  }

  @Override
  public ParagraphJobListener getParagraphJobListener(Note note) {
    return new ParagraphListenerImpl(this, note);
  }

  private void sendAllAngularObjects(Note note, WebAppSocket conn) throws IOException {
    List<InterpreterSetting> settings = note.getNoteReplLoader()
        .getInterpreterSettings();
    if (settings == null || settings.size() == 0) {
      return;
    }

    for (InterpreterSetting intpSetting : settings) {
      AngularObjectRegistry registry = intpSetting.getInterpreterGroup(note.id())
          .getAngularObjectRegistry();
      List<AngularObject> objects = registry.getAllWithGlobal(note.id());
      for (AngularObject object : objects) {
        conn.send(serializeMessage(new Message(OP.ANGULAR_OBJECT_UPDATE)
            .put("angularObject", object)
            .put("interpreterGroupId",
                intpSetting.getInterpreterGroup(note.id()).getId())
            .put("noteId", note.id())
            .put("paragraphId", object.getParagraphId())
        ));
      }
    }
  }

  @Override
  public void onAdd(String interpreterGroupId, AngularObject object) {
    onUpdate(interpreterGroupId, object);
  }

  @Override
  public void onUpdate(String interpreterGroupId, AngularObject object) {
    Notebook notebook = notebook();
    if (notebook == null) {
      return;
    }

    List<Note> notes = notebook.getAllNotes();
    for (Note note : notes) {
      if (object.getNoteId() != null && !note.id().equals(object.getNoteId())) {
        continue;
      }

      List<InterpreterSetting> intpSettings = note.getNoteReplLoader()
          .getInterpreterSettings();
      if (intpSettings.isEmpty())
        continue;
      for (InterpreterSetting setting : intpSettings) {
        if (setting.getInterpreterGroup(note.id()).getId().equals(interpreterGroupId)) {
          broadcast(
              note.id(),
              new Message(OP.ANGULAR_OBJECT_UPDATE)
                  .put("angularObject", object)
                  .put("interpreterGroupId", interpreterGroupId)
                  .put("noteId", note.id())
                  .put("paragraphId", object.getParagraphId()));
        }
      }
    }
  }

  @Override
  public void onRemove(String interpreterGroupId, String name, String noteId, String paragraphId) {
    Notebook notebook = notebook();
    List<Note> notes = notebook.getAllNotes();
    for (Note note : notes) {
      if (noteId != null && !note.id().equals(noteId)) {
        continue;
      }

      List<String> ids = note.getNoteReplLoader().getInterpreters();
      for (String id : ids) {
        if (id.equals(interpreterGroupId)) {
          broadcast(
              note.id(),
              new Message(OP.ANGULAR_OBJECT_REMOVE).put("name", name).put(
                      "noteId", noteId).put("paragraphId", paragraphId));
        }
      }
    }
  }
}

