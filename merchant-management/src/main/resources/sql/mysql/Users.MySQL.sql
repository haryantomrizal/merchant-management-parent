DROP TABLE IF EXISTS user_roles CASCADE CONSTRAINT;
DROP TABLE IF EXISTS users CASCADE CONSTRAINT;

CREATE  TABLE users (
  username VARCHAR(45) NOT NULL,
  password VARCHAR(60) NOT NULL,
  name VARCHAR(60),
  last_login DATE,
  create_by VARCHAR(45),
	create_date DATE DEFAULT CURRENT_TIMESTAMP,
  enabled CHAR(1) DEFAULT 'T'
);
ALTER TABLE users ADD CONSTRAINT pkUsers PRIMARY KEY (username); 

DROP SEQUENCE seqUserRoleId;
CREATE SEQUENCE seqUserRoleId;
CREATE TABLE user_roles (
  user_role_id NUMBER(11),
  username VARCHAR(45) NOT NULL,
  role VARCHAR(45) NOT NULL
);
ALTER TABLE user_roles ADD CONSTRAINT pkUserRoles PRIMARY KEY (user_role_id);
ALTER TABLE user_roles ADD CONSTRAINT unqUserRoles UNIQUE (role,username);
ALTER TABLE user_roles ADD CONSTRAINT fkUserRoles_username FOREIGN KEY (username) REFERENCES users (username);
CREATE INDEX idxUserRoles_username ON user_roles (username);

INSERT INTO users(username,password,enabled)
VALUES ('admin','$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', 'T');
INSERT INTO users(username,password,enabled)
VALUES ('user','$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', 'T');

INSERT INTO user_roles (user_role_id, username, role) VALUES (seqUserRoleId.NEXTVAL, 'admin', 'USER');
INSERT INTO user_roles (user_role_id, username, role) VALUES (seqUserRoleId.NEXTVAL, 'admin', 'ADMIN');
INSERT INTO user_roles (user_role_id, username, role) VALUES (seqUserRoleId.NEXTVAL, 'user', 'USER');