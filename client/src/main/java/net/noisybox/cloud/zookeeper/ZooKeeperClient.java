package net.noisybox.cloud.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.apache.zookeeper.Watcher.Event.EventType.NodeCreated;

public class ZooKeeperClient {

    public static final String CONNECT_STRING = "localhost:2181";

    private final Watcher watcher = new ClientWatcher();

    public static void main(String[] args) throws Exception {
        ZooKeeperClient client = new ZooKeeperClient();
        client.run();
    }

    private void run() throws Exception {
        ZooKeeper client = connect();

        Stat stat = new Stat();
        byte[] result = client.getData("/test/thing", watcher, stat);
        System.out.println("Result: " + new String(result, "UTF-8"));

        CountDownLatch latch = setUpWatchForSpecial(client);
        latch.await();
        System.out.println("Shutting down.");
        client.close();
    }

    private CountDownLatch setUpWatchForSpecial(final ZooKeeper client) throws KeeperException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        System.out.println("Waiting for /special to show up...");
        Watcher specialWatcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                boolean isNodeCreated = watchedEvent.getType().equals(NodeCreated);
                if (isNodeCreated && "/special".equals(watchedEvent.getPath())) {
                    System.out.println("Special showed up....shutting down.");
                    latch.countDown();
                } else {
                    System.out.println(" (something happened, but not what we wanted)");
                    registerWatcherForSpecial(client, this);
                }
            }
        };
        registerWatcherForSpecial(client, specialWatcher);
        return latch;
    }

    private void registerWatcherForSpecial(ZooKeeper client, Watcher specialWatcher)  {
        try {
            client.exists("/special", specialWatcher);
        } catch (KeeperException|InterruptedException e) {
            throw new RuntimeException("Error setting up /special watcher: ", e);
        }
    }

    private ZooKeeper connect() throws IOException {
        System.out.println("Connecting to " + CONNECT_STRING);
        ZooKeeper client = new ZooKeeper(CONNECT_STRING, 10000, watcher);
        System.out.println("Connected (session id = " + client.getSessionId() + ")");
        return client;
    }

    static class ClientWatcher implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println("watched event: " + watchedEvent);
        }
    }

}
