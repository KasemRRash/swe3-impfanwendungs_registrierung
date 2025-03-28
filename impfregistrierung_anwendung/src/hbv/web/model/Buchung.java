package hbv.web.model;

public class Buchung {
  private int id;
  private int userId;
  private int slotId;
  private int impfstoffId;

  public Buchung(int userId, int slotId, int impfstoffId) {
    this.userId = userId;
    this.slotId = slotId;
    this.impfstoffId = impfstoffId;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public int getSlotId() {
    return slotId;
  }

  public int getImpfstoffId() {
    return impfstoffId;
  }

  public void setId(int id) {
    this.id = id;
  }
}
