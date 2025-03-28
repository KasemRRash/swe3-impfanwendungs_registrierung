package hbv.web.model;

public class BuchungSlot {
  private String datum;
  private String zeitslot;
  private int anzahl;
  private int kapazitaet;

  public BuchungSlot(String datum, String zeitslot, int anzahl, int kapazitaet) {
    this.datum = datum;
    this.zeitslot = zeitslot;
    this.anzahl = anzahl;
    this.kapazitaet = kapazitaet;
  }

  public String getDatum() {
    return datum;
  }

  public String getZeitslot() {
    return zeitslot;
  }

  public int getAnzahl() {
    return anzahl;
  }

  public int getKapazitaet() {
    return kapazitaet;
  }
}
