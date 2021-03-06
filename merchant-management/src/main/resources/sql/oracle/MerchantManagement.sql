DROP TABLE USER_ROLES CASCADE CONSTRAINT;
DROP TABLE USERS CASCADE CONSTRAINT;
DROP TABLE ROLE_PERMISSIONS CASCADE CONSTRAINT;
DROP TABLE ROLES CASCADE CONSTRAINT;
DROP TABLE PERMISSIONS CASCADE CONSTRAINT;

CREATE TABLE PERMISSIONS (
	ID					CHAR(32)		NOT NULL,
	NAME				VARCHAR2(100),
	HTTP_PATH			VARCHAR2(150),
	HTTP_METHOD			VARCHAR2(10),
	ICON_FILE			VARCHAR2(155),
	AS_MENU				CHAR(1)			DEFAULT 'N',
	MENU_ORDER			NUMBER(2)		DEFAULT 0,
	PARENT_MENU			CHAR(32),
	DELETED				NUMBER(1)		DEFAULT 0,
	CREATE_BY			VARCHAR2(30),
	CREATE_DATE			DATE,
	LAST_UPDATE_BY		VARCHAR2(30),
	LAST_UPDATE_DATE	DATE,
	DESCRIPTION			VARCHAR2(255)
);

CREATE TABLE ROLES (
	NAME				VARCHAR2(30)	NOT NULL,
	DELETED				NUMBER(1)		DEFAULT 0,
	CREATE_BY			VARCHAR2(30),
	CREATE_DATE			DATE,
	LAST_UPDATE_BY		VARCHAR2(30),
	LAST_UPDATE_DATE	DATE,
	DESCRIPTION			VARCHAR2(255)
);

CREATE TABLE ROLE_PERMISSIONS (
	ROLE_NAME		VARCHAR2(30),
	PERMISSION_ID	CHAR(32)
);

CREATE TABLE USERS (
    ID						NUMBER			NOT NULL,
	USER_NAME				VARCHAR2(30)	NOT NULL,
	FIRST_NAME				VARCHAR2(255),
	LAST_NAME				VARCHAR2(255),
	PASSWORD				VARCHAR2(150),
	EMAIL					VARCHAR2(255),
	PHONE					VARCHAR2(100),
	PROFILE_PICTURE			VARCHAR2(255),
	PICTURE					BLOB,
	LAST_LOGIN				TIMESTAMP,
	LAST_LOGIN_ADDRESS		VARCHAR2(30),
	ENABLED					NUMBER(1)		DEFAULT 1,
	ACCOUNT_NON_EXPIRED		NUMBER(1)		DEFAULT 1,
	ACCOUNT_EXPIRY_DATE		DATE,
	CREDENTIALS_NON_EXPIRED	NUMBER(1)		DEFAULT 1,
	CREDENTIALS_EXPIRY_DATE	DATE,
	ACCOUNT_NON_LOCKED		NUMBER(1)		DEFAULT 1,
	DELETED					NUMBER(1)		DEFAULT 0,
	CREATE_BY				VARCHAR2(30),
	CREATE_DATE				DATE,
	LAST_UPDATE_BY			VARCHAR2(30),
	LAST_UPDATE_DATE		DATE
);

CREATE TABLE USER_ROLES (
	USER_ID		NUMBER,
	ROLE_NAME	VARCHAR2(30)
);

ALTER TABLE PERMISSIONS ADD (
	CONSTRAINT PK_PERMISSION PRIMARY KEY (ID)
);

ALTER TABLE ROLES ADD (
	CONSTRAINT PK_ROLE PRIMARY KEY (NAME)
);

ALTER TABLE ROLE_PERMISSIONS ADD (
	CONSTRAINT PK_ROLE_PERMISSIONS PRIMARY KEY (ROLE_NAME, PERMISSION_ID)
);

ALTER TABLE USERS ADD (
	CONSTRAINT PK_USERS PRIMARY KEY (ID),
	CONSTRAINT UNQ_USERS1 UNIQUE(USER_NAME),
	CONSTRAINT UNQ_USERS2 UNIQUE(EMAIL)
);

ALTER TABLE USER_ROLES ADD (
	CONSTRAINT PK_USER_ROLES PRIMARY KEY (USER_ID, ROLE_NAME)
);

INSERT INTO users (id, user_name, first_name, email, password, enabled, account_non_expired, credentials_non_expired, account_non_locked, deleted, create_by, last_update_by)
VALUES (1, 'admin', 'Administrator', 'admin@muamaltbank.com', '$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', 1,1,1,1,0, 'SYSTEM', 'SYSTEM');
INSERT INTO users (id, user_name, first_name, email, password, enabled, account_non_expired, credentials_non_expired, account_non_locked, deleted, create_by, last_update_by)
VALUES (2, 'user', 'User', 'user@muamalatbank.com', '$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y', 1,1,1,1,0, 'SYSTEM', 'SYSTEM');

INSERT INTO roles(name, deleted, create_by, last_update_by) VALUES ('ADMIN', 0, 'SYSTEM', 'SYSTEM');
INSERT INTO roles(name, deleted, create_by, last_update_by) VALUES ('USER', 0, 'SYSTEM', 'SYSTEM');

INSERT INTO user_roles(user_id, role_name) VALUES (1,'ADMIN');
INSERT INTO user_roles(user_id, role_name) VALUES (1,'USER');
INSERT INTO user_roles(user_id, role_name) VALUES (2,'USER');