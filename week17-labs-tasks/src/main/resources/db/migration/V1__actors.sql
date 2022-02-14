-- USE mysql;
-- CREATE DATABASE IF NOT EXISTS movies_actors;
-- CREATE USER 'movies'@'localhost' identified by 'movies';
-- grant all on *.* to 'movies'@'localhost';
-- USE movies_actors;
CREATE TABLE actors (id BIGINT AUTO_INCREMENT, actor_name VARCHAR(255), PRIMARY KEY(id));