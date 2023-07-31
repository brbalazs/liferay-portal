BEGIN
	BEGIN TRANSACTION;

	-- Individual's personal information

	MERGE
		DXPEntity AS target
	USING (
		SELECT
			CAST(dxpUserId AS STRING) AS classPK, dataSourceId
		FROM
			BQUser
		WHERE
			individualId = '${individual_id}'
	) AS source
	ON (
		target.classPK = source.classPK AND
		target.dataSourceId = source.dataSourceId AND
		target.type = 'com.liferay.portal.kernel.model.User'
	)
	WHEN MATCHED THEN
		DELETE;

	MERGE
	    BQExpandoValue AS target
	USING (
		SELECT
			CAST(dxpUserId AS STRING) AS classPK, dataSourceId
	    FROM
			BQUser
		WHERE
			individualId = '${individual_id}'
	) AS source
	ON (
		target.dataSourceId = source.dataSourceId AND
	    target.classPK = source.classPK AND
	    target.classType = 'com.liferay.portal.kernel.model.User'
	)
	WHEN MATCHED THEN
		DELETE;

	DELETE FROM BQIndividual WHERE id = '${individual_id}';
	DELETE FROM BQUser WHERE individualId = '${individual_id}';

	-- Individual's activities anonymization

	UPDATE Identity_Raw SET individualId = NULL WHERE individualId = '${individual_id}';
	UPDATE BQIdentityActivitySummary SET individualId = NULL WHERE individualId = '${individual_id}';

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	SELECT @@error.message;
	ROLLBACK TRANSACTION;
END