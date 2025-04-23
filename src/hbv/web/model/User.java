package hbv.web.model;

public class User {
  private String memail;
  private String mpasswortHash;
  private boolean mistMitarbeiter;

  public User(String email, String passwortHash, boolean istMitarbeiter) {
    memail = email;
    mpasswortHash = passwortHash;
    mistMitarbeiter = istMitarbeiter;
  }

  public String getEmail() {
    return memail;
  }

  public String getPasswortHash() {
    return mpasswortHash;
  }

  public boolean getIstMitarbeiter() {
    return mistMitarbeiter;
  }
}
