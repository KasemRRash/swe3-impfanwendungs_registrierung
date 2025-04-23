package hbv.web.util;

/*
 * Interface für den Mail-Versand
 * - Dieses Interface legt eine einheitliche Methode sendMail() fest.
 * - Jede Klasse, die E-Mails versenden soll, kann dieses Interface implementieren
 * - Dadurch können wir flexibel zwischen verschiedenen Mail-Diensten wechseln
 */
public interface MailService {

  /**
   * Sendet eine E-Mail mit PDF
   *
   * @param email Die E-Mail-Adresse des Empfängers
   * @param pdfPath Der Dateipfad zur PDF-Datei, die als Anhang versendet wird
   * @param bookingId Die Buchungs-ID, die sich auf den Termin bezieht
   */
  void sendMail(String email, String pdfPath, String bookingId);
}
