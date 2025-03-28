package hbv.web.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisAdapter {
  // thread-sichere singleton-Instanz
  private static volatile JedisAdapter instance;

  // Lock-Objekt für Synchronisation
  private static final Object block = new Object();

  // Verbindungspool
  private JedisPool jedisPool;

  /*
   * Privater Konstruktor, um sicherzustellen, dass die klasse nur über methde getInstance() instanziiert wird.
   * @throws IllegalArgumentException Falls passwort null oder leer ist
   */
  private JedisAdapter(String host, int port, String password) {
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Redispasswort fehlt");
    }

    // konfigobjekt für den Jedis-Verbindungspool
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(300); // Maximale Anzahl an gleichzeitigen Redis-Verbindungen
    poolConfig.setMaxIdle(100); // Maximale Anzahl an inaktiven Verbindungen im Pool
    poolConfig.setMinIdle(10); // Minimale Anzahl an inaktiven Verbindungen im Pool

    // jedispool erstellen
    this.jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
  }

  /*
   * Gibt die Singleton-Instanz des JedisAdapters zurück oder erstellt eine neue Instanz.
   * @return Die Singleton-Instanz von JedisAdapter
   */
  public static JedisAdapter getInstance(String host, int port, String password) {
    if (instance == null) { // Erster Check ohne Synchronisation
      synchronized (block) { // Synchronisierung, um parallele Instanz-Erstellung zu verhindern
        if (instance == null) { // Zweiter Check innerhalb der Synchronisation
          instance = new JedisAdapter(host, port, password);
        }
      }
    }
    return instance;
  }

  /*
   * holt eine verbindung aus dem pool
   */
  public Jedis getJedis() {
    return jedisPool.getResource();
  }

  /*
   * schließt redispool und gibt alle ressourcen frei
   */
  public void destroy() {
    if (jedisPool != null && !jedisPool.isClosed()) {
      jedisPool.close();
    }
  }
}
