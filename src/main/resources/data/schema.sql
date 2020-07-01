CREATE TABLE IF NOT EXISTS weather
(
    id          SERIAL PRIMARY KEY,
    city        VARCHAR(255) NOT NULL,
    country     VARCHAR(255) NOT NULL,
    temperature NUMERIC(5, 2)
);