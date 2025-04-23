package hbv.web.model;

public class Impfzentrum {
  private int id;
  private String name;
  private int kapazitaet;
  private String standort;

  public Impfzentrum(int id, String name, int kapazitaet, String standort) {
    this.id = id;
    this.name = name;
    this.kapazitaet = kapazitaet;
    this.standort = standort;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getKapazitaet() {
    return kapazitaet;
  }

  public String getStandort() {
    return standort;
  }
}
