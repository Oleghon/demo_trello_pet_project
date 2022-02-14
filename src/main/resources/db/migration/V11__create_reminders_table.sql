CREATE TABLE reminders(
    id uuid NOT NULL PRIMARY KEY,
    created_by VARCHAR(200),
    updated_by VARCHAR(200),
    created_date TIMESTAMP without time zone NOT NULL,
    updated_date TIMESTAMP without time zone,
    starts TIMESTAMP without time zone,
    ends TIMESTAMP without time zone,
    remind_on TIMESTAMP without time zone,
    alive BOOLEAN,
);

