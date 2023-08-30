INSERT INTO IdentityActivitySummary (firstActivityDate, identityId, individualId, lastActivityDate) VALUES (timestamp '${today-32d}', 'identity1', '1', timestamp '${today-32d}');
INSERT INTO IdentityActivitySummary (firstActivityDate, identityId, individualId, lastActivityDate) VALUES (timestamp '${today-28d}', 'identity2', '2', timestamp '${today-28d}');
INSERT INTO IdentityActivitySummary (firstActivityDate, identityId, individualId, lastActivityDate) VALUES (timestamp '${today-7d}', 'identity3', '3', timestamp '${today-7d}');
INSERT INTO IdentityActivitySummary (firstActivityDate, identityId, individualId, lastActivityDate) VALUES (timestamp '${today-3d}', 'identity4', '4', timestamp '${today-3d}');
INSERT INTO IdentityActivitySummary (firstActivityDate, identityId, individualId, lastActivityDate) VALUES (timestamp '${today-1d}', 'identity5', '5', timestamp '${today-1d}');
INSERT INTO IdentityActivitySummary (firstActivityDate, identityId, individualId, lastActivityDate) VALUES (timestamp '${today}', 'identity6', null, timestamp '${today}');

INSERT INTO Individual (emailAddress, id, suppressed) VALUES ('test1@liferay.com', '1', null);
INSERT INTO Individual (emailAddress, id, suppressed) VALUES ('test2@liferay.com', '2', false);
INSERT INTO Individual (emailAddress, id, suppressed) VALUES ('test3@liferay.com', '3', true);
INSERT INTO Individual (emailAddress, id, suppressed) VALUES ('test4@liferay.com', '4', false);

INSERT INTO Membership (identityId, individualId, segmentId) VALUES ('identity1', '1', 123);
INSERT INTO Membership (identityId, individualId, segmentId) VALUES ('identity2', '2', 123);
INSERT INTO Membership (identityId, individualId, segmentId) VALUES ('identity3', null, 123);
INSERT INTO Membership (identityId, individualId, segmentId) VALUES ('identity4', '4', 123);
INSERT INTO Membership (identityId, individualId, segmentId) VALUES ('identity5', '5', 123);
INSERT INTO Membership (identityId, individualId, segmentId) VALUES ('identity6', null, 123);

INSERT INTO Suppression (emailAddress) VALUES ('test3@liferay.com');