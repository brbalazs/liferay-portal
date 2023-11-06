INSERT INTO Identity_Raw (id, individualId) VALUES ('1', '1');
INSERT INTO Identity_Raw (id, individualId) VALUES ('2', '2');
INSERT INTO Identity_Raw (id, individualId) VALUES ('3', '3');

INSERT INTO Individual (emailAddress, firstName, id, lastName) VALUES ('test1@liferay.com', 'Test 1', '1', 'Test 1');
INSERT INTO Individual (emailAddress, firstName, id, lastName) VALUES ('test2@liferay.com', 'Test 2', '1', 'Test 2');
INSERT INTO Individual (emailAddress, firstName, id, lastName) VALUES ('test3@liferay.com', 'Test 3', '1', 'Test 3');

INSERT INTO PageDaily (canonicalUrl, channelId, eventDate, title, userId, views) VALUES ('https://www.beryl.com/delivery', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 'Beryl Delivery', '1', 1);
INSERT INTO PageDaily (canonicalUrl, channelId, eventDate, title, userId, views) VALUES ('https://www.beryl.com/delivery', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 'Beryl Delivery', '2', 1);
INSERT INTO PageDaily (canonicalUrl, channelId, eventDate, title, userId, views) VALUES ('https://www.beryl.com/delivery', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 'Beryl Delivery', '3', 1);