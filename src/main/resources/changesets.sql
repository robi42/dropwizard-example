-- liquibase formatted sql dbms:postgresql

-- changeset robi42:1
CREATE TABLE people (
  id                   UUID PRIMARY KEY,
  time_zone_preference VARCHAR(255) NOT NULL DEFAULT 'GMT',
  created_at           TIMESTAMP    NOT NULL DEFAULT NOW(),
  modified_at          TIMESTAMP,
  first_name           VARCHAR(255) NOT NULL,
  last_name            VARCHAR(255) NOT NULL,
  gender               VARCHAR(255),
  job_title            VARCHAR(255),
  email_address        VARCHAR(255),
  favorite_number      INT,
  birthday             DATE
);
-- ROLLBACK DROP TABLE people;

-- changeset robi42:2
ALTER TABLE people ADD CONSTRAINT people_email_address_key UNIQUE (email_address);
-- ROLLBACK ALTER TABLE people DROP CONSTRAINT people_email_address_key;
