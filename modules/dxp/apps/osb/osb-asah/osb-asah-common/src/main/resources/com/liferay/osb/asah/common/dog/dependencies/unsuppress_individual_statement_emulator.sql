BEGIN
	BEGIN TRANSACTION;

    ${anonymize_activities_statement}

	DELETE FROM Suppression WHERE emailAddress = '${emailAddress}';

	UPDATE BQIndividual SET suppressed = false WHERE emailAddress = '${emailAddress}';

	UPDATE Identity_Raw SET individualId = NULL WHERE individualId = '${individual_id}' AND createDate >= timestamp '${startDate}';

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	UPDATE Suppression SET hidden = false WHERE emailAddress = '${emailAddress}';

	SELECT @@error.message;
	ROLLBACK TRANSACTION;
END
