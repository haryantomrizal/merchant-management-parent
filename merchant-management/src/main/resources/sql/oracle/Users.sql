DROP TABLE user_roles CASCADE CONSTRAINT;
DROP TABLE users CASCADE CONSTRAINT;

DROP SEQUENCE seq_user_roles;


CREATE  TABLE users (
  username VARCHAR(45) NOT NULL,
  password VARCHAR(60) NOT NULL,
  name VARCHAR(60),
  last_login DATE,
  create_by VARCHAR(45),
  create_date DATE DEFAULT CURRENT_TIMESTAMP,
  enabled CHAR(1) DEFAULT 'T'
);
ALTER TABLE users ADD CONSTRAINT pk_users PRIMARY KEY (username); 

CREATE SEQUENCE seq_user_roles;
CREATE TABLE user_roles (
  user_role_id NUMBER(11),
  username VARCHAR(45) NOT NULL,
  role VARCHAR(45) NOT NULL
);
ALTER TABLE user_roles ADD CONSTRAINT pk_user_roles PRIMARY KEY (user_role_id);
ALTER TABLE user_roles ADD CONSTRAINT unq_user_roles UNIQUE (role,username);
ALTER TABLE user_roles ADD CONSTRAINT fk_user_roles_username FOREIGN KEY (username) REFERENCES users (username);
CREATE INDEX idx_user_roles_username ON user_roles (username);

INSERT INTO users(username,password,enabled)
VALUES ('admin','$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', 'T');
INSERT INTO users(username,password,enabled)
VALUES ('user','$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', 'T');

INSERT INTO user_roles (user_role_id, username, role) VALUES (seq_user_roles.NEXTVAL-1, 'admin', 'USER');
INSERT INTO user_roles (user_role_id, username, role) VALUES (seq_user_roles.NEXTVAL-1, 'admin', 'ADMIN');
INSERT INTO user_roles (user_role_id, username, role) VALUES (seq_user_roles.NEXTVAL-1, 'user', 'USER');