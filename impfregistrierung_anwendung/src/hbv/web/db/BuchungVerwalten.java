package hbv.web.db;

import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class BuchungVerwalten {

  private DataSource datenQuelle;
  private static volatile BuchungVerwalten instanz;
  private static final Object block = new Object();

  //  private final Object block = new Object();

  private BuchungVerwalten() {
    try {
      InitialContext initCtx = new InitialContext();
      datenQuelle = (DataSource) initCtx.lookup("java:/comp/env/jdbc/mariadb");
    } catch (NamingException e) {
      throw new RuntimeException("Datenbankverbindung fehlgeschlagen", e);
    }
  }

  public static BuchungVerwalten getInstance() {
    if (instanz == null) {
      synchronized (block) {
        if (instanz == null) {
          instanz = new BuchungVerwalten();
        }
      }
    }
    return instanz;
  }

  public int getSlotId(String datum, String zeit, String impfzentrumName) throws SQLException {
    String query =
        "SELECT s.id "
            + "FROM slot s "
            + "JOIN impfzentrum i ON s.impfzentrum_id = i.id "
            + "WHERE s.slot_date = ? AND s.slot_time = ? AND i.name = ?";

    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, datum);
      stmt.setString(2, zeit);
      stmt.setString(3, impfzentrumName);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("id");
      }
    }
    return -1;
  }

  public int buchungHinzufuegen(int userId, int slotId, int impfstoffId) throws SQLException {
    synchronized (block) {
      String countQuery = "SELECT COUNT(*) FROM buchung WHERE user_id = ?";
      String insertQuery = "INSERT INTO buchung (user_id, slot_id, impfstoff_id) VALUES (?, ?, ?)";

      try (Connection connection = datenQuelle.getConnection()) {
        try (PreparedStatement countStmt = connection.prepareStatement(countQuery)) {
          countStmt.setInt(1, userId);
          try (ResultSet rs = countStmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) >= 4) {
              return -1;
            }
          }
        }

        try (PreparedStatement insertStmt =
            connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
          insertStmt.setInt(1, userId);
          insertStmt.setInt(2, slotId);
          insertStmt.setInt(3, impfstoffId);
          insertStmt.executeUpdate();

          try (ResultSet rs = insertStmt.getGeneratedKeys()) {
            if (rs.next()) {
              return rs.getInt(1);
            } else {
              throw new SQLException("Fehler: Konnte keine Booking-ID abrufen.");
            }
          }
        }
      }
    }
  }

  // Termine stornieren ("Buchung löschen")
  public boolean buchungLoeschen(String email, int buchungId) throws SQLException {
    String deleteQuery =
        "DELETE FROM buchung WHERE id = ? AND user_id = (SELECT id FROM user WHERE email = ?)";

    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
      stmt.setInt(1, buchungId);
      stmt.setString(2, email);

      int rowsAffected = stmt.executeUpdate();
      return rowsAffected > 0; // Erfolgreich, wenn mindestens eine Zeile gelöscht wurde
    }
  }

  /*
    public int buchungHinzufuegen(int userId, int slotId, int impfstoffId) throws SQLException {
      synchronized (block) {
        String countQuery = "SELECT COUNT(*) FROM buchung WHERE user_id = ?";
        String insertQuery = "INSERT INTO buchung (user_id, slot_id, impfstoff_id) VALUES (?, ?, ?)";

        try (Connection connection = datenQuelle.getConnection();
            PreparedStatement countStmt = connection.prepareStatement(countQuery)) {
          countStmt.setInt(1, userId);
          ResultSet rs = countStmt.executeQuery();
          if (rs.next() && rs.getInt(1) >= 4) {
            return -1;
          }
        }

        try (Connection connection = datenQuelle.getConnection();
            PreparedStatement insertStmt =
                connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
          insertStmt.setInt(1, userId);
          insertStmt.setInt(2, slotId);
          insertStmt.setInt(3, impfstoffId);
          insertStmt.executeUpdate();
          // return true;

          ResultSet rs = insertStmt.getGeneratedKeys();
          if (rs.next()) {
            return rs.getInt(1); // Die neue `bookingId`
          } else {
            throw new SQLException("Fehler: Konnte keine Booking-ID abrufen.");
          }
        }
      }
    }
  */
  public List<String> getBuchungenByUser(String email, HttpSession session) throws SQLException {
    List<String> buchungen = new ArrayList<>();
    String query =
        "SELECT b.id, b.user_id, b.slot_id, s.slot_date, s.slot_time, "
            + "i.name AS impfstoff_name, z.name AS impfzentrum_name "
            + "FROM buchung b "
            + "JOIN slot s ON b.slot_id = s.id "
            + "JOIN impfzentrum z ON s.impfzentrum_id = z.id "
            + "JOIN impfstoff i ON b.impfstoff_id = i.id "
            + "JOIN user u ON b.user_id = u.id "
            + "WHERE u.email = ?";

    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int buchungId = rs.getInt("id");
        int userId = rs.getInt("user_id");
        int slotId = rs.getInt("slot_id");
        String slotDate = rs.getString("slot_date");
        String slotTime = rs.getString("slot_time");
        String impfstoff = rs.getString("impfstoff_name");
        String impfzentrum = rs.getString("impfzentrum_name");

        buchungen.add(
            "{ \"id\": "
                + buchungId
                + ", \"datum\": \""
                + slotDate
                + "\""
                + ", \"zeit\": \""
                + slotTime
                + "\""
                + ", \"impfstoff\": \""
                + impfstoff
                + "\""
                + ", \"impfzentrum\": \""
                + impfzentrum
                + "\""
                + " }");
      }
    }
    return buchungen;
  }

  /*
  public List<String> getBuchungenByUser(String email, HttpSession session) throws SQLException {
      List<String> buchungen = new ArrayList<>();
      String query = "SELECT b.id, b.user_id, b.slot_id, s.slot_date, s.slot_time,
      i.name AS impfstoff, " + "z.name AS impfzentrum " +
        "FROM buchung b " +
                     "JOIN slot s ON b.slot_id = s.id " +
                     "JOIN impfzentrum z ON s.impfzentrum_id = z.id " +
                     "JOIN impfstoff i ON b.impfstoff_id = i.id " +
                     "JOIN user u ON b.user_id = u.id " +
                     "WHERE u.email = ?";

      try (Connection connection = datenQuelle.getConnection();
           PreparedStatement stmt = connection.prepareStatement(query)) {
          stmt.setString(1, email);
          ResultSet rs = stmt.executeQuery();
          while (rs.next()) {
              int buchungId = rs.getInt("id");
              int userId = rs.getInt("user_id");
              int slotId = rs.getInt("slot_id");
              String slotDate = rs.getString("slot_date");
              String slotTime = rs.getString("slot_time");
              String impfstoff = rs.getString("name");
              //String impfzentrum = (String) session.getAttribute("buchung_" + userId + "_" + slotId);
              String impfzentrum = rs.getString("impfzentrum");

              buchungen.add("{ \"id\": " + buchungId +
                            ", \"datum\": \"" + slotDate + "\"" +
                            ", \"zeit\": \"" + slotTime + "\"" +
                            ", \"impfstoff\": \"" + impfstoff + "\"" +
                            ", \"impfzentrum\": \"" + impfzentrum + "\"" + " }");
          }
      }
      return buchungen;
  }
  */

  /*
  public List<String> getBuchungenByUser(String email) throws SQLException {
    List<String> buchungen = new ArrayList<>();


    String query = "SELECT b.id, s.slot_date, s.slot_time, i.name FROM buchung b " +
      "JOIN slot s ON b.slot_id = s.id " +
      "JOIN impfstoff i ON b.impfstoff_id = i.id " +
      "JOIN user u ON b.user_id = u.id " +
      "WHERE u.email = ?";

    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        buchungen.add("{ \"id\": " + rs.getInt("id") +
            ", \"datum\": \"" + rs.getString("slot_date") + "\"" +
            ", \"zeit\": \"" + rs.getString("slot_time") + "\"" +
            ", \"impfstoff\": \"" + rs.getString("name") + "\"" + " }");
      }
        }
    return buchungen;
  }
  */

}
