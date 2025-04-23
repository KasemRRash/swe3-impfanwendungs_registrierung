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
import java.util.List;

public class BuchungAnzeigen extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      out.print("{\"error\": \"Nicht eingeloggt!\"}");
      return;
    }

    String email = (String) session.getAttribute("user");

    try {
      List<String> buchungen = BuchungVerwalten.getInstance().getBuchungenByUser(email, session);
      out.print("{\"buchungen\": " + buchungen + "}");
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      out.print(
          "{\"error\": \"Datenbankfehler beim Abrufen der Buchungen\", \"details\": \""
              + e.getMessage()
              + "\"}");
    }
  }
}
