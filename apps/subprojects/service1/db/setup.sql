DROP TABLE IF EXISTS my_table;

CREATE TABLE my_table (id BIGINT PRIMARY KEY, time BIGINT, assc_id BIGINT, deleted BOOLEAN);

INSERT INTO my_table (id, time, assc_id, deleted) VALUES (1, 150, 20, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (2, 100, 40, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (3, 650, 20, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (4, 350, 20, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (5, 200, 20, true);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (6, 600, 30, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (7, 450, 20, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (8, 400, 40, true);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (9, 300, 40, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (10, 500, 40, false);
INSERT INTO my_table (id, time, assc_id, deleted) VALUES (11, 550, 20, false);

DROP TABLE IF EXISTS json_table;

CREATE TABLE json_table (id BIGINT PRIMARY KEY, data JSONB);
