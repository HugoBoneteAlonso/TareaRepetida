CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description VARCHAR(500),
                         price NUMERIC(10,2) NOT NULL,
                         stock INTEGER NOT NULL DEFAULT 0,
                         created_at TIMESTAMP NOT NULL DEFAULT now()
);