package hbv.web.service;

import hbv.web.db.UserVerwalten;
import hbv.web.model.User;
import hbv.web.util.PasswortService;
import java.sql.SQLException;

public class UserHelper {
  private final UserVerwalten userVerwalten;
  private final PasswortService passwortService;
  private static final Object block = new Object();

  public UserHelper(UserVerwalten userVerwalten, PasswortService passwortService) {
    this.userVerwalten = userVerwalten;
    this.passwortService = passwortService;
  }

  public boolean registriereUser(String email, String passwort) throws SQLException {
    synchronized (block) {
      if (userVerwalten.existiertUser(email)) {
        return false;
      }

      String passwortHash = passwortService.hashePasswortMitSalt(passwort);
      return userVerwalten.speichereUser(new User(email, passwortHash, false));
    }
  }

  public boolean authentifiziereUser(String email, String passwort) throws SQLException {
    User user = userVerwalten.findeUser(email);
    if (user == null) {
      return false;
    }
    return passwortService.passwortVergleichen(passwort, user.getPasswortHash());
  }
}
