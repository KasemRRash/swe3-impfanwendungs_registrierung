package hbv.web.model;

import java.util.List;

public class Slot {
  private String datum;
  // private String zeit;
  private List<String> zeiten;
  private List<String> impfzentrum;

  private List<Integer> gesamtKapazitaet;
  private List<Integer> gebuchtePlaetze;
  private List<Integer> verfuegbareSlotIds;

  public Slot(
      String datum,
      List<String> zeit,
      List<String> impfzentrum,
      List<Integer> gesamtKapazitaet,
      List<Integer> gebuchtePlaetze,
      List<Integer> verfuegbareSlotIds) {
    this.datum = datum;
    this.zeiten = zeit;
    //   this.impfzentren = Arrays.asList(impfzentrumCsv.split(","));
    this.impfzentrum = impfzentrum;
    this.gesamtKapazitaet = gesamtKapazitaet;
    this.gebuchtePlaetze = gebuchtePlaetze;
    this.verfuegbareSlotIds = verfuegbareSlotIds;
  }

  public String getDatum() {
    return datum;
  }

  public List<String> getZeit() {
    return zeiten;
  }

  public List<String> getImpfzentrum() {
    return impfzentrum;
  }

  public List<Integer> getGesamtKapazitaet() {
    return gesamtKapazitaet;
  }

  public List<Integer> getGebuchtePlaetze() {
    return gebuchtePlaetze;
  }

  public List<Integer> getVerfuegbareSlotIds() {
    return verfuegbareSlotIds;
  }
}
