CREATE DATABASE coffee_order
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE coffee;

show tables;
desc customers;
-- 1. 고객
CREATE TABLE customers (
                          id          BIGINT       NOT NULL AUTO_INCREMENT,
                          email       VARCHAR(255) NOT NULL,
                          created_at  DATETIME(6)  NOT NULL,
                          deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                          deleted_at  DATETIME(6),
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_customer_email (email)
);

-- 2. 메뉴
CREATE TABLE menus (
                      id          BIGINT       NOT NULL AUTO_INCREMENT,
                      name        VARCHAR(255) NOT NULL,
                      price       INT          NOT NULL,
                      description VARCHAR(255),
                      created_at  DATETIME(6)  NOT NULL,
                      updated_at  DATETIME(6),
                      deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                      deleted_at  DATETIME(6),
                      PRIMARY KEY (id)
);

-- 3. 주문 (orders는 예약어가 아니라 백틱 불필요)
CREATE TABLE orders (
                         id          BIGINT       NOT NULL AUTO_INCREMENT,
                         customer_id BIGINT,
                         order_date  DATE         NOT NULL,
                         status      VARCHAR(255) NOT NULL,
                         address     VARCHAR(255) NOT NULL,
                         zip_code    VARCHAR(255) NOT NULL,
                         created_at  DATETIME(6)  NOT NULL,
                         deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                         deleted_at  DATETIME(6),
                         PRIMARY KEY (id),
                         CONSTRAINT fk_order_customer
                             FOREIGN KEY (customer_id) REFERENCES customers (id)
);

-- 4. 주문 항목 (Soft Delete 없음)
CREATE TABLE order_items (
                            id         BIGINT NOT NULL AUTO_INCREMENT,
                            order_id   BIGINT NOT NULL,
                            menu_id    BIGINT NOT NULL,
                            quantity   INT    NOT NULL,
                            unit_price INT    NOT NULL,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_order_item_order
                                FOREIGN KEY (order_id) REFERENCES orders (id),
                            CONSTRAINT fk_order_item_menu
                                FOREIGN KEY (menu_id) REFERENCES menus (id)
);





# Test
INSERT INTO customers (
    email,
    created_at,
    deleted
) VALUES (
             'test9@example.com',
             NOW(),
             FALSE
         );

SET @customer_id = LAST_INSERT_ID();

INSERT INTO orders (
    customer_id,
    order_date,
    status,
    address,
    zip_code,
    created_at,
    deleted
) VALUES (
             @customer_id,
             CURDATE(),
             'SHIPPING',
             '서울시 강남구',
             '12345',
             NOW(),
             FALSE
         );

SELECT * FROM orders;

SELECT
    o.id AS order_id,
    o.customer_id,
    c.id AS customer_id,
    c.email,
    o.status,
    o.deleted
FROM orders o
         JOIN customers c ON c.id = o.customer_id
WHERE o.id = 5;