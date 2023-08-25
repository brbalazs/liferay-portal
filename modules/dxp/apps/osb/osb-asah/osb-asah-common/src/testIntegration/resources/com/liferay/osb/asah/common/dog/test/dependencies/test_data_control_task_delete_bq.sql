INSERT INTO ExpandoValue (classPK, classType, columnId, dataSourceId, id, value) VALUES ('36016', 'com.liferay.portal.kernel.model.User', '1', 405201047787757795, '1', 'test');

INSERT INTO Identity_Raw(id, individualId, createDate) VALUES ('1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '2023-08-23T00:00:00.000Z');
INSERT INTO Identity_Raw(id, individualId, createDate) VALUES ('2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '2023-08-24T14:45:00.000Z');

INSERT INTO Individual (createDate, emailAddress, id, modifiedDate) values (CURRENT_TIMESTAMP, 'test1@liferay.com', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', CURRENT_TIMESTAMP);
INSERT INTO Individual (createDate, emailAddress, id, modifiedDate) values (CURRENT_TIMESTAMP, 'test2@liferay.com', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', CURRENT_TIMESTAMP);

INSERT INTO User (dataSourceId, dxpUserId, firstName, id, individualId, modifiedDate) VALUES (405201047787757795, 36016, 'Test 1', '1', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485', timestamp '${now}');
INSERT INTO User (dataSourceId, dxpUserId, firstName, id, individualId, modifiedDate) VALUES (405201047787757795, 36017, 'Test 2', '2', '09d283764c971fbd2697396513679fe8ef5f416bfea42858b0c44289c4eb782f', timestamp '${now}');