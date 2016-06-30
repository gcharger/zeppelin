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

package org.apache.zeppelin.shell;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.InterpreterResult.Code;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShellInterpreterTest {
  
  private ShellInterpreter shell;
  
  @Before
  public void setUp() throws Exception {
    shell = new ShellInterpreter(new Properties());
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    shell.open();
    InterpreterContext context = new InterpreterContext("", "1", "", "", null, null, null, null, null, null, null);
    InterpreterResult result = new InterpreterResult(Code.ERROR);
    if (System.getProperty("os.name").startsWith("Windows")) {
      result = shell.interpret("dir", context);
    } else {
      result = shell.interpret("ls", context);
    }
    // System.out.println(result.message());
    assertEquals(InterpreterResult.Code.SUCCESS, result.code());
  }
  
}
