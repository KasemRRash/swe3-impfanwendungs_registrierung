package hbv.web.servlet;

import hbv.web.db.BuchungVerwalten;
import hbv.web.db.UserVerwalten;
import hbv.web.util.JedisAdapter;
import hbv.web.util.MailService;
import hbv.web.util.PdfBoxGenerator;
import hbv.web.util.PdfService;
import hbv.web.util.RedisMailService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import redis.clients.jedis.Jedis;

public class TerminBuchen extends HttpServlet {

  private PdfService pdfService;

  @Override
  public void init() {
    this.pdfService = new PdfBoxGenerator();
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();
    if (pdfService == null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      out.print("{\"error\": \"PDF-Service nicht geladen!\"}");
      return;
    }
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      out.print("{\"error\": \"Nicht eingeloggt!\"}");
      return;
    }
    String email = (String) session.getAttribute("user");
    String datum = request.getParameter("datum");
    String zeit = request.getParameter("zeit");
    String impfstoffIdString = request.getParameter("impfstoff_id");
    String impfzentrum = request.getParameter("impfzentrum");
    if (datum == null || zeit == null || impfstoffIdString == null || impfzentrum == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print("{\"error\": \"Fehlende Parameter!\"}");
      return;
    }
    int impfstoffId;
    try {
      impfstoffId = Integer.parseInt(impfstoffIdString);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print("{\"error\": \"Ungültige Impfstoff-ID!\"}");
      return;
    }
    try {
      UserVerwalten userVerwalten = UserVerwalten.getInstance();
      BuchungVerwalten buchungVerwalten = BuchungVerwalten.getInstance();
      int userId = userVerwalten.getUserIdByEmail(email);
      int slotId = buchungVerwalten.getSlotId(datum, zeit, impfzentrum);
      if (userId == -1) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print("{\"error\": \"Benutzer nicht gefunden!\"}");
        return;
      }
      if (slotId == -1) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print("{\"error\": \"Kein passender Zeitslot gefunden!\"}");
        return;
      }
      /* boolean buchungErfolgreich = buchungVerwalten.buchungHinzufuegen(userId, slotId, impfstoffId);
      if (!buchungErfolgreich) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      out.print("{\"error\": \"Fehler beim Speichern der Buchung!\"}");
      return;
      }
      */
      int bookingId = buchungVerwalten.buchungHinzufuegen(userId, slotId, impfstoffId);
      if (bookingId == -1) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print("{\"error\": \"Maximale Buchungsanzahl erreicht!\"}");
        return;
      }
      File pdfFile;
      try {
        pdfFile = pdfService.generatePDF(String.valueOf(bookingId), datum, impfzentrum);
        ServletContext ctx = request.getServletContext();
        JedisAdapter jedisAdapter = (JedisAdapter) ctx.getAttribute("jedisAdapter");
        boolean redisConnected = false;
        try (Jedis jedis = jedisAdapter.getJedis()) {
          jedis.set("testKey", "TerminBuchen läuft!");
          String testValue = jedis.get("testKey");
          redisConnected = true;
        } catch (Exception e) {
          e.printStackTrace();
        }
        MailService mailService = new RedisMailService(jedisAdapter);
        boolean mailJobSaved = false;
        try {
          mailService.sendMail(email, pdfFile.getAbsolutePath(), String.valueOf(bookingId));
          mailJobSaved = true;
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print(
            "{\"error\": \"Fehler bei der PDF-Erstellung!\", \"details\": \""
                + e.getMessage()
                + "\"}");
        return;
      }
      out.print("{ \"success\": \"Termin erfolgreich gebucht!\" }");
      out.flush();
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      out.print("{\"error\": \"Allgemeiner Fehler!\", \"details\": \"" + e.getMessage() + "\"}");
    }
  }
}
