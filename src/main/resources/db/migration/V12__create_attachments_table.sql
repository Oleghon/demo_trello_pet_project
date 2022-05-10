CREATE TABLE attachments(
    id uuid NOT NULL PRIMARY KEY,
    context VARCHAR(200) NOT NULL,
    name VARCHAR(200) NOT NULL,
    file oid NOT NULL,
    key_id uuid
);