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

package org.apache.zeppelin.livy;

import org.apache.zeppelin.interpreter.*;
import org.apache.zeppelin.interpreter.thrift.InterpreterCompletion;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Livy Spark interpreter for Zeppelin.
 */
public class LivySparkInterpreter extends Interpreter {

  Logger LOGGER = LoggerFactory.getLogger(LivySparkInterpreter.class);
  private LivyOutputStream out;
  private LivySparkSessionMap lsmap;
  private LivyHelper livyHelper;
  private String type = "spark";

  public LivySparkInterpreter(Properties property) {
    super(property);
    lsmap = LivySparkSessionMap.getInstance();
    livyHelper = new LivyHelper(property, type);
    out = new LivyOutputStream();
  }

  @Override
  public void open() {
  }

  @Override
  public void close() {
    livyHelper.closeSession(lsmap.getSparkUserSessionMap());
  }

  @Override
  public InterpreterResult interpret(String line, InterpreterContext interpreterContext) {
    try {
      if (lsmap.getSparkUserSession(interpreterContext.getAuthenticationInfo().getUser()) == null) {
        try {
          lsmap.setSparkUserSessionMap(
              interpreterContext.getAuthenticationInfo().getUser(),
              livyHelper.createSession(
                  interpreterContext,
                  "spark")
          );
        } catch (Exception e) {
          LOGGER.error("Exception in LivySparkInterpreter while interpret ", e);
          return new InterpreterResult(InterpreterResult.Code.ERROR, e.getMessage());
        }
      }
      if (line == null || line.trim().length() == 0) {
        return new InterpreterResult(InterpreterResult.Code.SUCCESS, "");
      }

      return livyHelper.interpretInput(line, interpreterContext, out);
    } catch (Exception e) {
      LOGGER.error("Exception in LivySparkInterpreter while interpret ", e);
      return new InterpreterResult(InterpreterResult.Code.ERROR,
          InterpreterUtils.getMostRelevantMessage(e));
    }
  }

  @Override
  public void cancel(InterpreterContext context) {
    livyHelper.cancelHTTP(context.getParagraphId());
  }

  @Override
  public FormType getFormType() {
    return FormType.SIMPLE;
  }

  @Override
  public int getProgress(InterpreterContext context) {
    return 0;
  }

  @Override
  public Scheduler getScheduler() {
    return SchedulerFactory.singleton().createOrGetFIFOScheduler(
        LivySparkInterpreter.class.getName() + this.hashCode());
  }

  @Override
  public List<InterpreterCompletion> completion(String buf, int cursor) {
    return null;
  }

}
