create database coffee;
use coffee;

-- 1. 고객
CREATE TABLE customer (
                          id          BIGINT       NOT NULL AUTO_INCREMENT,
                          email       VARCHAR(255) NOT NULL,
                          created_at  DATETIME(6)  NOT NULL,
                          deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                          deleted_at  DATETIME(6),
                          PRIMARY KEY (id),
                          UNIQUE KEY uk_customer_email (email)
);

-- 2. 메뉴
CREATE TABLE menu (
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

-- 3. 주문 (order는 예약어라 백틱 필수)
CREATE TABLE `order` (
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
                             FOREIGN KEY (customer_id) REFERENCES customer (id)
);

-- 4. 주문 항목 (Soft Delete 없음)
CREATE TABLE order_item (
                            id         BIGINT NOT NULL AUTO_INCREMENT,
                            order_id   BIGINT NOT NULL,
                            menu_id    BIGINT NOT NULL,
                            quantity   INT    NOT NULL,
                            unit_price INT    NOT NULL,
                            PRIMARY KEY (id),
                            CONSTRAINT fk_order_item_order
                                FOREIGN KEY (order_id) REFERENCES `order` (id),
                            CONSTRAINT fk_order_item_menu
                                FOREIGN KEY (menu_id) REFERENCES menu (id)
);