package hbv.web.servlet;

import hbv.web.db.ImpfzentrumVerwalten;
import hbv.web.model.BuchungSlot;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MitarbeiterServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String impfzentrumName = request.getParameter("impfzentrum");
    if (impfzentrumName == null) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Kein Impfzentrum angegeben");
      return;
    }
    List<BuchungSlot> buchungen = ImpfzentrumVerwalten.getBuchungenFuerImpfzentrum(impfzentrumName);
    JSONArray jsonArray = new JSONArray();
    for (BuchungSlot bs : buchungen) {
      JSONObject jo = new JSONObject();
      jo.put("datum", bs.getDatum());
      jo.put("zeitslot", bs.getZeitslot());
      jo.put("anzahl", bs.getAnzahl());
      jo.put("kapazitaet", bs.getKapazitaet());
      jsonArray.put(jo);
    }
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    PrintWriter out = response.getWriter();
    out.print(jsonArray.toString());
    out.flush();
  }
}
