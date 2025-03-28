CREATE TABLE impfzentrum (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  kapazitaet INT NOT NULL CHECK (kapazitaet > 0),
  standort VARCHAR(255) NOT NULL,
  UNIQUE (name, standort)
);
