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

package org.apache.zeppelin.notebook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.user.AuthenticationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.gson.annotations.SerializedName;

/**
 * Contains authorization information for resources, when resources are notes and folders.
 * To differ notes from folders folderId should always starts with slash (/)
 */
public class NotebookAuthorization {
  private static final Logger LOG = LoggerFactory.getLogger(NotebookAuthorization.class);
  private static NotebookAuthorization instance = null;
  /*
   * { "note1": { "owners": ["u1"], "readers": ["u1", "u2"], "writers": ["u1"] },  "note2": ... } }
   */
  private static Map<String, Map<PermissionType, Set<String>>> authInfo = new HashMap<>();

  private NotesInfoProvider notesInfoProvider;
  /*
   * contains roles for each user
   */
  private static Map<String, Set<String>> userRoles = new HashMap<>();
  private static ZeppelinConfiguration conf;
  private static String filePath;

  private NotebookAuthorization() {}

  public static NotebookAuthorization init(ZeppelinConfiguration config) {
    if (instance == null) {
      instance = new NotebookAuthorization();
      conf = config;
      filePath = conf.getNotebookAuthorizationPath();
      try {
        loadFromFile();
      } catch (IOException e) {
        LOG.error("Error loading NotebookAuthorization", e);
      }
    }
    return instance;
  }

  public static NotebookAuthorization getInstance() {
    if (instance == null) {
      LOG.warn("Notebook authorization module was called without initialization,"
          + " initializing with default configuration");
      init(ZeppelinConfiguration.create());
    }
    return instance;
  }

  private static void loadFromFile() throws IOException {
    File settingFile = new File(filePath);
    LOG.info(settingFile.getAbsolutePath());
    if (!settingFile.exists()) {
      // nothing to read
      return;
    }
    FileInputStream fis = new FileInputStream(settingFile);
    InputStreamReader isr = new InputStreamReader(fis);
    BufferedReader bufferedReader = new BufferedReader(isr);
    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      sb.append(line);
    }
    isr.close();
    fis.close();

    String json = sb.toString();
    NotebookAuthorizationInfoSaving info = NotebookAuthorizationInfoSaving.fromJson(json);
    authInfo = info.authInfo;
  }

  public void setRoles(String user, Set<String> roles) {
    if (StringUtils.isBlank(user)) {
      LOG.warn("Setting roles for empty user");
      return;
    }
    roles = filterEmpty(roles);
    userRoles.put(user, roles);
  }

  public Set<String> getRoles(String user) {
    Set<String> roles = new HashSet<>();
    if (userRoles.containsKey(user)) {
      roles.addAll(userRoles.get(user));
    }
    return roles;
  }

  public void setNotesInfoProvider(NotesInfoProvider notesInfoProvider) {
    this.notesInfoProvider = notesInfoProvider;
  }

  private void saveToFile() {
    String jsonString;

    synchronized (authInfo) {
      NotebookAuthorizationInfoSaving info = new NotebookAuthorizationInfoSaving();
      info.authInfo = authInfo;
      jsonString = info.toJson();
    }

    try {
      File settingFile = new File(filePath);
      if (!settingFile.exists()) {
        settingFile.createNewFile();
      }

      FileOutputStream fos = new FileOutputStream(settingFile, false);
      OutputStreamWriter out = new OutputStreamWriter(fos);
      out.append(jsonString);
      out.close();
      fos.close();
    } catch (IOException e) {
      LOG.error("Error saving notebook authorization file: " + e.getMessage());
    }
  }

  public boolean isPublic() {
    return conf.isNotebokPublic();
  }

  private Set<String> filterEmpty(Set<String> elements) {
    return FluentIterable.from(elements).filter(new Predicate<String>() {
      @Override
      public boolean apply(String input) {
        return !input.trim().isEmpty();
      }
    }).toSet();
  }

  public void setOwners(String resourceId, Set<String> entities) {
    checkCanSetPermissions(resourceId);
    setPermissionsRecursively(resourceId, entities, PermissionType.OWNER);
    saveToFile();
  }

  public void setReaders(String resourceId, Set<String> entities) {
    checkCanSetPermissions(resourceId);
    setPermissionsRecursively(resourceId, entities, PermissionType.READER);
    saveToFile();
  }

  public void setWriters(String resourceId, Set<String> entities) {
    checkCanSetPermissions(resourceId);
    setPermissionsRecursively(resourceId, entities, PermissionType.WRITER);
    saveToFile();
  }

  private void checkCanSetPermissions(String resourceId) {
    if (isRootFolder(resourceId)) {
      throw new RuntimeException("Cannot change permissions of the root folder");
    }
    if (isResourceUnderFolderWithPermissions(resourceId)) {
      throw new RuntimeException("Cannot change permissions for resource " +
          "under folder with permissions. Id = " + resourceId);
    }
  }

  private boolean isRootFolder(String resourceId) {
    return isFolderId(resourceId) && notesInfoProvider.getFolder(resourceId).isRoot();
  }

  private boolean isResourceUnderFolderWithPermissions(String resourceId) {
    Folder folder = getParentFolder(resourceId);
    Map<PermissionType, Set<String>> resourceAuthInfo = authInfo.get(folder.getId());
    return !isPermissionsEmpty(resourceAuthInfo);
  }

  private boolean isPermissionsEmpty(Map<PermissionType, Set<String>> resourceAuthInfo) {
    if (resourceAuthInfo == null || resourceAuthInfo.isEmpty())
      return true;
    for (Set<String> permissions: resourceAuthInfo.values()) {
      if (!permissions.isEmpty())
        return false;
    }
    return true;
  }

  private void setPermissionsRecursively(String resourceId, Set<String> entities,
      PermissionType permissionType) {
    setPermissions(resourceId, entities, permissionType);
    if (isFolderId(resourceId)) {
      Folder folder = notesInfoProvider.getFolder(resourceId);
      Set<String> subFolders = folder.getChildren().keySet();
      for (String folderId: subFolders){
        setPermissionsRecursively('/' + folderId, entities, permissionType);
      }
      List<Note> notes = folder.getNotes();
      for (Note note: notes) {
        setPermissionsRecursively(note.getId(), entities, permissionType);
      }
    }
  }

  private void setPermissions(String resourceId, Set<String> entities,
      PermissionType permissionType) {
    Map<PermissionType, Set<String>> noteAuthInfo = authInfo.get(resourceId);
    entities = filterEmpty(entities);
    if (noteAuthInfo == null) {
      noteAuthInfo = new LinkedHashMap<>();
      for (PermissionType type: PermissionType.values()) {
        if (type != permissionType) {
          noteAuthInfo.put(type, new LinkedHashSet<String>());
        }
      }
    }
    noteAuthInfo.put(permissionType, entities);
    authInfo.put(resourceId, noteAuthInfo);
  }

  private Set<String> getPermissions(String resourceId, PermissionType permissionType) {
    Map<PermissionType, Set<String>> resourceAuthInfo = authInfo.get(resourceId);
    Set<String> entities;
    if (resourceAuthInfo == null) {
      entities = new HashSet<>();
    } else {
      entities = resourceAuthInfo.get(permissionType);
      if (entities == null) {
        entities = new HashSet<>();
      }
    }
    return entities;
  }

  public Set<String> getOwners(String resourceId) {
    return getPermissions(resourceId, PermissionType.OWNER);
  }

  private boolean isFolderId(String resourceId) {
    return resourceId.charAt(0) == '/';
  }

  public Set<String> getReaders(String resourceId) {
    return getPermissions(resourceId, PermissionType.READER);
  }

  public Set<String> getWriters(String resourceId) {
    return getPermissions(resourceId, PermissionType.WRITER);
  }

  public boolean isOwner(String resourceId, Set<String> entities) {
    return isMember(entities, getOwners(resourceId));
  }

  public boolean isWriter(String resourceId, Set<String> entities) {
    return isMember(entities, getWriters(resourceId)) || isMember(entities, getOwners(resourceId));
  }

  public boolean isReader(String resourceId, Set<String> entities) {
    return isMember(entities, getReaders(resourceId)) ||
            isMember(entities, getOwners(resourceId)) ||
            isMember(entities, getWriters(resourceId));
  }

  // return true if b is empty or if (a intersection b) is non-empty
  private boolean isMember(Set<String> a, Set<String> b) {
    Set<String> intersection = new HashSet<>(b);
    intersection.retainAll(a);
    return (b.isEmpty() || (intersection.size() > 0));
  }

  public boolean isOwner(Set<String> userAndRoles, String resourceId) {
    if (conf.isAnonymousAllowed()) {
      LOG.debug("Zeppelin runs in anonymous mode, everybody is owner");
      return true;
    }
    return userAndRoles != null && isOwner(resourceId, userAndRoles);
  }

  public boolean hasWriteAuthorization(Set<String> userAndRoles, String resourceId) {
    if (conf.isAnonymousAllowed()) {
      LOG.debug("Zeppelin runs in anonymous mode, everybody is writer");
      return true;
    }
    return userAndRoles != null && isWriter(resourceId, userAndRoles);
  }

  public boolean hasReadAuthorization(Set<String> userAndRoles, String resourceId) {
    if (conf.isAnonymousAllowed()) {
      LOG.debug("Zeppelin runs in anonymous mode, everybody is reader");
      return true;
    }
    return userAndRoles != null && isReader(resourceId, userAndRoles);
  }

  public void removeResource(String resourceId) {
    authInfo.remove(resourceId);
    saveToFile();
  }

  public List<NoteInfo> filterByUser(List<NoteInfo> notes, AuthenticationInfo subject) {
    final Set<String> entities = new HashSet<>();
    if (subject != null) {
      entities.add(subject.getUser());
    }
    return FluentIterable.from(notes).filter(new Predicate<NoteInfo>() {
      @Override
      public boolean apply(NoteInfo input) {
        return input != null && isReader(input.getId(), entities);
      }
    }).toList();
  }

  public void setNewNotePermissions(String noteId, AuthenticationInfo subject) {
    if (!AuthenticationInfo.isAnonymous(subject)) {
      if (isPublic()) {
        if (!isResourceUnderFolderWithPermissions(noteId)) {
          // add current user to owners - can be public
          Set<String> owners = getOwners(noteId);
          owners.add(subject.getUser());
          setOwners(noteId, owners);
        } else {
          Folder folder = getParentFolder(noteId);
          Map<PermissionType, Set<String>> resourceAuthInfo = authInfo.get(folder.getId());
          for (Map.Entry<PermissionType, Set<String>> e: resourceAuthInfo.entrySet()) {
            setPermissions(noteId, e.getValue(), e.getKey());
          }
        }
      } else {
        if (isResourceUnderFolderWithPermissions(noteId)) {
          Folder folder = getParentFolder(noteId);
          Map<PermissionType, Set<String>> resourceAuthInfo = authInfo.get(folder.getId());
          for (Set<String> entries: resourceAuthInfo.values()) {
            if (!(entries.size() == 1 && entries.iterator().next().equals(subject.getUser()))){
              throw new RuntimeException("Cannot set private note permissions" +
                  " because of parent folder permissions");
            }
          }
        }
        // add current user to owners, readers, writers - private note
        Set<String> entities = getOwners(noteId);
        entities.add(subject.getUser());
        setOwners(noteId, entities);
        entities = getReaders(noteId);
        entities.add(subject.getUser());
        setReaders(noteId, entities);
        entities = getWriters(noteId);
        entities.add(subject.getUser());
        setWriters(noteId, entities);
      }
    }
  }

  private Folder getParentFolder(String resourceId){
    if (isFolderId(resourceId)) {
      return notesInfoProvider.getFolder(resourceId).getParent();
    } else {
      return notesInfoProvider.getFolderByNoteId(resourceId);
    }
  }

  /**
   * Permission type
   */
  public enum PermissionType {
    @SerializedName("readers") READER,
    @SerializedName("writers") WRITER,
    @SerializedName("owners") OWNER
  }
}
