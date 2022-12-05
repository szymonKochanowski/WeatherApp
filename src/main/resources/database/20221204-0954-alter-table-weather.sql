--liquibase formatted sql
--changeset szymon:3
USE weatherapp;
ALTER TABLE weather
MODIFY COLUMN timezone int,
MODIFY COLUMN wind_speed double,
DROP COLUMN rain,
DROP COLUMN snow;
