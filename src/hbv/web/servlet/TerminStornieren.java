package hbv.web.servlet;

import hbv.web.db.BuchungVerwalten;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class TerminStornieren extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    // Prüfe, ob der Benutzer eingeloggt ist
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      out.print("{\"error\": \"Nicht eingeloggt!\"}");
      return;
    }

    String email = (String) session.getAttribute("user");
    String buchungIdStr = request.getParameter("buchung_id");

    if (buchungIdStr == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print("{\"error\": \"Fehlende Buchungs-ID!\"}");
      return;
    }

    int buchungId;
    try {
      buchungId = Integer.parseInt(buchungIdStr);
    } catch (NumberFormatException e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print("{\"error\": \"Ungültige Buchungs-ID!\"}");
      return;
    }

    try {
      BuchungVerwalten buchungVerwalten = BuchungVerwalten.getInstance();
      boolean storniert = buchungVerwalten.buchungLoeschen(email, buchungId);

      if (storniert) {
        response.setStatus(HttpServletResponse.SC_OK);
        out.print("{\"success\": \"Termin erfolgreich storniert!\"}");
      } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print("{\"error\": \"Stornierung fehlgeschlagen!\"}");
      }
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      out.print("{\"error\": \"Fehler beim Stornieren der Buchung!\"}");
    }
  }
}
