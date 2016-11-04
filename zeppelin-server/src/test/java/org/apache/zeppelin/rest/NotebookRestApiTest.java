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

package org.apache.zeppelin.rest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.zeppelin.exception.DuplicateNameException;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.notebook.Note;
import org.apache.zeppelin.notebook.Paragraph;
import org.apache.zeppelin.server.ZeppelinServer;
import org.apache.zeppelin.user.AuthenticationInfo;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Zeppelin notebook rest api tests
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotebookRestApiTest extends AbstractTestRestApi {
  Gson gson = new Gson();
  AuthenticationInfo anonymous;
  private final String EMPTY_STRING = "";

  @BeforeClass
  public static void init() throws Exception {
    AbstractTestRestApi.startUp();
  }

  @AfterClass
  public static void destroy() throws Exception {
    AbstractTestRestApi.shutDown();
  }

  @Before
  public void setUp() {
    anonymous = new AuthenticationInfo("anonymous");
  }

  @Test
  public void testPermissions() throws IOException, DuplicateNameException {
    Note note1 = ZeppelinServer.notebook.createNote(anonymous, EMPTY_STRING);
    // Set only readers
    String jsonRequest = "{\"readers\":[\"admin-team\"],\"owners\":[]," +
            "\"writers\":[]}";
    PutMethod put = httpPut("/notebook/" + note1.getId() + "/permissions/", jsonRequest);
    LOG.info("testPermissions response\n" + put.getResponseBodyAsString());
    assertThat("test update method:", put, isAllowed());
    put.releaseConnection();


    GetMethod get = httpGet("/notebook/" + note1.getId() + "/permissions/");
    assertThat(get, isAllowed());
    Map<String, Object> resp = gson.fromJson(get.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    Map<String, Set<String>> authInfo = (Map<String, Set<String>>) resp.get("body");

    // Check that both owners and writers is set to the princpal if empty
    assertEquals(authInfo.get("readers"), Lists.newArrayList("admin-team"));
    assertEquals(authInfo.get("owners"), Lists.newArrayList("anonymous"));
    assertEquals(authInfo.get("writers"), Lists.newArrayList("anonymous"));
    get.releaseConnection();


    Note note2 = ZeppelinServer.notebook.createNote(anonymous, EMPTY_STRING);
    // Set only writers
    jsonRequest = "{\"readers\":[],\"owners\":[]," +
            "\"writers\":[\"admin-team\"]}";
    put = httpPut("/notebook/" + note2.getId() + "/permissions/", jsonRequest);
    assertThat("test update method:", put, isAllowed());
    put.releaseConnection();

    get = httpGet("/notebook/" + note2.getId() + "/permissions/");
    assertThat(get, isAllowed());
    resp = gson.fromJson(get.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    authInfo = (Map<String, Set<String>>) resp.get("body");
    // Check that owners is set to the princpal if empty
    assertEquals(authInfo.get("owners"), Lists.newArrayList("anonymous"));
    assertEquals(authInfo.get("writers"), Lists.newArrayList("admin-team"));
    get.releaseConnection();


    // Test clear permissions
    jsonRequest = "{\"readers\":[],\"owners\":[],\"writers\":[]}";
    put = httpPut("/notebook/" + note2.getId() + "/permissions/", jsonRequest);
    put.releaseConnection();
    get = httpGet("/notebook/" + note2.getId() + "/permissions/");
    assertThat(get, isAllowed());
    resp = gson.fromJson(get.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    authInfo = (Map<String, Set<String>>) resp.get("body");

    assertEquals(authInfo.get("readers"), Lists.newArrayList());
    assertEquals(authInfo.get("writers"), Lists.newArrayList());
    assertEquals(authInfo.get("owners"), Lists.newArrayList());
    get.releaseConnection();
    //cleanup
    ZeppelinServer.notebook.removeNote(note1.getId(), anonymous);
    ZeppelinServer.notebook.removeNote(note2.getId(), anonymous);

  }

  @Test
  public void testGetNoteParagraphJobStatus() throws IOException, DuplicateNameException {
    Note note1 = ZeppelinServer.notebook.createNote(anonymous, EMPTY_STRING);
    note1.addParagraph();

    String paragraphId = note1.getLastParagraph().getId();

    GetMethod get = httpGet("/notebook/job/" + note1.getId() + "/" + paragraphId);
    assertThat(get, isAllowed());
    Map<String, Object> resp = gson.fromJson(get.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    Map<String, Set<String>> paragraphStatus = (Map<String, Set<String>>) resp.get("body");

    // Check id and status have proper value
    assertEquals(paragraphStatus.get("id"), paragraphId);
    assertEquals(paragraphStatus.get("status"), "READY");

    //cleanup
    ZeppelinServer.notebook.removeNote(note1.getId(), anonymous);

  }

  @Test
  public void testCloneNotebook() throws IOException, DuplicateNameException {
    Note note1 = ZeppelinServer.notebook.createNote(anonymous, EMPTY_STRING);
    PostMethod post = httpPost("/notebook/" + note1.getId(), "");
    LOG.info("testCloneNote response\n" + post.getResponseBodyAsString());
    assertThat(post, isCreated());
    Map<String, Object> resp = gson.fromJson(post.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    String clonedNoteId = (String) resp.get("body");
    post.releaseConnection();

    GetMethod get = httpGet("/notebook/" + clonedNoteId);
    assertThat(get, isAllowed());
    Map<String, Object> resp2 = gson.fromJson(get.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    Map<String, Object> resp2Body = (Map<String, Object>) resp2.get("body");

    assertEquals((String)resp2Body.get("name"), "Note " + clonedNoteId);
    get.releaseConnection();

    //cleanup
    ZeppelinServer.notebook.removeNote(note1.getId(), anonymous);
    ZeppelinServer.notebook.removeNote(clonedNoteId, anonymous);

  }

  @Test
  public void testClearAllParagraphOutput() throws IOException, DuplicateNameException {
    // Create note and set result explicitly
    Note note = ZeppelinServer.notebook.createNote(anonymous, EMPTY_STRING);
    Paragraph p1 = note.addParagraph();
    InterpreterResult result = new InterpreterResult(InterpreterResult.Code.SUCCESS, InterpreterResult.Type.TEXT, "result");
    p1.setResult(result);

    Paragraph p2 = note.addParagraph();
    p2.setReturn(result, new Throwable());

    // clear paragraph result
    PutMethod put = httpPut("/notebook/" + note.getId() + "/clear", "");
    LOG.info("test clear paragraph output response\n" + put.getResponseBodyAsString());
    assertThat(put, isAllowed());
    put.releaseConnection();

    // check if paragraph results are cleared
    GetMethod get = httpGet("/notebook/" + note.getId() + "/paragraph/" + p1.getId());
    assertThat(get, isAllowed());
    Map<String, Object> resp1 = gson.fromJson(get.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    Map<String, Object> resp1Body = (Map<String, Object>) resp1.get("body");
    assertNull(resp1Body.get("result"));

    get = httpGet("/notebook/" + note.getId() + "/paragraph/" + p2.getId());
    assertThat(get, isAllowed());
    Map<String, Object> resp2 = gson.fromJson(get.getResponseBodyAsString(), new TypeToken<Map<String, Object>>() {
    }.getType());
    Map<String, Object> resp2Body = (Map<String, Object>) resp2.get("body");
    assertNull(resp2Body.get("result"));
    get.releaseConnection();

    //cleanup
    ZeppelinServer.notebook.removeNote(note.getId(), anonymous);
  }
  
  @Test
  public void testCloneNotebookWithSameName() throws IOException, DuplicateNameException {
    String noteName = "Test_Note";
    String jsonRequest = "{\"name\":\"" + noteName + "\"}";
    Note note1 = ZeppelinServer.notebook.createNote(anonymous, noteName);
    PostMethod post = httpPost("/notebook/" + note1.getId(), jsonRequest);
    LOG.info("testCloneNotebookWithSameName response\n" + post.getResponseBodyAsString());
    assertThat(post, isNotAcceptable());

    //cleanup
    ZeppelinServer.notebook.removeNote(note1.getId(), anonymous);
  }
  
  @Test
  public void testCreateNotebookWithSameName() throws IOException, DuplicateNameException {
    String noteName = "Test_Note";
    Note note1 = ZeppelinServer.notebook.createNote(anonymous, noteName);
    String jsonRequest = "{\"name\":\"" + noteName + "\"}";
    PostMethod post = httpPost("/notebook/", jsonRequest);
    LOG.info("testCloneNotebookWithSameName response\n" + post.getResponseBodyAsString());
    assertThat(post, isNotAcceptable());
    post.releaseConnection();

    //cleanup
    ZeppelinServer.notebook.removeNote(note1.getId(), anonymous);
  }
}


