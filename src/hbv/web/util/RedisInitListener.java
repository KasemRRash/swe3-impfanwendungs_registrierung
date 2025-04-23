package hbv.web.util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.concurrent.ExecutorService;

/*
 * Listener-Klasse, die beim Starten von Tomcat automatisch ausgeführt wird
 * - Erstellt eine redisverbindung über JedisAdapter
 * - Startet den redisMailWorker klasse über einen threadpool
 * - Beendet den worker, wenn Tomcat heruntergefahren wird
 */

public class RedisInitListener implements ServletContextListener {
  private ExecutorService executor; // pool für Worker-Threads
  // private Thread mailWorkerThread;
  private JedisAdapter jedisAdapter; // redisverbindung für das gesamte System

  /*
   * Wird aufgerufen, wenn Tomcat gestartet wird.
   * - Erstellt eine Verbindung zu Redis
   * - Startet den RedisMailWorker in einem separaten threadpool
   * @param servletContextEvent Das ServletContextEvent, das den kontext enthält
   */
  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    // zugriff auf den ServletContext
    ServletContext ctx = servletContextEvent.getServletContext();

    // liest die rediskonfigswerte
    String redispassword = ctx.getInitParameter("redispassword");
    String redisserver = ctx.getInitParameter("redisserver");

    // erstellt eine Verbindung zu Redis (JedisAdapter verwaltet den Verbindungspool)
    jedisAdapter = JedisAdapter.getInstance(redisserver, 6379, redispassword);

    // Speichert jedisAdapter im ServletContext, sodass andere Servlets darauf
    // zugreifen können
    ctx.setAttribute("jedisAdapter", jedisAdapter);
  }

  /*
   * Wird aufgerufen, wenn Tomcat heruntergefahren wird.
   * - Stoppt den RedisMailWorker
   * - Schließt die redisverbindung
   * @param servletContextEvent Das servletContextEvent, das den kontext hat
   */
  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    // schließt die redisverbindung
    if (jedisAdapter != null) {
      jedisAdapter.destroy();
    }
  }
}
