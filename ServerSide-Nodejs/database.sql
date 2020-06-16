CREATE DATABASE "userprofiles";

CREATE TABLE account(
    user_id SERIAL PRIMARY KEY,
    email VARCHAR (50) UNIQUE NOT NULL,
    password VARCHAR (50) NOT NULL,
    name VARCHAR (50) UNIQUE NOT NULL,
    age INT NOT NULL
);