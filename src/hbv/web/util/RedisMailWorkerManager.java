package hbv.web.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RedisMailWorkerManager {
  private static volatile RedisMailWorkerManager instance;
  private static final Object lock = new Object();
  private ExecutorService executor;

  private RedisMailWorkerManager() {
    executor = Executors.newCachedThreadPool();
  }

  public static RedisMailWorkerManager getInstance() {
    if (instance == null) {
      synchronized (lock) {
        if (instance == null) {
          instance = new RedisMailWorkerManager();
        }
      }
    }
    return instance;
  }

  public synchronized void startWorker() {
    if (executor == null || executor.isShutdown()) {
      executor = Executors.newCachedThreadPool();
    }
    executor.submit(new RedisMailWorker("localhost", 6379, "redispassword"));
  }

  public synchronized void stopWorker() {
    if (executor != null && !executor.isShutdown()) {
      executor.shutdown();
    }
  }
}
