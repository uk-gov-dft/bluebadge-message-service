--// BBB-184-add***REMOVED***-link
-- Migration SQL that makes the change goes here.

CREATE TABLE password_reset_email(
  user_id INTEGER,
  uuid UUID NOT NULL,
  created_on TIMESTAMP DEFAULT now(),
  PRIMARY KEY (uuid)
);

COMMENT ON TABLE  ***REMOVED***;
COMMENT ON COLUMN  ***REMOVED***;
COMMENT ON COLUMN  ***REMOVED***;
COMMENT ON COLUMN  ***REMOVED***;




--//@UNDO
-- SQL to undo the change goes here.
DROP TABLE email_link;

