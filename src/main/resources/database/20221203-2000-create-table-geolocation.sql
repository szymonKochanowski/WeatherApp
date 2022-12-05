--liquibase formatted sql
--changeset szymon:2
CREATE TABLE IF NOT EXISTS geolocation (
    id int PRIMARY KEY  NOT NULL AUTO_INCREMENT,
    city varchar(255),
    country varchar(255),
    lat Double,
    lon Double
);