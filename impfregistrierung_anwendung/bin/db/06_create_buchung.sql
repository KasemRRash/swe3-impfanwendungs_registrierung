CREATE TABLE buchung (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  slot_id INT NOT NULL,
  impfstoff_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
  FOREIGN KEY (slot_id) REFERENCES slot(id) ON DELETE CASCADE,
  FOREIGN KEY (impfstoff_id) REFERENCES impfstoff(id)
);
