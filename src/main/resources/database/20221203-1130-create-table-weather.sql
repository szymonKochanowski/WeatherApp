--liquibase formatted sql
--changeset szymon:1
CREATE TABLE IF NOT EXISTS weather (
    id int PRIMARY KEY  NOT NULL AUTO_INCREMENT,
    description varchar(255),
    city varchar(255),
    country varchar(255),
    timezone DATETIME,
    temperature double,
    temperature_min double,
    temperature_max double,
    wind_speed int,
    rain int,
    snow int,
    pressure int,
    humidity int,
    clouds int
);