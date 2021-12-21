CREATE TABLE users(
id uuid NOT NULL PRIMARY KEY,
created_by VARCHAR(200),
updated_by VARCHAR(200),
created_date TIMESTAMP without time zone NOT NULL,
updated_date TIMESTAMP without time zone,
first_name VARCHAR(200) NOT NULL,
last_name VARCHAR(200) NOT NULL,
email VARCHAR(200) NOT NULL
);