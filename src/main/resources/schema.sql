DROP TABLE IF EXISTS properties;

CREATE TABLE properties (
                            id VARCHAR(250)  PRIMARY KEY,
                            name VARCHAR(250) NOT NULL,
                            is_active bool NOT NULL,
                            created_at TIMESTAMP NOT NULL,
                            version INT DEFAULT NULL
);