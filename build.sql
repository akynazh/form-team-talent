CREATE DATABASE form_team_talent;

USE form_team_talent;

DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    u_open_id VARCHAR(255),
    u_name VARCHAR(255),
    u_stu_num VARCHAR(64),
    u_school VARCHAR(255),
    PRIMARY KEY(u_open_id)
);

DROP TABLE IF EXISTS t_activity;
CREATE TABLE t_activity (
    a_id BIGINT AUTO_INCREMENT,
    a_name VARCHAR(255),
    a_holder_id BIGINT,
    a_desc VARCHAR(1024),
    a_end_date VARCHAR(128),
    a_is_public INT,
    PRIMARY KEY(a_id)
);

DROP TABLE IF EXISTS t_group;
CREATE TABLE t_group (
    g_id BIGINT AUTO_INCREMENT,
    g_name VARCHAR(255),
    g_leader_id BIGINT,
    g_desc VARCHAR(1024),
    a_id BIGINT,
    PRIMARY KEY(g_id)
);

DROP TABLE IF EXISTS t_group_task;
CREATE TABLE t_group_task (
    id BIGINT AUTO_INCREMENT,
    t_id BIGINT,
    t_desc VARCHAR(255),
    g_id BIGINT,
    u_id BIGINT,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS t_join_info;
CREATE TABLE t_join_info (
    id BIGINT AUTO_INCREMENT,
    u_id BIGINT,
    a_id BIGINT,
    g_id BIGINT,
    PRIMARY KEY (id)
);