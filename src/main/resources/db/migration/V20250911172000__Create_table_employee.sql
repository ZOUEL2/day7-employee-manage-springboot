CREATE TABLE t_employee
(
    id    BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    age  INTEGER,
    gender VARCHAR(255),
    salary FLOAT(53),
    company_id BIGINT,
    PRIMARY KEY (id)
);