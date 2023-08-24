BEGIN
	BEGIN TRANSACTION;

	-- Individual's personal information

	DELETE FROM BQExpandoValue WHERE CONCAT(classPK, dataSourceId) IN ( SELECT CONCAT(dxpUserId, dataSourceId) FROM BQUser WHERE individualId = '${individual_id}' ) AND classType = 'com.liferay.portal.kernel.model.User';
	DELETE FROM BQIndividual WHERE id = '${individual_id}';
	DELETE FROM DXPEntity WHERE CONCAT(classPK, dataSourceId) IN ( SELECT CONCAT(dxpUserId, dataSourceId) FROM BQUser WHERE individualId = '${individual_id}' ) AND type = 'com.liferay.portal.kernel.model.User';

	DELETE FROM BQUser WHERE individualId = '${individual_id}';

	-- Individual's activities anonymization

	UPDATE BQEvent SET emailAddressHashed = NULL WHERE emailAddressHashed = '${individual_id}';

	UPDATE BQIdentityActivitySummary SET individualId = NULL WHERE individualId = '${individual_id}';
	UPDATE BQIdentity_Raw SET individualId = NULL WHERE individualId = '${individual_id}';

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	SELECT @@error.message;
	ROLLBACK TRANSACTION;
END