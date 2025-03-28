package hbv.web.servlet;

import hbv.web.db.SlotVerwalten;
import hbv.web.model.Slot;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class FreieSlotsAnzeigen extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    try (PrintWriter out = response.getWriter()) {
      SlotVerwalten slotVerwalten = SlotVerwalten.getInstance();
      List<Slot> slots = slotVerwalten.getVerfuegbareSlots();

      if (slots.isEmpty()) {
        out.print("[]");
        return;
      }

      JSONArray jsonArray = new JSONArray();
      for (Slot slot : slots) {
        JSONObject jsonSlot = new JSONObject();
        jsonSlot.put("datum", slot.getDatum());
        jsonSlot.put("zeiten", new JSONArray(slot.getZeit()));
        jsonSlot.put("impfzentrum", new JSONArray(slot.getImpfzentrum()));
        jsonSlot.put("gesamt_kapazitaet", slot.getGesamtKapazitaet());
        jsonSlot.put("gebuchte_plaetze", slot.getGebuchtePlaetze());
        jsonSlot.put("verfuegbareSlotIds", slot.getVerfuegbareSlotIds());
        jsonArray.put(jsonSlot);
      }
      System.out.println("Erzeugtes JSON: " + jsonArray.toString());
      out.print(jsonArray.toString());
      out.flush();

    } catch (SQLException e) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response
          .getWriter()
          .write("{\"status\": \"error\", \"message\": \"Fehler beim Laden der Slots.\"}");
    }
  }
  /*
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      response.setStatus(HttpServletResponse.SC_OK);

      String contextPath = request.getContextPath();

       if (contextPath == null || contextPath.isEmpty()) {
      contextPath = "/";
  }

      JSONObject json = new JSONObject();
      json.put("contextPath", contextPath);

      PrintWriter out = response.getWriter();
      out.print(json.toString());
      out.flush();
  }
  */
}
