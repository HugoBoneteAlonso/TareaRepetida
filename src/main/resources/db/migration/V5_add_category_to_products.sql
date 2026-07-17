CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO category(name) VALUES ('Sin categoria');

ALTER TABLE product
    ADD COLUMN category_id BIGINT;

UPDATE product
    SET category_id = (SELECT id FROM categories WHERE name = 'Sin categoria');

ALTER TABLE product
    ALTER COLUMN category_id SET NOT NULL;

ALTER TABLE product
    ADD CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category(id);