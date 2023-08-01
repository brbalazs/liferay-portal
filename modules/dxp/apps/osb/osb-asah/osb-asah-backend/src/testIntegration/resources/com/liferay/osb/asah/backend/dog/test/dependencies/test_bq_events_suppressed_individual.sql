INSERT INTO Event (applicationId, id, canonicalUrl, channelId, eventDate, eventId, sessionId, title, userId) VALUES ('Custom', '10', 'http://liferay.com', 1, timestamp '${now-3d}', 'assetClicked', '1', 'Liferay', '1');
INSERT INTO Event (applicationId, id, canonicalUrl, channelId, eventDate, eventId, sessionId, title, userId) VALUES ('Page', '20', 'http://liferay.com', 1, timestamp '${now-3d}', 'pageUnloaded', '1', 'Liferay', '1');
INSERT INTO Event (applicationId, id, canonicalUrl, channelId, eventDate, eventId, sessionId, title, userId) VALUES ('SocialBookmarks', '30', 'http://liferay.com', 1, timestamp '${now-3d}', 'shared', '1', 'Liferay', '1');
INSERT INTO Event (applicationId, id, canonicalUrl, channelId, eventDate, eventId, sessionId, title, userId) VALUES ('Form', '40', 'http://liferay.com', 1, timestamp '${now-10d}', 'formViewed', '3', 'Liferay', '2');
INSERT INTO Event (applicationId, id, canonicalUrl, channelId, eventDate, eventId, sessionId, title, userId) VALUES ('Blog', '50', 'http://liferay.com', 1, timestamp '${now-34d}', 'blogClicked', '2', 'Liferay', '1');
INSERT INTO Event (applicationId, id, canonicalUrl, channelId, eventDate, eventId, sessionId, title, userId) VALUES ('Form', '60', 'http://liferay.com', 1, timestamp '${now-34d}', 'fieldBlurred', '2', 'Liferay', '1');
INSERT INTO Event (applicationId, id, canonicalUrl, channelId, eventDate, eventId, sessionId, title, userId) VALUES ('Page', '70', 'http://liferay.com', 1, timestamp '${now-36d}', 'pageViewed', '4', 'Liferay', '3');

INSERT INTO Identity_Raw (id, individualId) VALUES ('1', '1');
INSERT INTO Identity_Raw (id, individualId) VALUES ('3', '1');

INSERT INTO Individual (id, suppressed) VALUES ('1', true);

INSERT INTO Session (channelId, id, sessionEnd, sessionStart, userId) VALUES (1, '1', timestamp '${now-3d}', timestamp '${now-3d}', '1');
INSERT INTO Session (channelId, id, sessionEnd, sessionStart, userId) VALUES (1, '2', timestamp '${now-34d}', timestamp '${now-34d}', '1');
INSERT INTO Session (channelId, id, sessionEnd, sessionStart, userId) VALUES (1, '3', timestamp '${now-10d}', timestamp '${now-10d}', '2');
INSERT INTO Session (channelId, id, sessionEnd, sessionStart, userId) VALUES (1, '4', timestamp '${now-36d}', timestamp '${now-36d}', '3');