package hbv.web.db;

import hbv.web.model.Impfstoff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ImpfstoffVerwalten {

  private static DataSource getDataSource() throws NamingException {
    Context initCtx = new InitialContext();
    return (DataSource) initCtx.lookup("java:/comp/env/jdbc/mariadb");
  }

  public static List<Impfstoff> getAlleImpfstoffe() {
    List<Impfstoff> impfstoffe = new ArrayList<>();
    try (Connection conn = getDataSource().getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, name FROM impfstoff")) {

      while (rs.next()) {
        impfstoffe.add(new Impfstoff(rs.getInt("id"), rs.getString("name")));
      }
    } catch (SQLException | NamingException e) {
      e.printStackTrace();
    }
    return impfstoffe;
  }

  public static List<Impfstoff> getImpfstoffeFuerImpfzentren(List<String> impfzentren) {
    List<Impfstoff> impfstoffe = new ArrayList<>();
    if (impfzentren.isEmpty()) {
      return impfstoffe;
    }

    try (Connection conn = getDataSource().getConnection()) {
      String placeholders = String.join(",", Collections.nCopies(impfzentren.size(), "?"));

      PreparedStatement stmt =
          conn.prepareStatement(
              "SELECT DISTINCT impfstoff.id, impfstoff.name "
                  + "FROM impfstoff "
                  + "JOIN impfzentrum_impfstoff ii ON impfstoff.id = ii.impfstoff_id "
                  + "JOIN impfzentrum i ON ii.impfzentrum_id = i.id "
                  + "WHERE i.name IN ("
                  + placeholders
                  + ")");

      for (int i = 0; i < impfzentren.size(); i++) {
        stmt.setString(i + 1, impfzentren.get(i));
      }

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        impfstoffe.add(new Impfstoff(rs.getInt("id"), rs.getString("name")));
      }
    } catch (SQLException | NamingException e) {
      e.printStackTrace();
    }
    return impfstoffe;
  }
}
