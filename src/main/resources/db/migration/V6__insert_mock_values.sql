-- Categorías de prueba
INSERT INTO category (name)
VALUES
    ('Electrónica'),
    ('Informática'),
    ('Hogar');

-- Clientes de prueba
INSERT INTO customers (name, email, phone)
VALUES
    ('Juan Pérez', 'juan@test.com', '600111111'),
    ('Ana García', 'ana@test.com', '600222222');

-- Usuarios de prueba
-- Contraseña: password
INSERT INTO users (
    email,
    password,
    role,
    enabled
)
VALUES
    (
        'admin@admin.com',
        '$2a$10$mop.gcKWqgck3svwqktG6OdHdfilkvx6APQiPzJLZ.k4L8FG4eC6C',
        'ADMIN',
        true
    ),
    (
        'user@user.com',
        '$2a$10$mop.gcKWqgck3svwqktG6OdHdfilkvx6APQiPzJLZ.k4L8FG4eC6C',
        'USER',
        true
    );

-- Productos de prueba
INSERT INTO product (
    name,
    description,
    price,
    stock,
    created_at,
    category_id
)
VALUES
    (
        'Portátil Lenovo',
        'Portátil de pruebas',
        899.99,
        10,
        CURRENT_TIMESTAMP,
        (SELECT id FROM category WHERE name = 'Informática')
    ),
    (
        'Ratón Logitech',
        'Ratón inalámbrico',
        29.99,
        50,
        CURRENT_TIMESTAMP,
        (SELECT id FROM category WHERE name = 'Informática')
    ),
    (
        'Televisor Samsung',
        'Smart TV 55 pulgadas',
        599.99,
        5,
        CURRENT_TIMESTAMP,
        (SELECT id FROM category WHERE name = 'Electrónica')
    );

-- Pedido de prueba
INSERT INTO orders (
    customer_id,
    order_date,
    status
)
VALUES (
           (SELECT id FROM customers WHERE email = 'juan@test.com'),
           CURRENT_TIMESTAMP,
           'PENDING'
       );

-- Líneas del pedido de prueba
INSERT INTO order_line (
    order_id,
    product_id,
    quantity,
    unit_price
)
VALUES
    (
        (
            SELECT o.id
            FROM orders o
                     JOIN customers c ON o.customer_id = c.id
            WHERE c.email = 'juan@test.com'
            LIMIT 1
    ),
    (SELECT id FROM product WHERE name = 'Portátil Lenovo'),
    1,
    899.99
    ),
    (
        (
            SELECT o.id
            FROM orders o
            JOIN customers c ON o.customer_id = c.id
            WHERE c.email = 'juan@test.com'
            LIMIT 1
        ),
        (SELECT id FROM product WHERE name = 'Ratón Logitech'),
        2,
        29.99
    );