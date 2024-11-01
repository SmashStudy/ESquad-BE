-- test-data.sql
INSERT INTO users (id, username, nickname) VALUES (1, 'adminname', 'adminnickname');
INSERT INTO team_space (id, team_name) VALUES (1, 'Existing Team');
INSERT INTO team_space_users (id, username, team_space_id, role) VALUES (1, 1, 1, 'Manager');
