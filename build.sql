CREATE DATABASE form_team_talent;

USE form_team_talent;

DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    u_id VARCHAR(255),
    u_name VARCHAR(255),
    u_stu_num VARCHAR(64),
    u_school VARCHAR(255),
    PRIMARY KEY(u_id)
);

DROP TABLE IF EXISTS t_activity;
CREATE TABLE t_activity (
    a_id VARCHAR(255),
    a_name VARCHAR(255) NOT NULL,
    a_holder_id VARCHAR(255) NOT NULL,
    a_desc VARCHAR(1024),
    a_end_date VARCHAR(128),
    a_is_public INT,
    a_qrcode_path VARCHAR(1024),
    PRIMARY KEY(a_id),
    FOREIGN KEY (a_holder_id) REFERENCES t_user(u_id)
);

DROP TABLE IF EXISTS t_team;
CREATE TABLE t_team (
    t_id VARCHAR(255),
    a_id VARCHAR(255) NOT NULL,
    t_name VARCHAR(255) NOT NULL,
    t_leader_id VARCHAR(255) NOT NULL,
    t_desc VARCHAR(1024),
    PRIMARY KEY(t_id),
    FOREIGN KEY (t_leader_id) REFERENCES t_user(u_id),
    FOREIGN KEY (a_id) REFERENCES t_activity(a_id)
);

DROP TABLE IF EXISTS t_uat;
CREATE TABLE t_uat (
   id BIGINT AUTO_INCREMENT,
   u_id VARCHAR(255) NOT NULL,
   a_id VARCHAR(255) NOT NULL,
   t_id VARCHAR(255) NOT NULL,
   PRIMARY KEY (id),
   FOREIGN KEY (u_id) REFERENCES t_user(u_id),
   FOREIGN KEY (a_id) REFERENCES t_activity(a_id),
   FOREIGN KEY (t_id) REFERENCES t_team(t_id)
);

ALTER DATABASE form_team_talent DEFAULT CHAR SET utf8;
ALTER TABLE t_user CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE t_team CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE t_activity CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE t_uat CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;