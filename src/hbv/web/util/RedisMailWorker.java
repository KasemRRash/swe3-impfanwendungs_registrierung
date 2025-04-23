package hbv.web.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;

/*
 * RedisMailWorker verarbeitet Mail-Jobs aus der Redis-Queue "mailQueue"
 * - Startet bei Bedarf und beendet sich nach der Verarbeitung
 */
public class RedisMailWorker implements Runnable {
  private final JedisAdapter jedisAdapter;

  public RedisMailWorker(String redisHost, int redisPort, String redisPassword) {
    this.jedisAdapter = JedisAdapter.getInstance(redisHost, redisPort, redisPassword);
  }

  @Override
  public void run() {
    try (Jedis jedis = jedisAdapter.getJedis()) {
      // Nur eine Mail aus der Queue holen (nicht dauerhaft laufen!)
      List<String> job = jedis.brpop(0, "mailQueue");

      if (job != null && job.size() > 1) {
        String jobString = job.get(1);
        Map<String, String> jobMap = parseJobString(jobString);

        if (jobMap != null) {
          processEmail(jobMap.get("email"), jobMap.get("pdf_path"), jobMap.get("booking_id"));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Map<String, String> parseJobString(String jobString) {
    Map<String, String> jobMap = new HashMap<>();

    try {
      JSONObject json = new JSONObject(jobString);
      jobMap.put("email", json.getString("email"));
      jobMap.put("pdf_path", json.getString("pdf_path"));
      jobMap.put("booking_id", json.getString("booking_id"));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return jobMap;
  }

  private void processEmail(String email, String pdfPath, String bookingId) {
    if (email == null || pdfPath == null) {
      return;
    }

    try (Jedis jedis = jedisAdapter.getJedis()) {
      jedis.sadd("sentMails", email + " -> " + pdfPath);
      jedis.lpush("mailLogs", "Mail an: " + email + " PDF: " + pdfPath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
