# Used to backup players wallets, possibly add more later
CREATE TABLE Players
(
  user_id  INTEGER     NOT NULL AUTO_INCREMENT,
  password VARCHAR(50) NOT NULL,
  wallet   DOUBLE      NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE Statistics (
  id          INTEGER NOT NULL AUTO_INCREMENT,
  wins        INTEGER,
  loses       INTEGER,
  largest_win INTEGER,
  user_id     INTEGER NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES Players (user_id)
    ON DELETE CASCADE
);

