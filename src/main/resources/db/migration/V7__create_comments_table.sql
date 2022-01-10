CREATE TABLE comments(
    id uuid NOT NULL PRIMARY KEY,
    created_by VARCHAR(200),
    updated_by VARCHAR(200),
    created_date TIMESTAMP without time zone NOT NULL,
    updated_date TIMESTAMP without time zone,
    text VARCHAR(200) NOT NULL,
    card_id uuid NOT NULL CONSTRAINT comment_card_id_fk REFERENCES cards(id) on delete cascade
);