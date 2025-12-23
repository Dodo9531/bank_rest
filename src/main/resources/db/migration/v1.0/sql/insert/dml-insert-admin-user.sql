INSERT INTO users (id, username, password, first_name, last_name)
VALUES ('00000000-0000-0000-0000-000000000001', 'Admin', '$2a$12$ec/ouDE1vZoA/f/DB8HFmOgS768TUA8yyTY0EgK6h/Hh07LZMTADy', 'Admin', 'Admin');

INSERT INTO user_roles (user_id, role_id) VALUES ('00000000-0000-0000-0000-000000000001', 1);
INSERT INTO user_roles (user_id, role_id) VALUES ('00000000-0000-0000-0000-000000000001', 2);
