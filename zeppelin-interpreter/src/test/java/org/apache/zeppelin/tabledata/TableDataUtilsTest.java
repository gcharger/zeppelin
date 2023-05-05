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

package org.apache.zeppelin.tabledata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TableDataUtilsTest {

  @Test
  void testColumn() {
    assertEquals("hello world", TableDataUtils.normalizeColumn("hello\tworld"));
    assertEquals("hello world", TableDataUtils.normalizeColumn("hello\nworld"));
    assertEquals("hello world", TableDataUtils.normalizeColumn("hello\r\nworld"));
    assertEquals("hello  world", TableDataUtils.normalizeColumn("hello\t\nworld"));

    assertEquals("null", TableDataUtils.normalizeColumn(null));
  }

  @Test
  void testColumns() {
    assertEquals(Arrays.asList("hello world", "hello world"),
            TableDataUtils.normalizeColumns(new Object[]{"hello\tworld", "hello\nworld"}));

    assertEquals(Arrays.asList("hello world", "null"),
            TableDataUtils.normalizeColumns(new String[]{"hello\tworld", null}));
  }
}
