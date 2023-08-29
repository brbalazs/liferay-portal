INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '374790569167317525', 'c2ca75aa0f15bdaf918f704df63b6012bc8c92cf0000764f1016fd84b5d7e485');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '386190546467211094', 'bc617015299f7b6220f7242eee4b3495c388a6f8b219881bcbdffcc653b02a5d');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '474790572703144534', '9479c311bc1de5b2cfe72620ca953e412bf66b6f70a4b943b94a3690b28093a9');
INSERT INTO Identity_Raw (createDate, id, individualId) VALUES (timestamp '${today-1d}', '374790575409131096', '77f2cfd1adfa703b1dc7bef8643bfe3487c45632724c3ec0b0b1624a2a2b8095');

INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', true, 2.614959778036198, 'javascript', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', true, 2.000000000000000, 'java', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', null, 1.7676619176489945, 'clicks-and-mortar e-tailers', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790569167317525', null, 1.7676619176489945, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790572309620075', null, 1.7676619176489945, 'clicks-and-mortar e-tailers', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790572309620075', true, 2.1041341542702074, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '474790572703144534', true, 2.1041341542702074, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790575409131096', false, 0.7702225204735745, 'javascript', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790575409131096', true, 2.1041341542702074, 'compelling metrics', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '386190546467211094', null, 1.4546849849874945, 'sales', DATE('2019-05-17'));
INSERT INTO IdentityInterestScore (channelId, identityId, interested, interestScore, keyword, recordedDate) VALUES (1, '374790575409131099', false, 0.01, 'javascript', DATE('2019-05-17'));

INSERT INTO Suppression (emailAddress) VALUES ('test1@liferay.com');