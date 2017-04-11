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

package org.apache.zeppelin.spark;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterException;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.InterpreterResult.Code;
import org.apache.zeppelin.interpreter.LazyOpenInterpreter;
import org.apache.zeppelin.interpreter.WrappedInterpreter;
import org.apache.zeppelin.interpreter.thrift.InterpreterCompletion;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spark SQL interpreter for Zeppelin.
 */
public class SparkSqlInterpreter extends Interpreter {
  private Logger logger = LoggerFactory.getLogger(SparkSqlInterpreter.class);

  public static final String MAX_RESULTS = "zeppelin.spark.maxResult";

  AtomicInteger num = new AtomicInteger(0);

  private int maxResult;

  public SparkSqlInterpreter(Properties property) {
    super(property);
  }

  @Override
  public void open() {
    this.maxResult = Integer.parseInt(getProperty(MAX_RESULTS));
  }

  private SparkInterpreter getSparkInterpreter() {
    LazyOpenInterpreter lazy = null;
    SparkInterpreter spark = null;
    Interpreter p = getInterpreterInTheSameSessionByClassName(SparkInterpreter.class.getName());

    while (p instanceof WrappedInterpreter) {
      if (p instanceof LazyOpenInterpreter) {
        lazy = (LazyOpenInterpreter) p;
      }
      p = ((WrappedInterpreter) p).getInnerInterpreter();
    }
    spark = (SparkInterpreter) p;

    if (lazy != null) {
      lazy.open();
    }
    return spark;
  }

  public boolean concurrentSQL() {
    return Boolean.parseBoolean(getProperty("zeppelin.spark.concurrentSQL"));
  }

  @Override
  public void close() {}

  @Override
  public InterpreterResult interpret(String st, InterpreterContext context) {
    SQLContext sqlc = null;
    SparkInterpreter sparkInterpreter = getSparkInterpreter();

    if (sparkInterpreter.isUnsupportedSparkVersion()) {
      return new InterpreterResult(Code.ERROR, "Spark "
          + sparkInterpreter.getSparkVersion().toString() + " is not supported");
    }

    sparkInterpreter.populateSparkWebUrl(context);
    sqlc = getSparkInterpreter().getSQLContext();
    SparkContext sc = sqlc.sparkContext();
    if (concurrentSQL()) {
      sc.setLocalProperty("spark.scheduler.pool", "fair");
    } else {
      sc.setLocalProperty("spark.scheduler.pool", null);
    }

    sc.setJobGroup(Utils.buildJobGroupId(context), "Zeppelin", false);
    Object rdd = null;
    try {
      // method signature of sqlc.sql() is changed
      // from  def sql(sqlText: String): SchemaRDD (1.2 and prior)
      // to    def sql(sqlText: String): DataFrame (1.3 and later).
      // Therefore need to use reflection to keep binary compatibility for all spark versions.
      Method sqlMethod = sqlc.getClass().getMethod("sql", String.class);
      for (String statement : splitSqlScript(st)) {
          if (StringUtils.isNotBlank(statement)) {
              rdd = sqlMethod.invoke(sqlc, statement);
          }
      }
    } catch (InvocationTargetException ite) {
      if (Boolean.parseBoolean(getProperty("zeppelin.spark.sql.stacktrace"))) {
        throw new InterpreterException(ite);
      }
      logger.error("Invocation target exception", ite);
      String msg = ite.getTargetException().getMessage()
              + "\nset zeppelin.spark.sql.stacktrace = true to see full stacktrace";
      return new InterpreterResult(Code.ERROR, msg);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException
        | IllegalArgumentException e) {
      throw new InterpreterException(e);
    }

    String msg = ZeppelinContext.showDF(sc, context, rdd, maxResult);
    sc.clearJobGroup();
    return new InterpreterResult(Code.SUCCESS, msg);
  }

    //split sql script to single statement list
    public static List<String> splitSqlScript(String script) {
        List<String> queries = new LinkedList<String>();
        StringBuilder query = new StringBuilder();
        List<Character> quoteList = Arrays.asList('\'', '"', '`');
        Stack<String> operatorStack = new Stack<String>();
        char lastCharacter = ' ';
        for (char character : script.toCharArray()) {
            if (';' == character && lastCharacter != '\\' && operatorStack.isEmpty()) {
                if (query.length() > 0) {
                    queries.add(query.toString());
                    query.setLength(0);
                }
            } else {
                query.append(character);
                if (operatorStack.isEmpty()) {
                    if ('-' == character && '-' == lastCharacter) {
                        operatorStack.push("--");
                    } else if (lastCharacter == '/' && '*' == character) {
                        operatorStack.push("/*");
                    } else if (quoteList.contains(character) && lastCharacter != '\\') {
                        operatorStack.push(String.valueOf(character));
                    }
                } else {
                    if ('\n' == character && "--".equals(operatorStack.peek())) {
                        operatorStack.pop();
                    } else if (lastCharacter == '*' && character == '/'
                            && operatorStack.peek().equals("/*")) {
                        operatorStack.pop();
                    } else if (quoteList.contains(character)
                            && operatorStack.peek().equals(String.valueOf(character))) {
                        operatorStack.pop();
                    }
                }
            }
            lastCharacter = character;
        }
        if (query.length() > 0) {
            queries.add(query.toString());
        }
        return queries;
    }

    @Override
    public void cancel(InterpreterContext context) {
        SQLContext sqlc = getSparkInterpreter().getSQLContext();
        SparkContext sc = sqlc.sparkContext();

    sc.cancelJobGroup(Utils.buildJobGroupId(context));
  }

  @Override
  public FormType getFormType() {
    return FormType.SIMPLE;
  }


  @Override
  public int getProgress(InterpreterContext context) {
    SparkInterpreter sparkInterpreter = getSparkInterpreter();
    return sparkInterpreter.getProgress(context);
  }

  @Override
  public Scheduler getScheduler() {
    if (concurrentSQL()) {
      int maxConcurrency = 10;
      return SchedulerFactory.singleton().createOrGetParallelScheduler(
          SparkSqlInterpreter.class.getName() + this.hashCode(), maxConcurrency);
    } else {
      // getSparkInterpreter() calls open() inside.
      // That means if SparkInterpreter is not opened, it'll wait until SparkInterpreter open.
      // In this moment UI displays 'READY' or 'FINISHED' instead of 'PENDING' or 'RUNNING'.
      // It's because of scheduler is not created yet, and scheduler is created by this function.
      // Therefore, we can still use getSparkInterpreter() here, but it's better and safe
      // to getSparkInterpreter without opening it.

      Interpreter intp =
          getInterpreterInTheSameSessionByClassName(SparkInterpreter.class.getName());
      if (intp != null) {
        return intp.getScheduler();
      } else {
        return null;
      }
    }
  }

  @Override
  public List<InterpreterCompletion> completion(String buf, int cursor) {
    return null;
  }
}
