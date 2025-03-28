package hbv.web.db;

import hbv.web.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UserVerwalten {

  private DataSource datenQuelle;
  private static UserVerwalten instanz;

  private UserVerwalten() {
    try {
      InitialContext initCtx = new InitialContext();
      datenQuelle = (DataSource) initCtx.lookup("java:/comp/env/jdbc/mariadb");
    } catch (NamingException e) {
      throw new RuntimeException("Datenbankverbindung fehlgeschlagen", e);
    }
  }

  public static synchronized UserVerwalten getInstance() {
    if (instanz == null) {
      instanz = new UserVerwalten();
    }
    return instanz;
  }

  public boolean existiertUser(String email) throws SQLException {
    String query = "SELECT COUNT(*) FROM user WHERE email = ?";
    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      return rs.next() && rs.getInt(1) > 0;
    }
  }

  public User findeUser(String email) throws SQLException {
    String query = "SELECT email, password, istMitarbeiter FROM user WHERE email = ?";
    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new User(
            rs.getString("email"), rs.getString("password"), rs.getBoolean("istMitarbeiter"));
      }
    }
    return null;
  }

  public boolean speichereUser(User user) throws SQLException {
    String insertQuery = "INSERT INTO user (email, password) VALUES (?, ?)";

    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
      stmt.setString(1, user.getEmail());
      stmt.setString(2, user.getPasswortHash());
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public int getUserIdByEmail(String email) throws SQLException {
    String query = "SELECT id FROM user WHERE email = ?";
    try (Connection connection = datenQuelle.getConnection();
        PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, email);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt("id");
      }
    }
    return -1; // User nicht gefunden
  }
  //  public boolean istMitarbeiter(String email) throws SQLException {
  //  String sql = "SELECT istMitarbeiter FROM user WHERE email = ?";
  //  try (Connection conn = DatabaseConnection.getConnection();
  //            PreparedStatement stmt = conn.prepareStatement(sql)) {
  //            stmt.setString(1, email);
  //            try (ResultSet rs = stmt.executeQuery()) {
  //                if (rs.next()) {
  //                    return rs.getBoolean("istMitarbeiter");
  //                }
  //            }
  //        }
  //        return false;
  //    }

}
