INSERT INTO Event (applicationId, channelId, createDate, eventDate, eventId, id, userId) VALUES ('Blog', 1, timestamp '${now-2d}', timestamp '${now-2d}', 'blogClicked', '1', '1');
INSERT INTO Event (applicationId, channelId, createDate, eventDate, eventId, id, userId) VALUES ('Blog', 1, timestamp '${now-4d}', timestamp '${now-4d}', 'blogClicked', '2', '2');
INSERT INTO Event (applicationId, channelId, createDate, eventDate, eventId, id, userId) VALUES ('Blog', 1, timestamp '${now-11d}', timestamp '${now-11d}', 'blogClicked', '3', '3');
INSERT INTO Event (applicationId, channelId, createDate, eventDate, eventId, id, userId) VALUES ('Blog', 1, timestamp '${now-35d}', timestamp '${now-35d}', 'blogClicked', '4', '4');
INSERT INTO Event (applicationId, channelId, createDate, eventDate, eventId, id, userId) VALUES ('Blog', 1, timestamp '${now-11d}', timestamp '${now-11d}', 'blogClicked', '5', '5');
INSERT INTO Event (applicationId, channelId, createDate, eventDate, eventId, id, userId) VALUES ('Blog', 1, timestamp '${now-47d}', timestamp '${now-47d}', 'blogClicked', '6', '6');
INSERT INTO Event (applicationId, channelId, createDate, eventDate, eventId, id, userId) VALUES ('Blog', 1, timestamp '${now-49d}', timestamp '${now-49d}', 'blogClicked', '7', '7');

INSERT INTO IdentityActivitySummary (channelId, identityId, individualId, lastActivityDate) VALUES (1, '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '${now-3d}');
INSERT INTO IdentityActivitySummary (channelId, identityId, individualId, lastActivityDate) VALUES (1, '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '${now-5d}');
INSERT INTO IdentityActivitySummary (channelId, identityId, individualId, lastActivityDate) VALUES (1, '3', '8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220', timestamp '${now-11d}');
INSERT INTO IdentityActivitySummary (channelId, identityId, individualId, lastActivityDate) VALUES (1, '4', 'e9def3785eef54c84bda7c05374abd1dfd7393592771eab541cf6be6f9961cc9', timestamp '${now-36d}');
INSERT INTO IdentityActivitySummary (channelId, identityId, individualId, lastActivityDate) VALUES (1, '5', '', timestamp '${now-11d}');
INSERT INTO IdentityActivitySummary (channelId, identityId, individualId, lastActivityDate) VALUES (1, '6', 'e9def3785eef54c84bda7c05374abd1dfd7393592771eab541cf6be6f9961cc9', timestamp '${now-47d}');
INSERT INTO IdentityActivitySummary (channelId, identityId, individualId, lastActivityDate) VALUES (1, '7', 'e9def3785eef54c84bda7c05374abd1dfd7393592771eab541cf6be6f9961cc9', timestamp '${now-49d}');

INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${now-3d}', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${now-5d}', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${now-11d}', '3', '8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${now-36d}', '4', 'e9def3785eef54c84bda7c05374abd1dfd7393592771eab541cf6be6f9961cc9');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${now-11d}', '5', '');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${now-47d}', '6', 'e9def3785eef54c84bda7c05374abd1dfd7393592771eab541cf6be6f9961cc9');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${now-49d}', '7', 'e9def3785eef54c84bda7c05374abd1dfd7393592771eab541cf6be6f9961cc9');

INSERT INTO Individual (createDate, emailAddress, firstName, id, suppressed) VALUES (timestamp '${now-2d}', 'test1@liferay.com', 'Test1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', null);
INSERT INTO Individual (createDate, emailAddress, firstName, id, suppressed) VALUES (timestamp '${now-4d}', 'test2@liferay.com', null, '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', null);
INSERT INTO Individual (createDate, emailAddress, firstName, id, suppressed) VALUES (timestamp '${now-10d}', 'test3@liferay.com', null, '8bb3cd4319c4cc4df1addc31cb0fae500288133b91228a1cacb4ff2802446220', false);
INSERT INTO Individual (createDate, emailAddress, firstName, id, suppressed) VALUES (timestamp '${now-35d}', 'test4@liferay.com', null, 'e9def3785eef54c84bda7c05374abd1dfd7393592771eab541cf6be6f9961cc9', true);

INSERT INTO Session (browserName, id, sessionEnd, sessionStart, userId) VALUES ('browser1', '1', timestamp '${now}', timestamp '${now-3h}', '1');