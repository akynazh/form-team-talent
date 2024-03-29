DROP DATABASE IF EXISTS form_team_talent;
CREATE DATABASE form_team_talent;
ALTER DATABASE form_team_talent DEFAULT CHAR SET utf8;

USE form_team_talent;

DROP TABLE IF EXISTS t_req;
DROP TABLE IF EXISTS t_uat;
DROP TABLE IF EXISTS t_team;
DROP TABLE IF EXISTS t_activity;
DROP TABLE IF EXISTS t_user;

DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    u_id VARCHAR(255),
    u_pwd VARCHAR(255),
    u_name VARCHAR(255),
    u_stu_num VARCHAR(64),
    u_school VARCHAR(255),
    u_major VARCHAR(255),
    u_sex VARCHAR(32),
    PRIMARY KEY(u_id)
);
ALTER TABLE t_user CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP TABLE IF EXISTS t_activity;
CREATE TABLE t_activity (
    a_id VARCHAR(255),
    a_name VARCHAR(255) NOT NULL,
    a_holder_id VARCHAR(255) NOT NULL,
    a_desc VARCHAR(1024),
    a_end_date VARCHAR(128),
    a_is_public INT,
    a_qrcode_path VARCHAR(1024),
    status INT,
    PRIMARY KEY(a_id),
    FOREIGN KEY (a_holder_id) REFERENCES t_user(u_id)
);
ALTER TABLE t_activity CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
-- CREATE INDEX t_activity_idx_get ON t_activity(a_holder_id, a_id);
CREATE INDEX t_activity_idx_get_pub ON t_activity(a_is_public, status);

DROP TABLE IF EXISTS t_team;
CREATE TABLE t_team (
    t_id VARCHAR(255),
    a_id VARCHAR(255) NOT NULL,
    t_name VARCHAR(255) NOT NULL,
    t_leader_id VARCHAR(255) NOT NULL,
    t_desc VARCHAR(1024),
    t_total INT,
    t_count INT,
    PRIMARY KEY(t_id),
    FOREIGN KEY (t_leader_id) REFERENCES t_user(u_id),
    FOREIGN KEY (a_id) REFERENCES t_activity(a_id)
);
ALTER TABLE t_team CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE INDEX t_team_idx_a_id ON t_team(a_id);

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
ALTER TABLE t_uat CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE INDEX t_uat_idx_join ON t_uat(a_id, u_id);
CREATE INDEX  t_uat_idx_t_id ON t_uat(u_id);

DROP TABLE IF EXISTS t_req;
CREATE TABLE t_req (
    id VARCHAR(255),
    from_id VARCHAR(255) NOT NULL,
    to_id VARCHAR(255) NOT NULL,
    a_id VARCHAR(255) NOT NULL,
    t_id VARCHAR(255) NOT NULL,
    t_name VARCHAR(255) NOT NULL,
    u_name VARCHAR(255) NOT NULL,
    content VARCHAR(255),
    send_date VARCHAR(128),
    agree INT,
    status INT,
    PRIMARY KEY (id),
    FOREIGN KEY (from_id) REFERENCES t_user(u_id),
    FOREIGN KEY (to_id) REFERENCES t_user(u_id),
    FOREIGN KEY (a_id) REFERENCES t_activity(a_id),
    FOREIGN KEY (t_id) REFERENCES t_team(t_id)
);
ALTER TABLE t_req CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE INDEX  t_req_idx_from_id ON t_req(from_id);
CREATE INDEX  t_req_idx_to_id ON t_req(to_id);