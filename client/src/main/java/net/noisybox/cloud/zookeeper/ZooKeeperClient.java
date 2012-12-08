package net.noisybox.cloud.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperClient {

    public static final String CONNECT_STRING = "localhost:2181";

    private final Watcher watcher = new ClientWatcher();

    public static void main(String[] args) throws Exception {
        ZooKeeperClient client = new ZooKeeperClient();
        client.run();

    }

    private void run() throws Exception {
        System.out.println("Connecting to " + CONNECT_STRING);
        ZooKeeper client = new ZooKeeper(CONNECT_STRING, 10000, watcher);
        System.out.println("Connected (session id = " + client.getSessionId() + ")");
        Stat stat = new Stat();
        byte[] result = client.getData("/test/thing", watcher, stat);
        System.out.println("Result: " + new String(result, "UTF-8"));
    }

    static class ClientWatcher implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println("watched event: " + watchedEvent);
        }
    }

}
