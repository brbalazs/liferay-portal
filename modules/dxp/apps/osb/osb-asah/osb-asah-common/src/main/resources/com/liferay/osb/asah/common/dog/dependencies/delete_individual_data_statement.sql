BEGIN
	BEGIN TRANSACTION;

	-- Individual's personal information

	DELETE FROM BQExpandoValue WHERE CONCAT(classPK, dataSourceId) IN ( SELECT CONCAT(dxpUserId, dataSourceId) FROM BQUser WHERE individualId = '${individual_id}' ) AND classType = 'com.liferay.portal.kernel.model.User';
	DELETE FROM BQIndividual WHERE id = '${individual_id}';
	DELETE FROM DXPEntity WHERE CONCAT(classPK, dataSourceId) IN ( SELECT CONCAT(dxpUserId, dataSourceId) FROM BQUser WHERE individualId = '${individual_id}' ) AND type = 'com.liferay.portal.kernel.model.User';

	DELETE FROM BQUser WHERE individualId = '${individual_id}';

	-- Individual's activities anonymization

	UPDATE BQEvent SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE BQIdentityInterestPage SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE BQIdentityInterestScore SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE BQIdentityActivitySummary SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE BQSession SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE BQSessionInterestScore SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE BlogDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE DocumentLibraryDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE FormDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE JournalDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');
	UPDATE PageDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individualId}');

	UPDATE BQIdentity_Raw SET id = '${new_identity_id}' WHERE individualId = '${individualId}';

	UPDATE BQIdentity_Raw SET individualId = NULL WHERE individualId = '${individual_id}';
	UPDATE BQIdentityActivitySummary SET individualId = NULL WHERE individualId = '${individual_id}';

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	SELECT @@error.message;
	ROLLBACK TRANSACTION;
END