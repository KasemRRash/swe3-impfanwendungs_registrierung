package hbv.web.servlet;

import hbv.web.db.ImpfzentrumVerwalten;
import hbv.web.model.Impfzentrum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImpfzentrenAnzeigen extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try (PrintWriter out = response.getWriter()) {
      ImpfzentrumVerwalten impfzentrumverwalten = ImpfzentrumVerwalten.getInstance();
      List<Impfzentrum> impfzentren = impfzentrumverwalten.getAlleImpfzentren();
      if (impfzentren.isEmpty()) {
        out.print("[]");
        return;
      }
      JSONArray jsonArray = new JSONArray();
      for (Impfzentrum impfzentrum : impfzentren) {
        JSONObject jsonImpfzentrum = new JSONObject();
        jsonImpfzentrum.put("impfzentrum", impfzentrum.getName());
        jsonArray.put(jsonImpfzentrum);
      }
      out.print(jsonArray.toString());
      out.flush();
    } catch (IOException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response
          .getWriter()
          .write("{\"status\": \"error\", \"message\": \"Fehler beim Laden der Impfzentren.\"}");
    }
  }
}
