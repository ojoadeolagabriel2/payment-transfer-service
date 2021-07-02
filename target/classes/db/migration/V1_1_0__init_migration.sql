DROP TABLE IF EXISTS transfer_requests;

CREATE TABLE customer_account
(
    id             BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id        VARCHAR(50),
    first_name     VARCHAR(20),
    middle_name    VARCHAR(20),
    last_name      VARCHAR(20),
    account_number VARCHAR(15),
    account_type   VARCHAR(10),
    last_balance   DOUBLE,
    created        DATETIME,
    modified       DATETIME
);


CREATE TABLE customer_account_transaction
(
    id                  BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    customer_account_id INT    NOT NULL,
    type                VARCHAR(10),
    amount              DOUBLE,
    created             DATETIME,
    modified            DATETIME,
    foreign key (customer_account_id) references customer_account (id)
);

INSERT INTO customer_account (user_id, first_name, middle_name, last_name, account_number, account_type, last_balance,
                              created, modified)
VALUES ('100012345', 'tony', 'blair', 'omairon', '1000000011', 'current', 0, now(), now());

INSERT INTO customer_account (user_id, first_name, middle_name, last_name, account_number, account_type, last_balance,
                              created, modified)
VALUES ('100012346', 'laura', 'sloan', 'jessica', '1000000021', 'savings', 0, now(), now());

INSERT INTO customer_account_transaction (customer_account_id, type, amount, created, modified)
VALUES (1, 'CR', 1000020.5, now(), now());

INSERT INTO customer_account_transaction (customer_account_id, type, amount, created, modified)
VALUES (1, 'DR', 20.5, now(), now())