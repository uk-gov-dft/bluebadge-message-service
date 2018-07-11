--// BBB-206-create-message-table
-- Migration SQL that makes the change goes here.

CREATE TABLE message(
  bbb_reference UUID NOT NULL,
  template VARCHAR (150),
  notify_reference UUID NOT NULL,
  created_on TIMESTAMP DEFAULT now(),
  PRIMARY KEY (bbb_reference)
);


--//@UNDO
-- SQL to undo the change goes here.

DROP TABLE message;

