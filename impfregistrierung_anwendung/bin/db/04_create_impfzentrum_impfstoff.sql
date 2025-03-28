CREATE TABLE impfzentrum_impfstoff (
  impfzentrum_id INT NOT NULL,
  impfstoff_id INT NOT NULL,
  PRIMARY KEY (impfzentrum_id, impfstoff_id),
  FOREIGN KEY (impfzentrum_id) REFERENCES impfzentrum(id) ON DELETE CASCADE,
  FOREIGN KEY (impfstoff_id) REFERENCES impfstoff(id) ON DELETE CASCADE
);
