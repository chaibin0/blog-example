DROP TABLE EMPLOYEE;

CREATE TABLE EMPLOYEE
(
    EMPLOYEE_ID     BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    EMPLOYEE_NAME   VARCHAR(100),
    EMPLOYEE_STATUS VARCHAR(30) NOT NULL,
    EMPLOYEE_GENDER VARCHAR(10) NOT NULL,
    CREATED_AT      DATETIME    NOT NULL
) ENGINE=InnoDB;


# 더미 데이터

DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsert$$
CREATE PROCEDURE loopInsert()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 100 DO
            INSERT INTO EMPLOYEE(EMPLOYEE_NAME , EMPLOYEE_STATUS, EMPLOYEE_GENDER , CREATED_AT)
            VALUES(concat('사원',i), 'NOT_APPLIED', 'MALE', now());
            SET i = i + 1;
END WHILE;
END$$
DELIMITER $$
CALL loopInsert;

DROP PROCEDURE loopInsert