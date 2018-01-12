# Used to backup players wallets, possibly add more later
CREATE TABLE Players
(
  user_id INTEGER NOT NULL AUTO_INCREMENT,
  wallet  DOUBLE NOT NULL,
  PRIMARY KEY (user_id)
);

