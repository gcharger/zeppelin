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

package org.apache.zeppelin.sparql;

import org.apache.commons.lang3.StringUtils;

import org.apache.http.HttpStatus;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;


/**
 * Interpreter for SPARQL-Query via Apache Jena ARQ.
 */
public class SparqlInterpreter extends Interpreter {
  private static final Logger LOGGER = LoggerFactory.getLogger(SparqlInterpreter.class);

  public static final String SPARQL_SERVICE_ENDPOINT = "sparql.endpoint";
  public static final String SPARQL_REPLACE_URIS = "sparql.replaceURIs";
  public static final String SPARQL_REMOVE_DATATYPES = "sparql.removeDatatypes";

  public SparqlInterpreter(Properties properties) {
    super(properties);
  }

  @Override
  public void open() {
    LOGGER.info("Properties: {}", getProperties());
  }

  @Override
  public void close() {
  }

  @Override
  public InterpreterResult interpret(String queryString, InterpreterContext context) {
    final String serviceEndpoint = getProperty(SPARQL_SERVICE_ENDPOINT);
    final String replaceURIs = getProperty(SPARQL_REPLACE_URIS);
    final String removeDatatypes = getProperty(SPARQL_REMOVE_DATATYPES);

    LOGGER.info("SPARQL: Run Query '" + queryString + "' against " + serviceEndpoint);

    if (StringUtils.isEmpty(queryString) || StringUtils.isEmpty(queryString.trim())) {
      return new InterpreterResult(InterpreterResult.Code.SUCCESS);
    }

    Query query = null;
    PrefixMapping prefixMapping = null;
    try {
      query = QueryFactory.create(queryString);
      prefixMapping = query.getPrologue().getPrefixMapping();
    } catch (QueryParseException e) {
      LOGGER.error(e.toString());
      return new InterpreterResult(
        InterpreterResult.Code.ERROR,
        "Error: " + e.getMessage());
    }

    try (QueryExecution qe =
      QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
      // execute query and get Results
      ResultSet results = qe.execSelect();

      // transform ResultSet to TSV-String
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ResultSetFormatter.outputAsTSV(outputStream, results);
      String tsv = new String(outputStream.toByteArray());

      if (replaceURIs != null && replaceURIs.equals("true")) {
        LOGGER.info("SPARQL: Replacing URIs");
        tsv = replaceURIs(tsv, prefixMapping);
      }

      if (removeDatatypes != null && removeDatatypes.equals("true")) {
        LOGGER.info("SPARQL: Removing datatypes");
        tsv = removeDatatypes(tsv);
      }

      return new InterpreterResult(
              InterpreterResult.Code.SUCCESS,
              InterpreterResult.Type.TABLE,
              tsv);
    } catch (QueryExceptionHTTP e) {
      LOGGER.error(e.toString());
      int responseCode = e.getResponseCode();

      if (responseCode == HttpStatus.SC_UNAUTHORIZED) {
        return new InterpreterResult(
          InterpreterResult.Code.ERROR,
            "Unauthorized.");
      } else if (responseCode == HttpStatus.SC_NOT_FOUND) {
        return new InterpreterResult(
          InterpreterResult.Code.ERROR,
            "Endpoint not found, please check endpoint in the configuration.");
      } else {
        return new InterpreterResult(
          InterpreterResult.Code.ERROR,
            "Error: " + e.getMessage());
      }
    }
  }

  @Override
  public void cancel(InterpreterContext context) {
  }

  @Override
  public FormType getFormType() {
    return FormType.SIMPLE;
  }

  @Override
  public int getProgress(InterpreterContext context) {
    return 0;
  }

  private String replaceURIs(String tsv, PrefixMapping prefixMapping) {
    Map<String, String> pmap = prefixMapping.getNsPrefixMap();
    for (Map.Entry<String, String> entry : pmap.entrySet()) {
      tsv = tsv.replaceAll(entry.getValue(), entry.getKey() + ":");
    }
    return tsv;
  }

  private String removeDatatypes(String tsv) {
    // capture group: "($1)"^^<.+?>
    return tsv.replaceAll("\"(.+?)\"\\^\\^\\<.+?\\>", "$1");
  }
}
