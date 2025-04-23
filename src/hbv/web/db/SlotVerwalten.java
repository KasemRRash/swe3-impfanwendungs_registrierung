package hbv.web.db;

import hbv.web.model.Slot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SlotVerwalten {

  private DataSource datenQuelle;
  private static SlotVerwalten instanz;

  private SlotVerwalten() {
    try {
      InitialContext initCtx = new InitialContext();
      datenQuelle = (DataSource) initCtx.lookup("java:/comp/env/jdbc/mariadb");
    } catch (NamingException e) {
      throw new RuntimeException("Datenbankverbindung fehlgeschlagen", e);
    }
  }

  public static synchronized SlotVerwalten getInstance() {
    if (instanz == null) {
      instanz = new SlotVerwalten();
    }
    return instanz;
  }

  public List<Slot> getVerfuegbareSlots() throws SQLException {

    List<Slot> slots = new ArrayList<>();
    String query =
        "SELECT   s.slot_date AS datum,   GROUP_CONCAT(DISTINCT TIME_FORMAT(s.slot_time, '%H:%i')"
            + " ORDER BY s.slot_time SEPARATOR ', ') AS zeit,   GROUP_CONCAT(i.name ORDER BY i.id "
            + " SEPARATOR ', ') AS impfzentren,   GROUP_CONCAT(i.kapazitaet ORDER BY i.id SEPARATOR"
            + " ', ') AS gesamt_kapazitaet,   GROUP_CONCAT(IFNULL(b.count_buchung, 0) ORDER BY i.id"
            + " SEPARATOR ', ') AS gebuchte_plaetze,   GROUP_CONCAT(DISTINCT s.id ORDER BY i.id"
            + " SEPARATOR ', ') AS slot_ids FROM slot s JOIN impfzentrum i ON s.impfzentrum_id ="
            + " i.id LEFT JOIN (     SELECT slot_id, COUNT(*) AS count_buchung     FROM buchung    "
            + " GROUP BY slot_id ) b ON s.id = b.slot_id GROUP BY s.slot_date, s.slot_time ORDER BY"
            + " s.slot_date, s.slot_time;";

    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        String datum = rs.getString("datum");
        List<String> zeitList = Arrays.asList(rs.getString("zeit").split(",\\s*"));
        String impfzentrenStr = rs.getString("impfzentren");
        String gesamtKapazitaetStr = rs.getString("gesamt_kapazitaet");
        String gebuchtePlaetzeStr = rs.getString("gebuchte_plaetze");
        String slotIdsStr = rs.getString("slot_ids");

        List<String> impfzentrenList = Arrays.asList(impfzentrenStr.split(",\\s*"));
        List<String> gesamtKapazitaetList = Arrays.asList(gesamtKapazitaetStr.split(",\\s*"));
        List<String> gebuchtePlaetzeList = Arrays.asList(gebuchtePlaetzeStr.split(",\\s*"));
        List<String> slotIdsList = Arrays.asList(slotIdsStr.split(",\\s*"));
        // System.out.println("impfzentrenList: " + impfzentrenList);
        // System.out.println("gesamtKapazitaetList: " + gesamtKapazitaetList);
        // System.out.println("gebuchtePlaetzeList: " + gebuchtePlaetzeList);
        // System.out.println("slotIdsList: " + slotIdsList);

        List<String> verfuegbareSlotIds = new ArrayList<>();
        List<String> verfuegbareImpfzentren = new ArrayList<>();
        for (int i = 0; i < gesamtKapazitaetList.size(); i++) {
          try {
            int kapazitaet = Integer.parseInt(gesamtKapazitaetList.get(i).trim());
            int buchungen = Integer.parseInt(gebuchtePlaetzeList.get(i).trim());
            if (kapazitaet > buchungen) {
              verfuegbareSlotIds.add(slotIdsList.get(i).trim());
              verfuegbareImpfzentren.add(impfzentrenList.get(i).trim());
              //        System.out.println("Slot ID " + slotIdsList.get(i).trim() + " ist
              // verfügbar.");
              //       System.out.println("Impfzentrum " +impfzentrenList.get(i).trim() + " ist
              // verfuegbar.");
            }
          } catch (NumberFormatException e) {
            continue;
          }
        }

        if (verfuegbareSlotIds.isEmpty() | verfuegbareImpfzentren.isEmpty()) {
          continue;
        }

        List<Integer> gesamtKapazitaetInt = new ArrayList<>();
        List<Integer> gebuchtePlaetzeInt = new ArrayList<>();
        List<Integer> verfuegbareSlotIdsInt = new ArrayList<>();
        for (String gk : gesamtKapazitaetList) {
          gesamtKapazitaetInt.add(Integer.parseInt(gk));
        }
        for (String gp : gebuchtePlaetzeList) {
          gebuchtePlaetzeInt.add(Integer.parseInt(gp));
        }
        for (String vs : verfuegbareSlotIds) {
          verfuegbareSlotIdsInt.add(Integer.parseInt(vs));
        }
        Slot slot =
            new Slot(
                datum,
                zeitList,
                verfuegbareImpfzentren,
                gesamtKapazitaetInt,
                gebuchtePlaetzeInt,
                verfuegbareSlotIdsInt);
        slots.add(slot);
        //       System.out.println("Gesamtkapazität: " + gesamtKapazitaetInt);
        // System.out.println("Gebuchte Plätze: " + gebuchtePlaetzeInt);
        // System.out.println("Verfügbare Slot-IDs: " + verfuegbareSlotIdsInt);
        // System.out.println("Verfuegbare Impfzentren: " + verfuegbareImpfzentren);

      }
    }
    return slots;
  }
}
