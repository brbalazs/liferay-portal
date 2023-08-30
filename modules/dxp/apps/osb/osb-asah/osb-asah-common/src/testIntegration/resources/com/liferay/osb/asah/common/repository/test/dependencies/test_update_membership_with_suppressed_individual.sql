INSERT INTO Channel (id) VALUES (1);

INSERT INTO Segment (id, channelId, filter, name, state, status, type) VALUES (111, 1, '(((demographics/jobTitle/value ne null)))', 'Has Job Title', 'READY', 'ACTIVE', 'DYNAMIC');