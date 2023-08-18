INSERT INTO Channel (id) VALUES (1);

INSERT INTO DataControlTask (id, batchId, continueDate, createDate, emailAddress, ownerId, startDate, status, type, userId, userName) VALUES (12345, 98765, timestamp '2023-08-03T01:30:00.000Z', timestamp '2023-08-02T22:55:00.000Z', 'test1@liferay.com', 1, timestamp '2023-08-03T00:00:00.000Z', 'RUNNING', 'SUPPRESS', 1111, 'Test Test');

INSERT INTO Segment (channelId, id, includeAnonymousUsers, state, status, type) VALUES (1, 123456, false, 'READY', 'ACTIVE', 'STATIC');
INSERT INTO Segment (channelId, id, includeAnonymousUsers, state, status, type) VALUES (1, 234567, false, 'READY', 'ACTIVE', 'STATIC');
INSERT INTO Segment (channelId, id, includeAnonymousUsers, state, status, type) VALUES (1, 345678, true, 'READY', 'ACTIVE', 'DYNAMIC');
INSERT INTO Segment (channelId, id, includeAnonymousUsers, state, status, type) VALUES (1, 456789, false, 'READY', 'ACTIVE', 'DYNAMIC');