package hbv.web.util;

public interface PasswortService {
  String hashePasswortMitSalt(String passwort);

  boolean passwortVergleichen(String passwort, String gespeichertesPasswort);
}
