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

package org.apache.zeppelin.plugin;

import com.google.common.annotations.VisibleForTesting;
import org.apache.zeppelin.background.NoteBackgroundTaskManager;
import org.apache.zeppelin.conf.ZeppelinConfiguration;
import org.apache.zeppelin.interpreter.launcher.InterpreterLauncher;
import org.apache.zeppelin.interpreter.recovery.RecoveryStorage;
import org.apache.zeppelin.notebook.repo.NotebookRepo;
import org.apache.zeppelin.notebook.repo.OldNotebookRepo;
import org.apache.zeppelin.serving.DummyNoteBackgroundTaskManager;
import org.apache.zeppelin.serving.DummyRestApiRouter;
import org.apache.zeppelin.serving.MetricStorage;
import org.apache.zeppelin.serving.RedisMetricStorage;
import org.apache.zeppelin.serving.RestApiRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for loading Plugins
 */
public class PluginManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

  private static PluginManager instance;

  private ZeppelinConfiguration zConf = ZeppelinConfiguration.create();
  private String pluginsDir = zConf.getPluginsDir();

  private Map<String, InterpreterLauncher> cachedLaunchers = new HashMap<>();
  private Map<String, NoteBackgroundTaskManager> cachedBackgroundTaskManagers = new HashMap<>();
  private Map<String, RestApiRouter> cachedRestApiRouter = new HashMap<>();

  public static synchronized PluginManager get() {
    if (instance == null) {
      instance = new PluginManager();
    }
    return instance;
  }

  public NotebookRepo loadNotebookRepo(String notebookRepoClassName) throws IOException {
    LOGGER.info("Loading NotebookRepo Plugin: " + notebookRepoClassName);
    // load plugin from classpath directly first for these builtin NotebookRepo (such as VFSNoteBookRepo
    // and GitNotebookRepo). If fails, then try to load it from plugin folder
    try {
      NotebookRepo notebookRepo = (NotebookRepo)
              (Class.forName(notebookRepoClassName).newInstance());
      return notebookRepo;
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      LOGGER.warn("Fail to instantiate notebookrepo from classpath directly:" + notebookRepoClassName, e);
    }

    String simpleClassName = notebookRepoClassName.substring(notebookRepoClassName.lastIndexOf(".") + 1);
    URLClassLoader pluginClassLoader = getPluginClassLoader(pluginsDir, "NotebookRepo", simpleClassName);
    if (pluginClassLoader == null) {
      return null;
    }
    NotebookRepo notebookRepo = null;
    try {
      notebookRepo = (NotebookRepo) (Class.forName(notebookRepoClassName, true, pluginClassLoader)).newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      LOGGER.warn("Fail to instantiate notebookrepo from plugin classpath:" + notebookRepoClassName, e);
    }

    if (notebookRepo == null) {
      LOGGER.warn("Unable to load NotebookRepo Plugin: " + notebookRepoClassName);
    }
    return notebookRepo;
  }

  private String getOldNotebookRepoClassName(String notebookRepoClassName) {
    int pos = notebookRepoClassName.lastIndexOf(".");
    return notebookRepoClassName.substring(0, pos) + ".Old" + notebookRepoClassName.substring(pos + 1);
  }

  /**
   * This is a temporary class which is used for loading old implemention of NotebookRepo.
   *
   * @param notebookRepoClassName
   * @return
   * @throws IOException
   */
  public OldNotebookRepo loadOldNotebookRepo(String notebookRepoClassName) throws IOException {
    LOGGER.info("Loading OldNotebookRepo Plugin: " + notebookRepoClassName);
    // load plugin from classpath directly when it is test.
    // otherwise load it from plugin folder
    String isTest = System.getenv("IS_ZEPPELIN_TEST");
    if (isTest != null && isTest.equals("true")) {
      try {
        OldNotebookRepo notebookRepo = (OldNotebookRepo)
            (Class.forName(notebookRepoClassName).newInstance());
        return notebookRepo;
      } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
        LOGGER.warn("Fail to instantiate notebookrepo from classpath directly:" + notebookRepoClassName, e);
      }
    }

    String simpleClassName = notebookRepoClassName.substring(notebookRepoClassName.lastIndexOf(".") + 1);
    URLClassLoader pluginClassLoader = getPluginClassLoader(pluginsDir, "NotebookRepo", simpleClassName);
    if (pluginClassLoader == null) {
      return null;
    }
    OldNotebookRepo notebookRepo = null;
    try {
      notebookRepoClassName = getOldNotebookRepoClassName(notebookRepoClassName);
      notebookRepo = (OldNotebookRepo) (Class.forName(notebookRepoClassName, true, pluginClassLoader)).newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      LOGGER.warn("Fail to instantiate notebookrepo from plugin classpath:" + notebookRepoClassName, e);
    }

    if (notebookRepo == null) {
      LOGGER.warn("Unable to load NotebookRepo Plugin: " + notebookRepoClassName);
    }
    return notebookRepo;
  }

  public synchronized InterpreterLauncher loadInterpreterLauncher(String launcherPlugin,
                                                                  RecoveryStorage recoveryStorage)
      throws IOException {

    if (cachedLaunchers.containsKey(launcherPlugin)) {
      return cachedLaunchers.get(launcherPlugin);
    }
    LOGGER.info("Loading Interpreter Launcher Plugin: " + launcherPlugin);
    URLClassLoader pluginClassLoader = getPluginClassLoader(pluginsDir, "Launcher", launcherPlugin);
    String pluginClass = "org.apache.zeppelin.interpreter.launcher." + launcherPlugin;
    InterpreterLauncher launcher = null;
    try {
      launcher = (InterpreterLauncher) (Class.forName(pluginClass, true, pluginClassLoader))
          .getConstructor(ZeppelinConfiguration.class, RecoveryStorage.class)
          .newInstance(zConf, recoveryStorage);
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException
        | NoSuchMethodException | InvocationTargetException e) {
      LOGGER.warn("Fail to instantiate Launcher from plugin classpath:" + launcherPlugin, e);
    }

    if (launcher == null) {
      throw new IOException("Fail to load plugin: " + launcherPlugin);
    }
    cachedLaunchers.put(launcherPlugin, launcher);
    return launcher;
  }

  public synchronized NoteBackgroundTaskManager loadNoteBackgroundTaskManager() throws IOException {
    if (zConf.getRunMode() == ZeppelinConfiguration.RUN_MODE.K8S) {
      /**
       * For now, class name is hardcoded here.
       * Later, we can make it configurable if necessary.
       */
      return loadNoteBackgroundTaskManager(
              "K8sStandardInterpreterLauncher",
              "org.apache.zeppelin.serving.K8sNoteServingTaskManager"
      );
    } else {
      return new DummyNoteBackgroundTaskManager(zConf);
    }
  }

  public synchronized NoteBackgroundTaskManager loadNoteTestTaskManager() throws IOException {
    if (zConf.getRunMode() == ZeppelinConfiguration.RUN_MODE.K8S) {
      /**
       * For now, class name is hardcoded here.
       * Later, we can make it configurable if necessary.
       */
      return loadNoteBackgroundTaskManager(
              "K8sStandardInterpreterLauncher",
              "org.apache.zeppelin.test.K8sNoteTestTaskManager"
      );
    } else {
      return new DummyNoteBackgroundTaskManager(zConf);
    }
  }

  public synchronized NoteBackgroundTaskManager loadNoteBackgroundTaskManager(String launcherPlugin,
                                                                              String pluginClass) throws IOException {

    if (cachedBackgroundTaskManagers.containsKey(pluginClass)) {
      return cachedBackgroundTaskManagers.get(pluginClass);
    }
    LOGGER.info("Loading Interpreter Launcher Plugin: " + launcherPlugin);
    URLClassLoader pluginClassLoader = getPluginClassLoader(pluginsDir, "Launcher", launcherPlugin);
    NoteBackgroundTaskManager taskManager = null;
    try {
      taskManager = (NoteBackgroundTaskManager) (Class.forName(pluginClass, true, pluginClassLoader))
              .getConstructor(ZeppelinConfiguration.class)
              .newInstance(zConf);
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException
            | NoSuchMethodException | InvocationTargetException e) {
      LOGGER.warn("Fail to instantiate Launcher from plugin classpath:" + launcherPlugin, e);
    }

    if (taskManager == null) {
      throw new IOException("Fail to load plugin: " + launcherPlugin);
    }
    cachedBackgroundTaskManagers.put(pluginClass, taskManager);
    return taskManager;
  }

  public synchronized RestApiRouter loadNoteServingRestApiRouter() throws IOException {
    if (zConf.getRunMode() == ZeppelinConfiguration.RUN_MODE.K8S) {
      String backgroundTaskType = System.getenv("ZEPPELIN_BACKGROUND_TYPE");
      if ("test".equalsIgnoreCase(backgroundTaskType)) {
        // if NoteTestTask shouldn't add route during test.
        return new DummyRestApiRouter();
      }

      /**
       * For now, class name is hardcoded here.
       * Later, we can make it configurable if necessary.
       */
      return loadNoteServingRestApiRouter(
              "K8sStandardInterpreterLauncher",
              "org.apache.zeppelin.serving.K8sRestApiRouter"
      );
    } else {
      return new DummyRestApiRouter();
    }
  }

  public synchronized RestApiRouter loadNoteServingRestApiRouter(String launcherPlugin,
                                                                 String pluginClass) throws IOException {

    if (cachedRestApiRouter.containsKey(launcherPlugin)) {
      return cachedRestApiRouter.get(launcherPlugin);
    }
    LOGGER.info("Loading Interpreter Launcher Plugin: " + launcherPlugin);
    URLClassLoader pluginClassLoader = getPluginClassLoader(pluginsDir, "Launcher", launcherPlugin);
    RestApiRouter apiRouter = null;
    try {
      apiRouter = (RestApiRouter) (Class.forName(pluginClass, true, pluginClassLoader))
              .getConstructor(ZeppelinConfiguration.class)
              .newInstance(zConf);
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException
            | NoSuchMethodException | InvocationTargetException e) {
      LOGGER.warn("Fail to instantiate Launcher from plugin classpath:" + launcherPlugin, e);
    }

    if (apiRouter == null) {
      throw new IOException("Fail to load plugin: " + launcherPlugin);
    }
    cachedRestApiRouter.put(launcherPlugin, apiRouter);
    return apiRouter;
  }

  public synchronized MetricStorage loadNoteServingMetricStorage() throws IOException {
    String redisAddr = zConf.getInterpreterMetricRedisAddr();
    if (redisAddr != null) {
      return new RedisMetricStorage(
              redisAddr,
              "",
              "",
              RedisMetricStorage.DEFAULT_METRIC_EXPIRE_SEC);
    }
    return null;
  }

  private URLClassLoader getPluginClassLoader(String pluginsDir,
                                              String pluginType,
                                              String pluginName) throws IOException {

    File pluginFolder = new File(pluginsDir + "/" + pluginType + "/" + pluginName);
    if (!pluginFolder.exists() || pluginFolder.isFile()) {
      LOGGER.warn("PluginFolder " + pluginFolder.getAbsolutePath() +
          " doesn't exist or is not a directory");
      return null;
    }
    List<URL> urls = new ArrayList<>();
    for (File file : pluginFolder.listFiles()) {
      LOGGER.debug("Add file " + file.getAbsolutePath() + " to classpath of plugin: "
          + pluginName);
      urls.add(file.toURI().toURL());
    }
    if (urls.isEmpty()) {
      LOGGER.warn("Can not load plugin " + pluginName +
          ", because the plugin folder " + pluginFolder + " is empty.");
      return null;
    }
    return new URLClassLoader(urls.toArray(new URL[0]));
  }

  @VisibleForTesting
  public static void reset() {
    instance = null;
  }

}
