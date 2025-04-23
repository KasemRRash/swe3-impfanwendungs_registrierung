package hbv.web.servlet;

import hbv.web.db.ImpfstoffVerwalten;
import hbv.web.model.Impfstoff;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImpfstoffAnzeigen extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    PrintWriter out = response.getWriter();

    // Impfzentren aus der Anfrage holen
    String impfzentrenParam = request.getParameter("impfzentren");

    if (impfzentrenParam == null || impfzentrenParam.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      out.print("{\"error\": \"Kein Impfzentrum angegeben!\"}");
      return;
    }

    // Liste der Impfzentren aus der Anfrage verarbeiten
    List<String> impfzentren = Arrays.asList(impfzentrenParam.split(","));

    // Nur die Impfstoffe abrufen, die für diese Impfzentren verfügbar sind
    List<Impfstoff> impfstoffe = ImpfstoffVerwalten.getImpfstoffeFuerImpfzentren(impfzentren);
    JSONArray impfstoffeArray = new JSONArray();

    for (Impfstoff impfstoff : impfstoffe) {
      JSONObject impfstoffObj = new JSONObject();
      impfstoffObj.put("id", impfstoff.getId());
      impfstoffObj.put("name", impfstoff.getName());
      impfstoffeArray.put(impfstoffObj);
    }

    out.print(impfstoffeArray.toString());
    out.flush();
  }
}
