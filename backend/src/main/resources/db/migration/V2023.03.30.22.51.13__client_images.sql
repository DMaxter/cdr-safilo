ALTER TABLE Image
    ADD client_id BIGINT NULL;

ALTER TABLE Image
    ADD CONSTRAINT FK_IMAGE_ON_CLIENT FOREIGN KEY (client_id) REFERENCES Client (id);