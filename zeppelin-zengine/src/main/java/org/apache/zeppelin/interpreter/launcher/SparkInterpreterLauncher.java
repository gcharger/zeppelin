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

package org.apache.zeppelin.interpreter.launcher;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.lang3.StringUtils;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.interpreter.recovery.RecoveryStorage;
import org.apache.zeppelin.interpreter.remote.RemoteInterpreterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Spark specific launcher.
 */
public class SparkInterpreterLauncher extends ShellScriptLauncher {

  private static final Logger LOGGER = LoggerFactory.getLogger(SparkInterpreterLauncher.class);

  public SparkInterpreterLauncher(ZeppelinConfiguration zConf, RecoveryStorage recoveryStorage) {
    super(zConf, recoveryStorage);
  }

  @Override
  protected Map<String, String> buildEnvFromProperties(InterpreterLaunchContext context) {
    Map<String, String> env = new HashMap<String, String>();
    Properties sparkProperties = new Properties();
    String sparkMaster = getSparkMaster(properties);
    for (String key : properties.stringPropertyNames()) {
      if (RemoteInterpreterUtils.isEnvString(key)) {
        env.put(key, properties.getProperty(key));
      }
      if (isSparkConf(key, properties.getProperty(key))) {
        sparkProperties.setProperty(key, toShellFormat(properties.getProperty(key)));
      }
    }

    setupPropertiesForPySpark(sparkProperties);
    setupPropertiesForSparkR(sparkProperties);
    if (isYarnMode() && getDeployMode().equals("cluster")) {
      env.put("ZEPPELIN_SPARK_YARN_CLUSTER", "true");
    }

    StringBuilder sparkConfBuilder = new StringBuilder();
    if (sparkMaster != null) {
      sparkConfBuilder.append(" --master " + sparkMaster);
    }
    if (isYarnMode() && getDeployMode().equals("cluster")) {
      sparkConfBuilder.append(" --files " + zConf.getConfDir() + "/log4j_yarn_cluster.properties");
    }
    String useProxyUserEnv = System.getenv("ZEPPELIN_IMPERSONATE_SPARK_PROXY_USER");
    if (context.getOption().isUserImpersonate() && (StringUtils.isBlank(useProxyUserEnv) ||
        !useProxyUserEnv.equals("false"))) {
      sparkConfBuilder.append(" --proxy-user " + context.getUserName());
      sparkProperties.remove("spark.yarn.principal");
      sparkProperties.remove("spark.yarn.keytab");
    }
    for (String name : sparkProperties.stringPropertyNames()) {
      sparkConfBuilder.append(" --conf " + name + "=" + sparkProperties.getProperty(name));
    }

    env.put("ZEPPELIN_SPARK_CONF", sparkConfBuilder.toString());

    // set these env in the order of
    // 1. interpreter-setting
    // 2. zeppelin-env.sh
    // It is encouraged to set env in interpreter setting, but just for backward compatability,
    // we also fallback to zeppelin-env.sh if it is not specified in interpreter setting.
    for (String envName : new String[]{"SPARK_HOME", "SPARK_CONF_DIR", "HADOOP_CONF_DIR"})  {
      String envValue = getEnv(envName);
      if (envValue != null) {
        env.put(envName, envValue);
      }
    }

    String keytab = zConf.getString(ZeppelinConfiguration.ConfVars.ZEPPELIN_SERVER_KERBEROS_KEYTAB);
    String principal =
        zConf.getString(ZeppelinConfiguration.ConfVars.ZEPPELIN_SERVER_KERBEROS_PRINCIPAL);

    if (!StringUtils.isBlank(keytab) && !StringUtils.isBlank(principal)) {
      env.put("ZEPPELIN_SERVER_KERBEROS_KEYTAB", keytab);
      env.put("ZEPPELIN_SERVER_KERBEROS_PRINCIPAL", principal);
      LOGGER.info("Run Spark under secure mode with keytab: " + keytab +
          ", principal: " + principal);
    } else {
      LOGGER.info("Run Spark under non-secure mode as no keytab and principal is specified");
    }
    LOGGER.debug("buildEnvFromProperties: " + env);
    return env;

  }

  private void kinit(String principal, String keytab) throws IOException {
    Executor executor = new DefaultExecutor();
    CommandLine cmdLine = CommandLine.parse(String.format("kinit %s -kt %s", principal, keytab));
    LOGGER.info(cmdLine.toString());
    int exitValue = executor.execute(cmdLine);
    if (exitValue != 0) {
      throw new IOException(String.format("Failed to kinit witjh exit code %d", exitValue));
    }
    else {
      LOGGER.info ("kinit succeeded");
    }
  }

  class KinitTask extends TimerTask {
    private final String principal;
    private final String keytab;

    KinitTask(String principal, String keytab) {
      this.principal = principal;
      this.keytab = keytab;
    }

    public void run() {
      try {
        kinit(principal, keytab);
      } catch (IOException e) {
        LOGGER.error(e.getMessage());
      }
    }
  }

  @Override
  public InterpreterClient launch(InterpreterLaunchContext context) throws IOException {
    long renewPeriod = zConf.getLong(
            "ZEPPELIN_KERBEROS_RENEW_PERIOD",
            "zeppelin.kerberos.renew.period",
            120);
    String principal = context.getProperties().getProperty("spark.yarn.principal");
    String keytab = context.getProperties().getProperty("spark.yarn.keytab");
    if (keytab != null && principal != null) {
      kinit(principal, keytab);
      Timer time = new Timer();
      time.schedule(new KinitTask(principal, keytab), 0L, renewPeriod * 60 * 1000);
      
    }
    return super.launch(context);
  }


  /**
   * get environmental variable in the following order
   *
   * 1. interpreter setting
   * 2. zeppelin-env.sh
   *
   */
  private String getEnv(String envName) {
    String env = properties.getProperty(envName);
    if (env == null) {
      env = System.getenv(envName);
    }
    return env;
  }

  private boolean isSparkConf(String key, String value) {
    return !StringUtils.isEmpty(key) && key.startsWith("spark.") && !StringUtils.isEmpty(value);
  }

  private void setupPropertiesForPySpark(Properties sparkProperties) {
    if (isYarnMode()) {
      sparkProperties.setProperty("spark.yarn.isPython", "true");
    }
  }

  private void mergeSparkProperty(Properties sparkProperties, String propertyName,
                                  String propertyValue) {
    if (sparkProperties.containsKey(propertyName)) {
      String oldPropertyValue = sparkProperties.getProperty(propertyName);
      sparkProperties.setProperty(propertyName, oldPropertyValue + "," + propertyValue);
    } else {
      sparkProperties.setProperty(propertyName, propertyValue);
    }
  }

  private void setupPropertiesForSparkR(Properties sparkProperties) {
    String sparkHome = getEnv("SPARK_HOME");
    File sparkRBasePath = null;
    if (sparkHome == null) {
      if (!getSparkMaster(properties).startsWith("local")) {
        throw new RuntimeException("SPARK_HOME is not specified in interpreter-setting" +
            " for non-local mode, if you specify it in zeppelin-env.sh, please move that into " +
            " interpreter setting");
      }
      String zeppelinHome = zConf.getString(ZeppelinConfiguration.ConfVars.ZEPPELIN_HOME);
      sparkRBasePath = new File(zeppelinHome,
          "interpreter" + File.separator + "spark" + File.separator + "R");
    } else {
      sparkRBasePath = new File(sparkHome, "R" + File.separator + "lib");
    }

    File sparkRPath = new File(sparkRBasePath, "sparkr.zip");
    if (sparkRPath.exists() && sparkRPath.isFile()) {
      mergeSparkProperty(sparkProperties, "spark.yarn.dist.archives", sparkRPath.getAbsolutePath());
    } else {
      LOGGER.warn("sparkr.zip is not found, SparkR may not work.");
    }
  }

  /**
   * Order to look for spark master
   * 1. master in interpreter setting
   * 2. spark.master interpreter setting
   * 3. use local[*]
   * @param properties
   * @return
   */
  private String getSparkMaster(Properties properties) {
    String master = properties.getProperty("master");
    if (master == null) {
      master = properties.getProperty("spark.master");
      if (master == null) {
        master = "local[*]";
      }
    }
    return master;
  }

  private String getDeployMode() {
    String master = getSparkMaster(properties);
    if (master.equals("yarn-client")) {
      return "client";
    } else if (master.equals("yarn-cluster")) {
      return "cluster";
    } else if (master.startsWith("local")) {
      return "client";
    } else {
      String deployMode = properties.getProperty("spark.submit.deployMode");
      if (deployMode == null) {
        throw new RuntimeException("master is set as yarn, but spark.submit.deployMode " +
            "is not specified");
      }
      if (!deployMode.equals("client") && !deployMode.equals("cluster")) {
        throw new RuntimeException("Invalid value for spark.submit.deployMode: " + deployMode);
      }
      return deployMode;
    }
  }

  private boolean isYarnMode() {
    return getSparkMaster(properties).startsWith("yarn");
  }

  private String toShellFormat(String value) {
    if (value.contains("'") && value.contains("\"")) {
      throw new RuntimeException("Spark property value could not contain both \" and '");
    } else if (value.contains("'")) {
      return "\"" + value + "\"";
    } else {
      return "'" + value + "'";
    }
  }

}
