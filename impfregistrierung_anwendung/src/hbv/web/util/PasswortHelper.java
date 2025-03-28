package hbv.web.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HexFormat;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswortHelper implements PasswortService {

  @Override
  public String hashePasswortMitSalt(String passwort) {
    String saltHex = erstelleSalt();
    byte[] saltBytes = HexFormat.of().parseHex(saltHex);
    byte[] hash = hashePasswort(passwort, saltBytes);
    return saltHex + HexFormat.of().formatHex(hash);
  }

  @Override
  public boolean passwortVergleichen(String passwort, String gespeichertesPasswort) {
    String hexSalt = gespeichertesPasswort.substring(0, 16);
    String gespeicherterHash = gespeichertesPasswort.substring(16);
    byte[] salt = HexFormat.of().parseHex(hexSalt);
    byte[] hash = hashePasswort(passwort, salt);
    return HexFormat.of().formatHex(hash).equals(gespeicherterHash);
  }

  private String erstelleSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[8];
    random.nextBytes(salt);
    return HexFormat.of().formatHex(salt);
  }

  private byte[] hashePasswort(String passwort, byte[] salt) {
    try {
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      PBEKeySpec spec = new PBEKeySpec(passwort.toCharArray(), salt, 210000, 512);
      SecretKey key = factory.generateSecret(spec);
      spec.clearPassword();
      return key.getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException("Fehler beim Hashen des Passworts", e);
    }
  }
}
