--liquibase formatted sql
--changeset szymon:1
USE weatherapp;
ALTER TABLE weather
DROP COLUMN data_time;