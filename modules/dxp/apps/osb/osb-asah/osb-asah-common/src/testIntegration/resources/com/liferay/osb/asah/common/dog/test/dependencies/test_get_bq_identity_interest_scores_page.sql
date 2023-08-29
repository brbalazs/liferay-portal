INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '374790569167317525', '474790569167317529');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '386190546467211094', '374790572703144533');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '474790572703144534', '374790572703144534');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '374790575409131096', '274790575409131093');

INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', true, 2.614959778036198, 'javascript', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', true, 2.000000000000000, 'java', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', null, 1.7676619176489945, 'clicks-and-mortar e-tailers', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', null, 1.7676619176489945, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790572309620075', null, 1.7676619176489945, 'clicks-and-mortar e-tailers', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790572309620075', null, 2.1041341542702074, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '474790572703144534', null, 2.1041341542702074, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790575409131096', false, 0.7702225204735745, 'javascript', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790575409131096', null, 2.1041341542702074, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '386190546467211094', null, 1.4546849849874945, 'sales', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790575409131099', false, 0.01, 'javascript', DATE('2019-05-17'));