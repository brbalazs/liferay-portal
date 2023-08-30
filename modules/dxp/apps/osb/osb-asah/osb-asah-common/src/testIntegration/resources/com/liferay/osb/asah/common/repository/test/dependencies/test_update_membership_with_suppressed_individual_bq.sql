INSERT INTO Identity_Raw (id, individualId) VALUES ('identity1', '1');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity2', '2');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity3', '3');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity4', '4');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity5', '5');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity6', '6');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity7', '7');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity8', '8');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity9', '9');
INSERT INTO Identity_Raw (id, individualId) VALUES ('identity10', '10');

INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity1', '1', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity2', '2', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity3', '3', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity4', '4', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity5', '5', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity6', '6', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity7', '7', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity8', '8', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity9', '9', timestamp '${now}');
INSERT INTO IdentityActivitySummary (activitiesCount, channelId, dataSourceId, eventId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 1, 123, 'pageViewed', timestamp '${today}', 'identity10', '10', timestamp '${now}');

INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test1@liferay.com', '1', 'Tester', null);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test2@liferay.com', '2', 'Tester', null);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test3@liferay.com', '3', null, null);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test4@liferay.com', '4', 'Tester', false);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test5@liferay.com', '5', 'Tester', true);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test6@liferay.com', '6', null, null);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test7@liferay.com', '7', null, null);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test8@liferay.com', '8', 'Tester', false);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test9@liferay.com', '9', null, false);
INSERT INTO Individual (emailAddress, id, jobTitle, suppressed) VALUES ('test10@liferay.com', '10', 'Tester', true);

INSERT INTO Membership (channelId, identityId, individualId) VALUES (1, 'identity1', '1');
INSERT INTO Membership (channelId, identityId, individualId) VALUES (1, 'identity2', '2');
INSERT INTO Membership (channelId, identityId, individualId) VALUES (1, 'identity4', '4');

INSERT INTO Suppression (emailAddress) VALUES ('test5@liferay.com');
INSERT INTO Suppression (emailAddress) VALUES ('test10@liferay.com');