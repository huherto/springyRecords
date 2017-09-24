
drop schema test if exists;

create schema test;

drop table if exists owner;

CREATE TABLE owner (
  owner_id INTEGER IDENTITY PRIMARY KEY,
  name varchar(20) DEFAULT NULL,
);

drop table if exists pet;

CREATE TABLE pet (
  name varchar(20) NOT NULL PRIMARY KEY,
  owner varchar(20) DEFAULT NULL,
  species varchar(20) DEFAULT NULL,
  sex varchar(20) DEFAULT NULL,
  birth_date date DEFAULT NULL,
  death date DEFAULT NULL,
);
