DECLARE new_identity_id STRING;
BEGIN
	BEGIN TRANSACTION;
	FOR Identity IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}')
	DO
		SET new_identity_id = GENERATE_UUID();

		UPDATE BQEvent SET userId = new_identity_id WHERE userId = Identity.id AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
		UPDATE BQIdentityInterestPage SET identityId = new_identity_id WHERE identityId = Identity.id;
		UPDATE BQIdentityInterestScore SET identityId = new_identity_id WHERE identityId = Identity.id AND recordedDate BETWEEN DATE('${range_start_date}') AND DATE('${range_end_date}');
		UPDATE BQIdentityActivitySummary SET identityId = new_identity_id WHERE identityId = Identity.id AND firstActivityDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
		UPDATE BQSession SET userId = new_identity_id WHERE userId = Identity.id AND sessionStart BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
		UPDATE BQSessionInterestScore SET identityId = new_identity_id WHERE identityId = Identity.id AND recordedDate BETWEEN DATE('${range_start_date}') AND DATE('${range_end_date}');
		UPDATE BlogDaily SET userId = new_identity_id WHERE userId = Identity.id AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
		UPDATE DocumentLibraryDaily SET userId = new_identity_id WHERE userId = Identity.id AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
		UPDATE FormDaily SET userId = new_identity_id WHERE userId = Identity.id AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
		UPDATE JournalDaily SET userId = new_identity_id WHERE userId = Identity.id AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
		UPDATE PageDaily SET userId = new_identity_id WHERE userId = Identity.id AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';

		UPDATE BQIdentity_Raw SET id = new_identity_id WHERE id = Identity.id AND createDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
	END FOR;

	DELETE FROM Suppression WHERE emailAddress = '${email_address}';

	UPDATE BQIndividual SET suppressed = false WHERE emailAddress = '${email_address}';

	UPDATE Identity_Raw SET individualId = NULL WHERE individualId = '${individual_id}' AND createDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	ROLLBACK TRANSACTION;

	UPDATE Suppression SET hidden = false WHERE emailAddress = '${email_address}';

	SELECT ERROR(@@error.message);
END