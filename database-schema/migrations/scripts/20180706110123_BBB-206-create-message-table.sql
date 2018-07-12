--// BBB-206-create-message-table
-- Migration SQL that makes the change goes here.
DROP TABLE IF EXISTS message.password_reset_email;

CREATE TABLE message.message(
  bbb_reference UUID NOT NULL,
  template VARCHAR (150),
  notify_reference UUID NOT NULL,
  created_on TIMESTAMP DEFAULT now(),
  PRIMARY KEY (bbb_reference)
);


--//@UNDO
-- SQL to undo the change goes here.

DROP TABLE message.message;

