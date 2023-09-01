INSERT INTO DXPEntity (classPK, dataSourceId, type) VALUES ('36016', 405201047787757795, 'com.liferay.portal.kernel.model.User');
INSERT INTO DXPEntity (classPK, dataSourceId, type) VALUES ('36017', 405201047787757795, 'com.liferay.portal.kernel.model.User');

INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 1, 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-23T00:00:00.000Z', 'pageViewed', 'new_identity1');
INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 1, 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-24T00:00:00.000Z', 'pageViewed', 'new_identity1');
INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 1, 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-25T00:00:00.000Z', 'pageViewed', '1');
INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 2, 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-23T00:00:00.000Z', 'pageViewed', 'new_identity1');
INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 1, '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-23T00:00:00.000Z', 'pageViewed', '2');
INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 1, '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-24T00:00:00.000Z', 'pageViewed', '2');
INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 1, '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-25T00:00:00.000Z', 'pageViewed', '2');
INSERT INTO Event (applicationId, channelId, emailAddressHashed, eventDate, eventId, userId) VALUES ('Page', 2, '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-23T00:00:00.000Z', 'pageViewed', '2');

INSERT INTO ExpandoValue (classPK, classType, columnId, dataSourceId, id, value) VALUES ('36016', 'com.liferay.portal.kernel.model.User', '1', 405201047787757795, '1', 'test');

INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 123, timestamp '2023-08-23T00:00:00.000Z', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-23T00:00:00.000Z');
INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 123, timestamp '2023-08-24T00:00:00.000Z', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-24T00:00:00.000Z');
INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 123, timestamp '2023-08-25T00:00:00.000Z', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-25T00:00:00.000Z');
INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (2, 345, timestamp '2023-08-23T00:00:00.000Z', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-23T00:00:00.000Z');
INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 123, timestamp '2023-08-23T00:00:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-23T00:00:00.000Z');
INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 123, timestamp '2023-08-24T00:00:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-24T00:00:00.000Z');
INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (1, 123, timestamp '2023-08-25T00:00:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-25T00:00:00.000Z');
INSERT INTO IdentityActivitySummary (channelId, dataSourceId, firstActivityDate, identityId, individualId, lastActivityDate) VALUES (2, 345, timestamp '2023-08-23T00:00:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-23T00:00:00.000Z');

INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '2023-08-23T00:00:00.000Z', 'new_identity1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '2023-08-24T00:00:00.000Z', 'new_identity1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '2023-08-25T00:00:00.000Z', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '2023-08-23T00:00:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '2023-08-24T00:00:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '2023-08-24T14:45:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '2023-08-25T00:00:00.000Z', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f');

INSERT INTO Individual (createDate, emailAddress, id, modifiedDate) values (CURRENT_TIMESTAMP, 'test1@liferay.com', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', CURRENT_TIMESTAMP);
INSERT INTO Individual (createDate, emailAddress, id, modifiedDate) values (CURRENT_TIMESTAMP, 'test2@liferay.com', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', CURRENT_TIMESTAMP);

INSERT INTO User (dataSourceId, dxpUserId, firstName, id, individualId, modifiedDate) VALUES (405201047787757795, 36016, 'Test 1', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '${now}');
INSERT INTO User (dataSourceId, dxpUserId, firstName, id, individualId, modifiedDate) VALUES (405201047787757795, 36017, 'Test 2', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '${now}');