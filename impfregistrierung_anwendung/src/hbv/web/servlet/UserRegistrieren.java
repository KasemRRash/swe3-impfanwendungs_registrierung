package hbv.web.servlet;

import hbv.web.db.UserVerwalten;
import hbv.web.service.UserHelper;
import hbv.web.util.PasswortHelper;
import hbv.web.util.PasswortService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import org.json.JSONObject;

public class UserRegistrieren extends HttpServlet {
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
    String pwConfirm = request.getParameter("passwordConfirm");

    JSONObject jsonResponse = new JSONObject();

    if (!password.equals(pwConfirm)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      jsonResponse.put("status", "error");
      jsonResponse.put("message", "Passwörter stimmen nicht überein.");
      out.println(jsonResponse.toString());
      return;
    }

    try {
      boolean success = userService.registriereUser(email, password);
      if (success) {
        jsonResponse.put("status", "success");
        jsonResponse.put("message", "Registrierung erfolgreich.");

        jsonResponse.put("status", "success");
        jsonResponse.put("message", "Registrierung erfolgreich!");
        //               response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        //                response.setHeader("Location", "login.html");
      } else {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        jsonResponse.put("status", "error");
        jsonResponse.put("message", "E-Mail bereits registriert.");
      }
    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      jsonResponse.put("status", "error");
      jsonResponse.put("message", "Fehler bei der Registrierung.");
    }

    out.println(jsonResponse.toString());
  }
}
