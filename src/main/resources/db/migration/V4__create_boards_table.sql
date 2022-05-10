CREATE TABLE boards(
    id uuid NOT NULL PRIMARY KEY,
    created_by VARCHAR(200),
    updated_by VARCHAR(200),
    created_date TIMESTAMP without time zone NOT NULL,
    updated_date TIMESTAMP without time zone,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(200),
    archived BOOLEAN NOT NULL,
    visibility VARCHAR(100) NOT NULL,
    workspace_id uuid NOT NULL CONSTRAINT board_workspace_id_fk REFERENCES workspaces(id) on delete cascade
);