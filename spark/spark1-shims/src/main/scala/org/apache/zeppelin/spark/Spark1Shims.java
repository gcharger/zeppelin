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

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkContext;
import org.apache.spark.scheduler.SparkListenerJobStart;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.catalyst.expressions.GenericRow;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.ui.jobs.JobProgressListener;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.ResultMessages;
import org.apache.zeppelin.interpreter.SingleRowInterpreterResult;
import org.apache.zeppelin.tabledata.TableDataUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Spark1Shims extends SparkShims {

  private SparkContext sc;

  public Spark1Shims(Properties properties, Object entryPoint) {
    super(properties);
    this.sc = (SparkContext) entryPoint;
  }

  public void setupSparkListener(final String master,
                                 final String sparkWebUrl,
                                 final InterpreterContext context) {
    SparkContext sc = SparkContext.getOrCreate();
    sc.addSparkListener(new JobProgressListener(sc.getConf()) {
      @Override
      public void onJobStart(SparkListenerJobStart jobStart) {
        if (sc.getConf().getBoolean("spark.ui.enabled", true) &&
            !Boolean.parseBoolean(properties.getProperty("zeppelin.spark.ui.hidden", "false"))) {
          buildSparkJobUrl(master, sparkWebUrl, jobStart.jobId(), jobStart.properties(), context);
        }
      }
    });
  }

  @Override
  public String showDataFrame(Object obj, int maxResult, InterpreterContext context) {
    if (obj instanceof DataFrame) {
      DataFrame df = (DataFrame) obj;
      String[] columns = df.columns();
      // DDL will empty DataFrame
      if (columns.length == 0) {
        return "";
      }

      // fetch maxResult+1 rows so that we can check whether it is larger than zeppelin.spark.maxResult
      List<Row> rows = df.takeAsList(maxResult + 1);
      String template = context.getLocalProperties().get("template");
      if (!StringUtils.isBlank(template)) {
        if (rows.size() >= 1) {
          return new SingleRowInterpreterResult(sparkRowToList(rows.get(0)), template, context).toHtml();
        } else {
          return "";
        }
      }

      StringBuilder msg = new StringBuilder();
      msg.append("\n%table ");
      msg.append(StringUtils.join(TableDataUtils.normalizeColumns(columns), "\t"));
      msg.append("\n");
      boolean isLargerThanMaxResult = rows.size() > maxResult;
      if (isLargerThanMaxResult) {
        rows = rows.subList(0, maxResult);
      }
      for (Row row : rows) {
        for (int i = 0; i < row.size(); ++i) {
          msg.append(TableDataUtils.normalizeColumn(row.get(i)));
          if (i != row.size() - 1) {
            msg.append("\t");
          }
        }
        msg.append("\n");
      }

      if (isLargerThanMaxResult) {
        msg.append("\n");
        msg.append(ResultMessages.getExceedsLimitRowsMessage(maxResult, "zeppelin.spark.maxResult"));
      }
      // append %text at the end, otherwise the following output will be put in table as well.
      msg.append("\n%text ");
      return msg.toString();
    } else {
      return obj.toString();
    }
  }

  private List sparkRowToList(Row row) {
    List list = new ArrayList();
    for (int i = 0; i< row.size(); i++) {
      list.add(row.get(i));
    }
    return list;
  }

  @Override
  public DataFrame getAsDataFrame(String value) {
    String[] lines = value.split("\\n");
    String head = lines[0];
    String[] columns = head.split("\t");
    StructType schema = new StructType();
    for (String column : columns) {
      schema = schema.add(column, "String");
    }

    List<Row> rows = new ArrayList<>();
    for (int i = 1; i < lines.length; ++i) {
      String[] tokens = lines[i].split("\t");
      Row row = new GenericRow(tokens);
      rows.add(row);
    }
    return SQLContext.getOrCreate(sc)
            .createDataFrame(rows, schema);
  }
}
