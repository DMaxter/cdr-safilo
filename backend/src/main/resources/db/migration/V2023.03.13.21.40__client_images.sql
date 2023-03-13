ALTER TABLE Image
    ADD client_id BIGINT NULL;

ALTER TABLE Image
    ADD CONSTRAINT FK_IMAGE_ON_CLIENT FOREIGN KEY (client_id) REFERENCES Client (id);

ALTER TABLE RequestSlot
DROP
FOREIGN KEY FK_REQUESTSLOT_ON_FINISHING;

ALTER TABLE Material_FinishingGroup
DROP
FOREIGN KEY FKao7iw4xoltxm3cjnf4hg4fdm3;

ALTER TABLE Material_Finishing
DROP
FOREIGN KEY FKaquxeg93o42kbamm9gof3c48r;

ALTER TABLE FinishingGroup_Finishing
DROP
FOREIGN KEY FKb9dq56kn51897owkyltmvl6j8;

ALTER TABLE RequestSlot_Finishing
DROP
FOREIGN KEY FKf7dulpeklnm50b6u9opxxmhfl;

ALTER TABLE FinishingGroup_Finishing
DROP
FOREIGN KEY FKhwjm3x4wxipxu3wqxpiqwy8w7;

ALTER TABLE RequestSlot_Finishing
DROP
FOREIGN KEY FKkl23ccybkck4ihctfamfcpnl9;

ALTER TABLE Price_Finishing
DROP
FOREIGN KEY FKl2et4e8dqgdr8xj27ot3roc7m;

ALTER TABLE Material_FinishingGroup
DROP
FOREIGN KEY FKm2550cm2g769pesjh87r9tbto;

ALTER TABLE Material_Finishing
DROP
FOREIGN KEY FKpqi88t0hie738it3juj1536q5;

ALTER TABLE Price_Finishing
DROP
FOREIGN KEY FKsw519a5xvg10lc603rbqetlfd;

DROP TABLE FinishingGroup_Finishing;

DROP TABLE Material_Finishing;

DROP TABLE Material_FinishingGroup;

DROP TABLE Price_Finishing;

DROP TABLE RequestSlot_Finishing;

ALTER TABLE RequestSlot
DROP
COLUMN finishing_id;

ALTER TABLE RequestSlot
    MODIFY cost DOUBLE NOT NULL;