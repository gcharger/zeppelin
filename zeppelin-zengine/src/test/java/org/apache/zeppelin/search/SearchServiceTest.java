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
package org.apache.zeppelin.search;

import static com.google.common.truth.Truth.assertThat;
import static org.apache.zeppelin.search.SearchService.formatId;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.zeppelin.notebook.Note;
import org.apache.zeppelin.notebook.Paragraph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchServiceTest {

  SearchService notebookIndex;

  @Before
  public void startUp() {
    notebookIndex = new SearchService();
  }

  @After
  public void shutDown() {
    notebookIndex.close();
  }

  @Test public void canIndexNotebook() {
    //give
    Note note1 = newNoteWithParapgraph("Notebook1", "test");
    Note note2 = newNoteWithParapgraph("Notebook2", "not test");
    List<Note> notebook = Arrays.asList(note1, note2);

    //when
    notebookIndex.addIndexDocs(notebook);
  }

  @Test public void canIndexAndQuery() {
    //given
    Note note1 = newNoteWithParapgraph("Notebook1", "test");
    Note note2 = newNoteWithParapgraphs("Notebook2", "not test", "not test at all");
    notebookIndex.addIndexDocs(Arrays.asList(note1, note2));

    //when
    List<Map<String, String>> results = notebookIndex.query("all");

    //then
    assertThat(results).isNotEmpty();
    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get(0))
      .containsEntry("id", formatId(note2.getId(), note2.getLastParagraph()));
  }

  @Test public void canIndexAndQueryByNotebookName() {
    //given
    Note note1 = newNoteWithParapgraph("Notebook1", "test");
    Note note2 = newNoteWithParapgraphs("Notebook2", "not test", "not test at all");
    notebookIndex.addIndexDocs(Arrays.asList(note1, note2));

    //when
    List<Map<String, String>> results = notebookIndex.query("Notebook1");

    //then
    assertThat(results).isNotEmpty();
    assertThat(results.size()).isEqualTo(1);
    assertThat(results.get(0)).containsEntry("id", note1.getId());
  }


  @Test //(expected=IllegalStateException.class)
  public void canNotSearchBeforeIndexing() {
    //given NO notebookIndex.index() was called
    //when
    List<Map<String, String>> result = notebookIndex.query("anything");
    //then
    assertThat(result).isEmpty();
    //assert logs were printed
    //"ERROR org.apache.zeppelin.search.SearchService:97 - Failed to open index dir RAMDirectory"
  }

  @Test public void canIndexAndReIndex() throws IOException {
    //given
    Note note1 = newNoteWithParapgraph("Notebook1", "test");
    Note note2 = newNoteWithParapgraphs("Notebook2", "not test", "not test at all");
    notebookIndex.addIndexDocs(Arrays.asList(note1, note2));

    //when
    Paragraph p2 = note2.getLastParagraph();
    p2.setText("test indeed");
    notebookIndex.updateIndexDoc(note2, p2);

    //then
    List<Map<String, String>> results = notebookIndex.query("all");
    assertThat(results).isEmpty();

    results = notebookIndex.query("indeed");
    assertThat(results).isNotEmpty();
  }

  @Test public void canDeleteFromIndex() throws IOException {
    //given
    Note note1 = newNoteWithParapgraph("Notebook1", "test");
    Note note2 = newNoteWithParapgraphs("Notebook2", "not test", "not test at all");
    notebookIndex.addIndexDocs(Arrays.asList(note1, note2));
    assertThat(resultForQuery("Notebook2")).isNotEmpty();

    //when
    notebookIndex.deleteIndexDocs(note2);

    //then
    assertThat(notebookIndex.query("all")).isEmpty();
    assertThat(resultForQuery("Notebook2")).isEmpty();

    List<Map<String, String>> results = resultForQuery("test");
    assertThat(results).isNotEmpty();
    assertThat(results.size()).isEqualTo(1);
  }

  private List<Map<String, String>> resultForQuery(String q) {
    return notebookIndex.query(q);
  }

  /**
   * Creates a new Note \w given name,
   * adds a new paragraph \w given text
   *
   * @param noteName name of the note
   * @param parText text of the paragraph
   * @return Note
   */
  private Note newNoteWithParapgraph(String noteName, String parText) {
    Note note1 = newNote(noteName);
    addParagraphWithText(note1, parText);
    return note1;
  }

  /**
   * Creates a new Note \w given name,
   * adds N paragraphs \w given texts
   */
  private Note newNoteWithParapgraphs(String noteName, String... parTexts) {
    Note note1 = newNote(noteName);
    for (String parText : parTexts) {
      addParagraphWithText(note1, parText);
    }
    return note1;
  }

  private Paragraph addParagraphWithText(Note note, String text) {
    Paragraph p = note.addParagraph();
    p.setText(text);
    return p;
  }

  private Note newNote(String name) {
    Note note = new Note(null, null, null);
    note.setName(name);
    return note;
  }

}
