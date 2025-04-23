package hbv.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

public class SessionPruefen extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    HttpSession session =
        request.getSession(false); // Prüft nur vorhandene Session, erstellt keine neue
    JSONObject jsonResponse = new JSONObject();

    if (session != null && session.getAttribute("user") != null) {
      jsonResponse.put("session", "active");
      jsonResponse.put("user", session.getAttribute("user")); // Optional: Nutzername zurückgeben
    } else {
      jsonResponse.put("session", "inactive");
    }

    out.println(jsonResponse.toString());
    out.flush();
  }
}
