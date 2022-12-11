--liquibase formatted sql
--changeset szymon:1
USE weatherapp;
ALTER TABLE geolocation
RENAME COLUMN lat TO latitude,
RENAME COLUMN lon TO longitude;
