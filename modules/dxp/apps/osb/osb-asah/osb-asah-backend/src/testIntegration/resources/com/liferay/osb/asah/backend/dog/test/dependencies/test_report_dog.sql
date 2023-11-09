INSERT INTO BlogDaily (assetId, assetTitle, channelId, clicks, comments, eventDate, ratings, readTime, userId, views) VALUES ('1', 'Blog 1', 1, 1, 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 1, 1, '1', 1);
INSERT INTO BlogDaily (assetId, assetTitle, channelId, clicks, comments, eventDate, ratings, readTime, userId, views) VALUES ('2', 'Blog 2', 1, 2, 2, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 2, 2, '2', 2);
INSERT INTO BlogDaily (assetId, assetTitle, channelId, clicks, comments, eventDate, ratings, readTime, userId, views) VALUES ('3', 'Blog 3', 1, 3, 3, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 3, 3, '3', 3);

INSERT INTO DocumentLibraryDaily (assetId, assetTitle, channelId, comments, downloads, eventDate, previews, ratings, userId) VALUES ('1', 'Document 1', 1, 1, 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 1, 1, '1');
INSERT INTO DocumentLibraryDaily (assetId, assetTitle, channelId, comments, downloads, eventDate, previews, ratings, userId) VALUES ('2', 'Document 2', 1, 2, 2, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 2, 2, '2');
INSERT INTO DocumentLibraryDaily (assetId, assetTitle, channelId, comments, downloads, eventDate, previews, ratings, userId) VALUES ('3', 'Document 3', 1, 3, 3, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 3, 3, '3');

INSERT INTO FormDaily (abandonments, assetId, assetTitle, channelId, eventDate, submissions, submissionsTime, userId, views) VALUES (1, '1', 'Form 1', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 1, 1, '1', 1);
INSERT INTO FormDaily (abandonments, assetId, assetTitle, channelId, eventDate, submissions, submissionsTime, userId, views) VALUES (2, '2', 'Form 2', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 2, 2, '2', 2);
INSERT INTO FormDaily (abandonments, assetId, assetTitle, channelId, eventDate, submissions, submissionsTime, userId, views) VALUES (3, '3', 'Form 3', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 3, 3, '3', 3);

INSERT INTO Identity_Raw (id, individualId) VALUES ('1', '1');
INSERT INTO Identity_Raw (id, individualId) VALUES ('2', '2');
INSERT INTO Identity_Raw (id, individualId) VALUES ('3', '3');

INSERT INTO Individual (emailAddress, firstName, id, lastName) VALUES ('test1@liferay.com', 'Test 1', '1', 'Test 1');
INSERT INTO Individual (emailAddress, firstName, id, lastName) VALUES ('test2@liferay.com', 'Test 2', '2', 'Test 2');
INSERT INTO Individual (emailAddress, firstName, id, lastName) VALUES ('test3@liferay.com', 'Test 3', '3', 'Test 3');

INSERT INTO JournalDaily (assetId, assetTitle, channelId, eventDate, userId, views) VALUES ('1', 'Journal 1', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), '1', 1);
INSERT INTO JournalDaily (assetId, assetTitle, channelId, eventDate, userId, views) VALUES ('2', 'Journal 2', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), '2', 2);
INSERT INTO JournalDaily (assetId, assetTitle, channelId, eventDate, userId, views) VALUES ('3', 'Journal 3', 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), '3', 3);

INSERT INTO PageDaily (bounce, canonicalUrl, channelId, entrances, eventDate, exits, timeOnPage, title, userId, views) VALUES (1, 'https://www.beryl.com/delivery', 1, 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 1, 1, 'Beryl Delivery', '1', 1);
INSERT INTO PageDaily (bounce, canonicalUrl, channelId, entrances, eventDate, exits, timeOnPage, title, userId, views) VALUES (1, 'https://www.beryl.com/delivery', 1, 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 1, 1, 'Beryl Delivery', '2', 1);
INSERT INTO PageDaily (bounce, canonicalUrl, channelId, entrances, eventDate, exits, timeOnPage, title, userId, views) VALUES (1, 'https://www.beryl.com/delivery', 1, 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 1, 1, 'Beryl Delivery', '3', 1);
INSERT INTO PageDaily (bounce, canonicalUrl, channelId, entrances, eventDate, exits, timeOnPage, title, userId, views) VALUES (1, 'https://www.liferay.com', 1, 1, TIMESTAMP(DATETIME_TRUNC(timestamp '2023-11-04T17:10:00.666Z', HOUR)), 1, 1, 'Liferay', '1', 1);