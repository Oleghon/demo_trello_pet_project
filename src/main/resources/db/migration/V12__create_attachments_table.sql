CREATE TABLE attachments(
    id uuid NOT NULL PRIMARY KEY,
    link VARCHAR(200) NOT NULL,
    name VARCHAR(200) NOT NULL,
    file bytea NOT NULL,
    card_id uuid CONSTRAINT attachment_card_id_fk REFERENCES cards(id),
    comment_id uuid CONSTRAINT attachment_comment_id_fk REFERENCES comments(id)
);