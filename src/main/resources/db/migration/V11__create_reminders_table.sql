CREATE TABLE reminders(
    id uuid NOT NULL PRIMARY KEY,
    starts TIMESTAMP without time zone,
    ends TIMESTAMP without time zone,
    remind_on TIMESTAMP without time zone,
    alive BOOLEAN,
);

