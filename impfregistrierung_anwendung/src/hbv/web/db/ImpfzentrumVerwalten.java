package hbv.web.db;

import hbv.web.model.BuchungSlot;
import hbv.web.model.Impfzentrum;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ImpfzentrumVerwalten {
  private static ImpfzentrumVerwalten instance;

  public static ImpfzentrumVerwalten getInstance() {
    if (instance == null) {
      instance = new ImpfzentrumVerwalten();
    }
    return instance;
  }

  private static DataSource getDataSource() throws NamingException {
    Context initCtx = new InitialContext();
    return (DataSource) initCtx.lookup("java:/comp/env/jdbc/mariadb");
  }

  public List<Impfzentrum> getAlleImpfzentren() {
    List<Impfzentrum> impfzentren = new ArrayList<>();
    try (Connection conn = getDataSource().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs =
            stmt.executeQuery("select id, name, kapazitaet, standort from impfzentrum")) {

      while (rs.next()) {
        impfzentren.add(
            new Impfzentrum(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("kapazitaet"),
                rs.getString("standort")));
      }

    } catch (SQLException | NamingException e) {
      e.printStackTrace();
    }
    return impfzentren;
  }

  public static List<BuchungSlot> getBuchungenFuerImpfzentrum(String impfzentrum) {
    List<BuchungSlot> buchungen = new ArrayList<>();
    String sql =
        "SELECT s.slot_date AS datum, s.slot_time AS zeitslot, COUNT(b.id) AS anzahl, z.kapazitaet"
            + " AS kapazitaet FROM buchung b JOIN slot s ON b.slot_id = s.id JOIN impfzentrum z ON"
            + " s.impfzentrum_id = z.id WHERE z.name = ? GROUP BY s.slot_date, s.slot_time ORDER BY"
            + " s.slot_date, s.slot_time";

    try (Connection conn = getDataSource().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, impfzentrum);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        buchungen.add(
            new BuchungSlot(
                rs.getString("datum"),
                rs.getString("zeitslot"),
                rs.getInt("anzahl"),
                rs.getInt("kapazitaet")));
      }
    } catch (SQLException | NamingException e) {
      e.printStackTrace();
    }
    return buchungen;
  }
}
