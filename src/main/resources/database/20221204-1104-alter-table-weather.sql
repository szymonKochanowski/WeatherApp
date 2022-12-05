--liquibase formatted sql
--changeset szymon:1
USE weatherapp;

ALTER TABLE weather
ADD COLUMN local_data_time DATETIME;

ALTER TABLE weather
MODIFY COLUMN local_data_time TIMESTAMP;

ALTER TABLE weather
RENAME COLUMN local_data_time TO date_time;