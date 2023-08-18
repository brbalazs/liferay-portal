BEGIN
	BEGIN TRANSACTION;

    ${anonymize_activities_statement}

	DELETE FROM Suppression WHERE emailAddress = '${email_address}';

	UPDATE BQIndividual SET suppressed = false WHERE emailAddress = '${email_address}';

	UPDATE Identity_Raw SET individualId = NULL WHERE individualId = '${individual_id}' AND createDate >= timestamp '${start_date}';

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	SELECT @@error.message;
	ROLLBACK TRANSACTION;

	UPDATE Suppression SET hidden = false WHERE emailAddress = '${email_address}';
END
