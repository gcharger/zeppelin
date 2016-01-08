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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterPropertyBuilder;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.InterpreterResult.Code;
import org.apache.zeppelin.scheduler.Job;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shell interpreter for Zeppelin.
 *
 * @author Leemoonsoo
 * @author anthonycorbacho
 *
 */
public class ShellInterpreter extends Interpreter {
  Logger logger = LoggerFactory.getLogger(ShellInterpreter.class);
  private static final String WATCHDOG_KEY = "watchdog";
  int commandTimeOut;

  static {
    Interpreter.register(
        "sh",
        "sh",
        ShellInterpreter.class.getName(),
        new InterpreterPropertyBuilder()
            .add("shell.commandtimeout", "60000", "Timeout period for shell command").build());
  }

  public ShellInterpreter(Properties property) {
    super(property);
    commandTimeOut = parseIntProperty("shell.commandtimeout", 60000);
  }

  private int parseIntProperty(String prop, int defaultvalue) {
    String propValue = getProperty(prop);
    try {
      defaultvalue = Integer.parseInt(propValue);
    } catch (Exception e) {
      logger.info("Unable to parse property" + prop + ". Using defaults:" + defaultvalue);
    }
    return defaultvalue;
  }
 
  @Override
  public void open() {}

  @Override
  public void close() {}


  @Override
  public InterpreterResult interpret(String cmd, InterpreterContext contextInterpreter) {
    logger.info("Run shell command '" + cmd + "'");
    CommandLine cmdLine = CommandLine.parse("bash");
    cmdLine.addArgument("-c", false);
    cmdLine.addArgument(cmd, false);
    DefaultExecutor executor = new DefaultExecutor();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
    executor.setStreamHandler(new PumpStreamHandler(outputStream, errorStream));

    ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(commandTimeOut);
    Job runningJob = getRunningJob(contextInterpreter.getParagraphId());
    Map<String, Object> info = runningJob.info();
    info.put(WATCHDOG_KEY, executeWatchdog);
    
    executor.setWatchdog(executeWatchdog);
    try {
      executor.execute(cmdLine);
      return new InterpreterResult(InterpreterResult.Code.SUCCESS, outputStream.toString());
    } catch (ExecuteException e) {
      int exitValue = e.getExitValue();
      logger.error("Can not run " + cmd, e);
      Code code = Code.ERROR;
      String msg = errorStream.toString();
      if (exitValue == 143) {
        code = Code.INCOMPLETE;
        msg = "Paragraph received a SIGTERM";
      }
      return new InterpreterResult(code, msg);
    } catch (IOException e) {
      logger.error("Can not run " + cmd, e);
      return new InterpreterResult(Code.ERROR, e.getMessage());
    }
  }

  private Job getRunningJob(String paragraphId) {
    Job foundJob = null;
    Collection<Job> jobsRunning = getScheduler().getJobsRunning();
    for (Job job : jobsRunning) {
      if (job.getId().equals(paragraphId)) {
        foundJob = job;
      }
    }
    return foundJob;
  }

  @Override
  public void cancel(InterpreterContext context) {
    Job runningJob = getRunningJob(context.getParagraphId());
    if (runningJob != null) {
      Map<String, Object> info = runningJob.info();
      Object object = info.get(WATCHDOG_KEY);
      if (object != null) {
        ExecuteWatchdog watchdog = (ExecuteWatchdog) object;
        watchdog.destroyProcess();
      }
    }
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
    return SchedulerFactory.singleton().createOrGetParallelScheduler(
        ShellInterpreter.class.getName() + this.hashCode(), 10);
  }

  @Override
  public List<String> completion(String buf, int cursor) {
    return null;
  }

}
