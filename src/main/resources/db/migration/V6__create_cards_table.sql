CREATE TABLE cards(
id uuid NOT NULL PRIMARY KEY,
created_by VARCHAR(200),
updated_by VARCHAR(200),
created_date TIMESTAMP without time zone NOT NULL,
updated_date TIMESTAMP without time zone,
name VARCHAR(200) NOT NULL,
description VARCHAR(200),
archived BOOLEAN NOT NULL,
cardlist_id uuid NOT NULL CONSTRAINT card_cardlist_id_fk REFERENCES cardlists(id)
);