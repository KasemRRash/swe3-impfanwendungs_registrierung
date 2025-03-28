package hbv.web.util;

import redis.clients.jedis.Jedis;

/*
 * Implementierung des MailService-Interfaces.
 * - Diese Klasse speichert Mail-Jobs in Redis ( in mailQueue), anstatt sie sofort zu
 *   versenden
 * - Die Verarbeitung der Mails übernimmt später der RedisMailWorker
 * - So wird sichergestellt, dass der Mail-Versand asynchron und nicht blockierend erfolgt.
 */

public class RedisMailService implements MailService {
  private final JedisAdapter jedisAdapter;

  /*
   * Konstruktor, der eine Instanz von JedisAdapter benötigt.
   * @param jedisAdapter Die redisverbindung, die für das Speichern von
   * Mailjobs verwendet wird.
   */
  public RedisMailService(JedisAdapter jedisAdapter) {
    this.jedisAdapter = jedisAdapter;
  }

  /*
   * Speichert die Maildaten in redis, damit sie später vom RedisMailWorker
   * verarbeitet werden.
   */
  @Override
  public void sendMail(String email, String pdfPath, String bookingId) {
    try (Jedis jedis = jedisAdapter.getJedis()) { // Verwende die Instanzvariable
      String mailJob =
          String.format(
              "{\"email\":\"%s\", \"pdf_path\":\"%s\", \"booking_id\":\"%s\"}",
              email, pdfPath, bookingId);
      // speichere den Mailjob in die mailQueue in Redis
      jedis.lpush("mailQueue", mailJob);
      RedisMailWorkerManager.getInstance().startWorker();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
