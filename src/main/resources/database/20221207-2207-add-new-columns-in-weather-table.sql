--liquibase formatted sql
--changeset szymon:2
USE weatherapp;
ALTER TABLE weather
ADD latitude double,
ADD longitude double;

