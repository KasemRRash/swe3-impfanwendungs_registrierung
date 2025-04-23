CREATE TABLE slot (
  id INT PRIMARY KEY AUTO_INCREMENT,
  slot_date DATE NOT NULL,
  slot_time TIME NOT NULL,
  impfzentrum_id INT NOT NULL,
  FOREIGN KEY (impfzentrum_id) REFERENCES impfzentrum(id) ON DELETE CASCADE,
  UNIQUE (slot_date, slot_time, impfzentrum_id)
);
