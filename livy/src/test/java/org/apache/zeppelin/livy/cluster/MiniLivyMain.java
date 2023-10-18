package org.apache.zeppelin.livy.cluster;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.server.MiniYARNCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MiniLivyMain extends MiniClusterBase {

    public static MiniLivyMain INSTANCE = new MiniLivyMain();

    public static Logger LOG = LoggerFactory.getLogger(MiniLivyMain.class);

    protected Map<String, String> baseLivyConf(String configPath) {
        Map<String, String> conf = new HashMap<>();
        conf.put("livy.spark.master", "yarn");
        conf.put("livy.spark.deploy-mode", "cluster");
        conf.put("livy.server.heartbeat-watchdog.interval", "1s");
        conf.put("livy.server.yarn.poll-interval", "500ms");
        conf.put("livy.server.recovery.mode", "recovery");
        conf.put("livy.server.recovery.state-store", "filesystem");
        conf.put("livy.server.recovery.state-store.url", "file://" + configPath + "/state-store");
        return conf;
    }

    @Override
    protected void start(MiniClusterConfig config, String configPath) {
        Map<String, String> livyConf = baseLivyConf(configPath);

//        if (Cluster.isRunningOnTravis) {
//            livyConf.put("livy.server.yarn.app-lookup-timeout", "2m");
//        }

        MiniClusterUtils.saveProperties(livyConf, new File(configPath + "/livy.conf"));

        val server = new LivyServer();
        server.start();
        server.livyConf.set(LivyConf.ENABLE_HIVE_CONTEXT, false);
        // Write a serverUrl.conf file to the conf directory with the location of the Livy
        // server. Do it atomically since it's used by MiniCluster to detect when the Livy server
        // is up and ready.
        eventually(timeout(30 seconds), interval(1 second)) {
            var serverUrlConf = Map("livy.server.server-url" -> server.serverUrl())
            MiniClusterUtils.saveProperties(serverUrlConf, new File(configPath + "/serverUrl.conf"));
        }
    }
    }
}
