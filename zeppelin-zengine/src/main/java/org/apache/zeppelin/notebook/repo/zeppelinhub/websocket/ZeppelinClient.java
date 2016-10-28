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
package org.apache.zeppelin.notebook.repo.zeppelinhub.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.notebook.repo.zeppelinhub.security.Authentication;
import org.apache.zeppelin.notebook.repo.zeppelinhub.websocket.listener.WatcherWebsocket;
import org.apache.zeppelin.notebook.repo.zeppelinhub.websocket.listener.ZeppelinWebsocket;
import org.apache.zeppelin.notebook.repo.zeppelinhub.websocket.protocol.ZeppelinhubMessage;
import org.apache.zeppelin.notebook.repo.zeppelinhub.websocket.scheduler.SchedulerService;
import org.apache.zeppelin.notebook.repo.zeppelinhub.websocket.scheduler.ZeppelinHeartbeat;
import org.apache.zeppelin.notebook.socket.Message;
import org.apache.zeppelin.notebook.socket.Message.OP;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Zeppelin websocket client.
 *
 */
public class ZeppelinClient {
  private static final Logger LOG = LoggerFactory.getLogger(ZeppelinClient.class);
  private final URI zeppelinWebsocketUrl;
  private final String zeppelinhubToken;
  private final WebSocketClient wsClient;
  private static Gson gson;
  // Keep track of current open connection per notebook.
  private ConcurrentHashMap<String, Session> notesConnection;
  // Listen to every note actions.
  private static Session watcherSession;
  private static ZeppelinClient instance = null;
  private SchedulerService schedulerService;
  private Authentication authModule;
  private static final int MIN = 60;

  public static ZeppelinClient initialize(String zeppelinUrl, String token, 
      ZeppelinConfiguration conf) {
    if (instance == null) {
      instance = new ZeppelinClient(zeppelinUrl, token, conf);
    }
    return instance;
  }

  public static ZeppelinClient getInstance() {
    return instance;
  }

  private ZeppelinClient(String zeppelinUrl, String token, ZeppelinConfiguration conf) {
    zeppelinWebsocketUrl = URI.create(zeppelinUrl);
    zeppelinhubToken = token;
    wsClient = createNewWebsocketClient();
    gson = new Gson();
    notesConnection = new ConcurrentHashMap<>();
    schedulerService = SchedulerService.getInstance();
    authModule = Authentication.initialize(token, conf);
    if (authModule != null) {
      SchedulerService.getInstance().addOnce(authModule, 10);
    }
    LOG.info("Initialized Zeppelin websocket client on {}", zeppelinWebsocketUrl);
  }

  private WebSocketClient createNewWebsocketClient() {
    SslContextFactory sslContextFactory = new SslContextFactory();
    WebSocketClient client = new WebSocketClient(sslContextFactory);
    client.setMaxIdleTimeout(5 * MIN * 1000);
    client.setMaxTextMessageBufferSize(Client.getMaxNoteSize());
    client.getPolicy().setMaxTextMessageSize(Client.getMaxNoteSize());
    //TODO(khalid): other client settings
    return client;
  }

  public void start() {
    try {
      if (wsClient != null) {
        wsClient.start();
        addRoutines();
      } else {
        LOG.warn("Cannot start zeppelin websocket client - isn't initialized");
      }
    } catch (Exception e) {
      LOG.error("Cannot start Zeppelin websocket client", e);
    }
  }

  private void addRoutines() {
    schedulerService.add(ZeppelinHeartbeat.newInstance(this), 10, 1 * MIN);
    new Timer().schedule(new java.util.TimerTask() {
      @Override
      public void run() {
        watcherSession = openWatcherSession();
      }
    }, 5000);
  }

  public void stop() {
    try {
      if (wsClient != null) {
        wsClient.stop();
      } else {
        LOG.warn("Cannot stop zeppelin websocket client - isn't initialized");
      }
      if (watcherSession != null) {
        watcherSession.close();
      }
    } catch (Exception e) {
      LOG.error("Cannot stop Zeppelin websocket client", e);
    }
  }

  public String serialize(Message zeppelinMsg) {
    if (credentialsAvailable()) {
      zeppelinMsg.principal = authModule.getPrincipal();
      zeppelinMsg.ticket = authModule.getTicket();
      zeppelinMsg.roles = authModule.getRoles();
    }
    String msg = gson.toJson(zeppelinMsg);
    return msg;
  }

  private boolean credentialsAvailable() {
    return Authentication.getInstance() != null && Authentication.getInstance().isAuthenticated();
  }

  public Message deserialize(String zeppelinMessage) {
    if (StringUtils.isBlank(zeppelinMessage)) {
      return null;
    }
    Message msg;
    try {
      msg = gson.fromJson(zeppelinMessage, Message.class);
    } catch (JsonSyntaxException ex) {
      LOG.error("Cannot deserialize zeppelin message", ex);
      msg = null;
    }
    return msg;
  }
  
  private Session openWatcherSession() {
    ClientUpgradeRequest request = new ClientUpgradeRequest();
    WatcherWebsocket socket = WatcherWebsocket.createInstace();
    Future<Session> future = null;
    Session session = null;
    try {
      future = wsClient.connect(socket, zeppelinWebsocketUrl, request);
      session = future.get();
    } catch (IOException | InterruptedException | ExecutionException e) {
      LOG.error("Couldn't establish websocket connection to Zeppelin ", e);
      return session;
    }
    return session;
  }

  public void send(Message msg, String noteId) {
    Session noteSession = getZeppelinConnection(noteId);
    if (!isSessionOpen(noteSession)) {
      LOG.error("Cannot open websocket connection to Zeppelin note {}", noteId);
      return;
    }
    noteSession.getRemote().sendStringByFuture(serialize(msg));
  }
  
  public Session getZeppelinConnection(String noteId) {
    if (StringUtils.isBlank(noteId)) {
      LOG.warn("Cannot get Websocket session with blanck noteId");
      return null;
    }
    return getNoteSession(noteId);
  }
  
/*
  private Message zeppelinGetNoteMsg(String noteId) {
    Message getNoteMsg = new Message(Message.OP.GET_NOTE);
    HashMap<String, Object> data = new HashMap<>();
    data.put("id", noteId);
    getNoteMsg.data = data;
    return getNoteMsg;
  }
  */

  private Session getNoteSession(String noteId) {
    LOG.info("Getting Note websocket connection for note {}", noteId);
    Session session = notesConnection.get(noteId);
    if (!isSessionOpen(session)) {
      LOG.info("No open connection for note {}, opening one", noteId);
      notesConnection.remove(noteId);
      session = openNoteSession(noteId);
    }
    return session;
  }
  
  private Session openNoteSession(String noteId) {
    ClientUpgradeRequest request = new ClientUpgradeRequest();
    ZeppelinWebsocket socket = new ZeppelinWebsocket(noteId);
    Future<Session> future = null;
    Session session = null;
    try {
      future = wsClient.connect(socket, zeppelinWebsocketUrl, request);
      session = future.get();
    } catch (IOException | InterruptedException | ExecutionException e) {
      LOG.error("Couldn't establish websocket connection to Zeppelin ", e);
      return session;
    }

    if (notesConnection.containsKey(noteId)) {
      session.close();
      session = notesConnection.get(noteId);
    } else {
      String getNote = serialize(zeppelinGetNoteMsg(noteId));
      session.getRemote().sendStringByFuture(getNote);
      notesConnection.put(noteId, session);
    }
    return session;
  }
  
  private boolean isSessionOpen(Session session) {
    return (session != null) && (session.isOpen());
  }

  private Message zeppelinGetNoteMsg(String noteId) {
    Message getNoteMsg = new Message(Message.OP.GET_NOTE);
    HashMap<String, Object> data = new HashMap<String, Object>();
    data.put("id", noteId);
    getNoteMsg.data = data;
    return getNoteMsg;
  }

  public void handleMsgFromZeppelin(String message, String noteId) {
    Map<String, String> meta = new HashMap<>();
    meta.put("token", zeppelinhubToken);
    meta.put("noteId", noteId);
    Message zeppelinMsg = deserialize(message);
    if (zeppelinMsg == null) {
      return;
    }
    ZeppelinhubMessage hubMsg = ZeppelinhubMessage.newMessage(zeppelinMsg, meta);
    Client client = Client.getInstance();
    if (client == null) {
      LOG.warn("Client isn't initialized yet");
      return;
    }
    client.relayToZeppelinHub(hubMsg.serialize());
  }

  public void removeNoteConnection(String noteId) {
    if (StringUtils.isBlank(noteId)) {
      LOG.error("Cannot remove session for empty noteId");
      return;
    }
    if (notesConnection.containsKey(noteId)) {
      Session connection = notesConnection.get(noteId);
      if (connection.isOpen()) {
        connection.close();
      }
      notesConnection.remove(noteId);
    }
    LOG.info("Removed note websocket connection for note {}", noteId);
  }

  public void ping() {
    if (watcherSession == null) {
      LOG.info("Cannot send PING event, watcher is null");
      return;
    }
    watcherSession.getRemote().sendStringByFuture(serialize(new Message(OP.PING)));
  }
}
