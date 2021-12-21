CREATE TABLE workspaces(
id uuid NOT NULL PRIMARY KEY,
created_by VARCHAR(200),
updated_by VARCHAR(200),
created_date TIMESTAMP without time zone NOT NULL,
updated_date TIMESTAMP without time zone,
name VARCHAR(200) NOT NULL,
description VARCHAR(200),
visibility VARCHAR(200) NOT NULL,
member_id uuid  NOT NULL CONSTRAINT workspace_member_id_fk REFERENCES members(id)
);