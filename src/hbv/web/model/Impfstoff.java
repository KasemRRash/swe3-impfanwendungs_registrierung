package hbv.web.model;

public class Impfstoff {
  private int id;
  private String name;

  public Impfstoff(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
