package hbv.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

public class UserAbmelden extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    HttpSession session = request.getSession(false);
    JSONObject jsonResponse = new JSONObject();

    if (session != null) {
      session.invalidate();

      jsonResponse.put("status", "success");
      jsonResponse.put("message", "Abmeldung erfolgreich");
    } else {
      jsonResponse.put("status", "error");
      jsonResponse.put("message", "Keine aktive Sitzung gefunden.");
    }

    out.println(jsonResponse.toString());
    out.flush();
  }
}
