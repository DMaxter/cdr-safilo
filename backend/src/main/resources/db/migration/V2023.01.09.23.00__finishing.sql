CREATE TABLE Finishing
(
    id   BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(255) NULL,
    cost DOUBLE NOT NULL,
    CONSTRAINT pk_finishing PRIMARY KEY (id)
);

ALTER TABLE Finishing
    ADD CONSTRAINT uc_finishing_name UNIQUE (name);

ALTER TABLE RequestType
    MODIFY type VARCHAR (31) NULL;