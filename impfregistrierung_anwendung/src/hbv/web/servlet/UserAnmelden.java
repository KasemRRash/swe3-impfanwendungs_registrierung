package hbv.web.servlet;

import hbv.web.db.UserVerwalten;
import hbv.web.model.User;
import hbv.web.service.UserHelper;
import hbv.web.util.PasswortHelper;
import hbv.web.util.PasswortService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import org.json.JSONObject;

public class UserAnmelden extends HttpServlet {
  private UserHelper userService;

  @Override
  public void init() throws ServletException {
    PasswortService passwortService = new PasswortHelper();
    userService = new UserHelper(UserVerwalten.getInstance(), passwortService);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    String email = request.getParameter("email");
    String password = request.getParameter("password");

    JSONObject jsonResponse = new JSONObject();

    try {
      User user = UserVerwalten.getInstance().findeUser(email);
      boolean loginErfolgreich = userService.authentifiziereUser(email, password);
      boolean istMitarbeiter = user.getIstMitarbeiter();
      if (loginErfolgreich) {
        HttpSession session = request.getSession();
        session.setAttribute("user", email);
        session.setMaxInactiveInterval(120); // zum Testen auf 2 Minuten
        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setHttpOnly(true); // Verhindert Zugriff durch JavaScript (XSS)
        sessionCookie.setSecure(true); // Cookie wird nur über HTTPS übertragen
        response.addCookie(sessionCookie);

        jsonResponse.put("status", "success");
        jsonResponse.put("message", "Login erfolgreich");
        jsonResponse.put("redirect", istMitarbeiter ? "mitarbeiter" : "termine");
        // jsonResponse.put("redirect", "termine");
        // if ("test@mitarbeiter.de".equalsIgnoreCase(email)) {

        // jsonResponse.put("redirect", "mitarbeiter");
        // } else {
        //   jsonResponse.put("redirect", "termine");
        // }
        response.setStatus(HttpServletResponse.SC_OK);
        //  response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        //  response.setHeader("Location", "termine.html");
      } else {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        jsonResponse.put("status", "error");
        jsonResponse.put("message", "Falsche E-Mail oder Passwort.");
      }
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      jsonResponse.put("status", "error");
      jsonResponse.put("message", "Fehler beim Login." + e.getMessage());
    }
    out.println(jsonResponse.toString());
  }
}
