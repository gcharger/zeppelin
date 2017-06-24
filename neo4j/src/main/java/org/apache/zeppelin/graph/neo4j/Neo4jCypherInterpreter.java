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

package org.apache.zeppelin.graph.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.zeppelin.graph.neo4j.utils.Neo4jConversionUtils;
import org.apache.zeppelin.interpreter.Interpreter;
import org.apache.zeppelin.interpreter.InterpreterContext;
import org.apache.zeppelin.interpreter.InterpreterResult;
import org.apache.zeppelin.interpreter.InterpreterResult.Code;
import org.apache.zeppelin.interpreter.graph.GraphResult;
import org.apache.zeppelin.resource.Resource;
import org.apache.zeppelin.resource.ResourcePool;
import org.apache.zeppelin.scheduler.Scheduler;
import org.apache.zeppelin.scheduler.SchedulerFactory;
import org.neo4j.driver.internal.util.Iterables;
import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;
import org.neo4j.driver.v1.types.TypeSystem;
import org.neo4j.driver.v1.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Neo4j interpreter for Zeppelin.
 */
public class Neo4jCypherInterpreter extends Interpreter {
  static final Logger LOGGER = LoggerFactory.getLogger(Neo4jCypherInterpreter.class);

  public static final String NEO4J_SERVER_URL = "neo4j.url";
  public static final String NEO4J_AUTH_TYPE = "neo4j.auth.type";
  public static final String NEO4J_AUTH_USER = "neo4j.auth.user";
  public static final String NEO4J_AUTH_PASSWORD = "neo4j.auth.password";
  public static final String NEO4J_MAX_CONCURRENCY = "neo4j.max.concurrency";

  private static final String TABLE = "%table";
  public static final String NEW_LINE = "\n";
  public static final String TAB = "\t";

  private static final Pattern PROPERTY_PATTERN = Pattern.compile("\\{\\w+\\}");
  private static final String REPLACE_CURLY_BRACKETS = "\\{|\\}";

  private static final Pattern $_PATTERN = Pattern.compile("\\$\\w+\\}");
  private static final String REPLACE_$ = "\\$";

  private static final String MAP_KEY_TEMPLATE = "%s.%s";
  private static final String ARAY_KEY_TEMPLATE = "%s[%d]";

  /**
   * 
   * Enum type for the AuthToken 
   *
   */
  public enum Neo4jAuthType {NONE, BASIC}

  private Driver driver = null;

  private Map<String, String> labels;

  private Set<String> types;

  public Neo4jCypherInterpreter(Properties properties) {
    super(properties);
  }

  private Driver getDriver() {
    if (driver == null) {
      Config config = Config.build()
            .withMaxIdleSessions(Integer.parseInt(getProperty(NEO4J_MAX_CONCURRENCY)))
            .toConfig();
      String authType = getProperty(NEO4J_AUTH_TYPE);
      AuthToken authToken = null;
      switch (Neo4jAuthType.valueOf(authType.toUpperCase())) {
        case BASIC:
          authToken = AuthTokens.basic(getProperty(NEO4J_AUTH_USER),
            getProperty(NEO4J_AUTH_PASSWORD));
          break;
        case NONE:
          authToken = AuthTokens.none();
          break;
        default:
          throw new RuntimeException("Neo4j authentication type not supported");
      }
      driver = GraphDatabase.driver(getProperty(NEO4J_SERVER_URL), authToken, config);
    }
    return driver;
  }

  @Override
  public void open() {
    getDriver();
  }

  @Override
  public void close() {
    getDriver().close();
  }

  public Map<String, String> getLabels(boolean refresh) {
    if (labels == null || refresh) {
      Map<String, String> old = labels == null ?
          new LinkedHashMap<String, String>() : new LinkedHashMap<>(labels);
      labels = new LinkedHashMap<>();
      try (Session session = getDriver().session()) {
        StatementResult result = session.run("CALL db.labels()");
        Set<String> colors = new HashSet<>();
        while (result.hasNext()) {
          Record record = result.next();
          String label = record.get("label").asString();
          String color = old.get(label);
          while (color == null || colors.contains(color)) {
            color = Neo4jConversionUtils.getRandomLabelColor();
          }
          colors.add(color);
          labels.put(label, color);
        }
      }
    }
    return labels;
  }

  private Set<String> getTypes(boolean refresh) {
    if (types == null || refresh) {
      types = new HashSet<>();
      try (Session session = getDriver().session()) {
        StatementResult result = session.run("CALL db.relationshipTypes()");
        while (result.hasNext()) {
          Record record = result.next();
          types.add(record.get("relationshipType").asString());
        }
      }
    }
    return types;
  }

  @Override
  public InterpreterResult interpret(String cypherQuery, InterpreterContext interpreterContext) {
    logger.info("Opening session");
    if (StringUtils.isEmpty(cypherQuery)) {
      return new InterpreterResult(Code.ERROR, "Cypher query is Empty");
    }
    try (Session session = getDriver().session()){
      StatementResult result = execute(session, cypherQuery, interpreterContext);
      Set<Node> nodes = new HashSet<>();
      Set<Relationship> relationships = new HashSet<>();
      List<String> columns = new ArrayList<>();
      List<List<String>> lines = new ArrayList<List<String>>();
      while (result.hasNext()) {
        Record record = result.next();
        List<Pair<String, Value>> fields = record.fields();
        List<String> line = new ArrayList<>();
        for (Pair<String, Value> field : fields) {
          if (field.value().hasType(session.typeSystem().NODE())) {
            nodes.add(field.value().asNode());
          } else if (field.value().hasType(session.typeSystem().RELATIONSHIP())) {
            relationships.add(field.value().asRelationship());
          } else if (field.value().hasType(session.typeSystem().PATH())) {
            nodes.addAll(Iterables.asList(field.value().asPath().nodes()));
            relationships.addAll(Iterables.asList(field.value().asPath().relationships()));
          } else if (field.value().hasType(session.typeSystem().LIST())) {
            List<Object> list = field.value().asList();
            for (Object elem : list) {
              List<String> lineList = new ArrayList<>();
              setTabularResult(field.key(), elem, columns, lineList, session.typeSystem());
              if (!lineList.isEmpty()) {
                lines.add(lineList);
              }
            }
          } else {
            setTabularResult(field.key(), field.value(), columns, line, session.typeSystem());
          }
        }
        if (!line.isEmpty()) {
          lines.add(line);
        }
      }
      if (!nodes.isEmpty()) {
        return renderGraph(nodes, relationships);
      } else {
        return renderTable(columns, lines);
      }
    } catch (Exception e) {
      logger.error("Exception while interpreting cypher query", e);
      return new InterpreterResult(Code.ERROR, e.getMessage());
    }
  }

  private void setTabularResult(String key, Object obj, List<String> columns, List<String> line,
      TypeSystem typeSystem) {
    if (obj instanceof Value) {
      Value value = (Value) obj;
      if (value.hasType(typeSystem.MAP())) {
        Map<String, Object> map = value.asMap();
        for (Entry<String, Object> entry : map.entrySet()) {
          setTabularResult(String.format(MAP_KEY_TEMPLATE, key, entry.getKey()), entry.getValue(),
                columns, line, typeSystem);
        }
      } else if (value.hasType(typeSystem.LIST())) {
        List<Object> list = value.asList();
        for (int i = 0; i < list.size(); i++) {
          Object elem = list.get(i);
          setTabularResult(String.format(ARAY_KEY_TEMPLATE, key, i), elem, columns,
                line, typeSystem);
        }
      } else {
        addLine(key, columns, line, value);
      }
    } else if (obj instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, Object> map = (Map<String, Object>) obj;
      for (Entry<String, Object> entry : map.entrySet()) {
        setTabularResult(String.format(MAP_KEY_TEMPLATE, key, entry.getKey()), entry.getValue(),
                columns, line, typeSystem);
      }
    } else if (obj instanceof List) {
      @SuppressWarnings("unchecked")
      List<Object> list = (List<Object>) obj;
      for (int i = 0; i < list.size(); i++) {
        Object elem = list.get(i);
        setTabularResult(String.format(ARAY_KEY_TEMPLATE, key, i), elem, columns, line, typeSystem);
      }
    } else {
      addLine(key, columns, line, obj);
    }
  }

  private void addLine(String key, List<String> columns, List<String> line, Object value) {
    if (!columns.contains(key)) {
      columns.add(key);
    }
    int position = columns.indexOf(key);
    if (line.size() < columns.size()) {
      for (int i = line.size(); i < columns.size(); i++) {
        line.add(null);
      }
    }
    line.set(position, value == null ? null : value.toString());
  }

  private StatementResult execute(Session session, String cypherQuery,
      InterpreterContext interpreterContext) {
    Map<String, Object> params = new HashMap<>();
    ResourcePool resourcePool = interpreterContext.getResourcePool();
    Set<String> keys = extractParams(cypherQuery, PROPERTY_PATTERN, REPLACE_CURLY_BRACKETS);
    keys.addAll(extractParams(cypherQuery, $_PATTERN, REPLACE_$));
    for (String key : keys) {
      Resource resource = resourcePool.get(key);
      if (resource != null) {
        params.put(key, resource.get());
      }
    }
    logger.info("Executing cypher query {} with params {}", cypherQuery, params);
    return params.isEmpty() ? session.run(cypherQuery) : session.run(cypherQuery, params);
  }

  private Set<String> extractParams(String cypherQuery, Pattern pattern, String replaceChar) {
    Matcher matcher = pattern.matcher(cypherQuery);
    Set<String> keys = new HashSet<>();
    while (matcher.find()) {
      keys.add(matcher.group().replaceAll(replaceChar, StringUtils.EMPTY));
    }
    return keys;
  }

  private InterpreterResult renderTable(List<String> cols, List<List<String>> lines) {
    logger.info("Executing renderTable method");
    StringBuilder msg = new StringBuilder(TABLE);
    msg.append(NEW_LINE);
    msg.append(StringUtils.join(cols, TAB));
    msg.append(NEW_LINE);
    for (List<String> line : lines) {
      if (line.size() < cols.size()) {
        for (int i = line.size(); i < cols.size(); i++) {
          line.add(null);
        }
      }
      msg.append(StringUtils.join(line, TAB));
      msg.append(NEW_LINE);
    }
    return new InterpreterResult(Code.SUCCESS, msg.toString());
  }

  private InterpreterResult renderGraph(Set<Node> nodes,
      Set<Relationship> relationships) {
    logger.info("Executing renderGraph method");
    List<org.apache.zeppelin.tabledata.Node> nodesList = new ArrayList<>();
    List<org.apache.zeppelin.tabledata.Relationship> relsList = new ArrayList<>();
    for (Relationship rel : relationships) {
      relsList.add(Neo4jConversionUtils.toZeppelinRelationship(rel));
    }
    Map<String, String> labels = getLabels(true);
    for (Node node : nodes) {
      nodesList.add(Neo4jConversionUtils.toZeppelinNode(node, labels));
    }
    return new GraphResult(Code.SUCCESS,
        new GraphResult.Graph(nodesList, relsList, labels, getTypes(true), true));
  }

  @Override
  public Scheduler getScheduler() {
    return SchedulerFactory.singleton()
        .createOrGetParallelScheduler(Neo4jCypherInterpreter.class.getName() + this.hashCode(),
            Integer.parseInt(getProperty(NEO4J_MAX_CONCURRENCY)));
  }

  @Override
  public int getProgress(InterpreterContext context) {
    return 0;
  }

  @Override
  public FormType getFormType() {
    return FormType.SIMPLE;
  }

  @Override
  public void cancel(InterpreterContext context) {
  }

}
