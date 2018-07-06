--// BBB-206-create-message-table
-- Migration SQL that makes the change goes here.

CREATE TABLE message(
  uuid UUID NOT NULL,
  template VARCHAR (150),
  created_on TIMESTAMP DEFAULT now(),
  PRIMARY KEY (uuid)
);


--//@UNDO
-- SQL to undo the change goes here.

DROP TABLE message;

