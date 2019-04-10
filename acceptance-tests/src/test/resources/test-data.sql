SET search_path TO message;


SET search_path TO usermanagement;

DELETE FROM usermanagement.email_link WHERE user_id between -3510 and -3500;
DELETE FROM usermanagement.users WHERE id between -3510 and -3500;
INSERT INTO usermanagement.users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid, is_active, login_fail_count) VALUES
  (-3501, 'Danger Mouse', 'ms_SHROP@la.admins.dft.gov.uk', 'SHROP', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '6c004c3d-926e-457a-9f0a-a09bf42731f7'::UUID, TRUE, 0)
  ,(-3502, 'Baron Greenback', 'ms_SHROP@la.editors.dft.gov.uk', 'SHROP', 3, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '2084c983-dfcb-4166-93f5-74aaa7398e29'::UUID, TRUE, 0)
;
