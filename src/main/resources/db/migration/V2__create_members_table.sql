CREATE TABLE members(
id uuid NOT NULL PRIMARY KEY,
created_by VARCHAR(200),
updated_by VARCHAR(200),
created_date TIMESTAMP without time zone NOT NULL,
updated_date TIMESTAMP without time zone,
role VARCHAR(50) NOT NULL,
user_id uuid NOT NULL CONSTRAINT member_user_id_fk REFERENCES users(id)
);