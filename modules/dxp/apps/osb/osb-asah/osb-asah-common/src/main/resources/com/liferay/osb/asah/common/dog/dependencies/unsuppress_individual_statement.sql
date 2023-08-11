DECLARE new_identity_id STRING;

BEGIN
	BEGIN TRANSACTION;

	FOR target_identity IN (SELECT id FROM Identity_Raw WHERE individualId = individualId = '${individualId}')
	DO
		SET new_identity_id = GENERATE_UUID();

		UPDATE BQEvent SET userId = new_identity_id WHERE userId = target_identity.id AND eventDate >= timestamp '${startDate}';
		UPDATE BQIdentityInterestPage SET identityId = new_identity_id WHERE identityId = target_identity.id;
		UPDATE BQIdentityInterestScore SET identityId = new_identity_id WHERE identityId = target_identity.id AND recordedDate >= timestamp '${startDate}';
		UPDATE BQIdentity_Raw SET id = new_identity_id WHERE id = target_identity.id AND createDate >= timestamp '${startDate}';
		UPDATE BQIdentityActivitySummary SET identityId = new_identity_id WHERE identityId = target_identity.id AND firstActivityDate >= timestamp '${startDate}';
		UPDATE BQSession SET userId = new_identity_id WHERE userId = target_identity.id AND sessionStart >= timestamp '${startDate}';
		UPDATE BQSessionInterestScore SET identityId = new_identity_id WHERE identityId = target_identity.id AND recordedDate >= timestamp '${startDate}';
		UPDATE BlogDaily SET userId = new_identity_id WHERE userId = target_identity.id AND eventDate >= timestamp '${startDate}';
		UPDATE DocumentLibraryDaily SET userId = new_identity_id WHERE userId = target_identity.id AND eventDate >= timestamp '${startDate}';
		UPDATE FormDaily SET userId = new_identity_id WHERE userId = target_identity.id AND eventDate >= timestamp '${startDate}';
		UPDATE JournalDaily SET userId = new_identity_id WHERE userId = target_identity.id AND eventDate >= timestamp '${startDate}';
		UPDATE PageDaily SET userId = new_identity_id WHERE userId = target_identity.id AND eventDate >= timestamp '${startDate}';
    END FOR;

	DELETE FROM Suppression WHERE emailAddress = '${emailAddress}';

	UPDATE BQIndividual SET suppressed = false WHERE emailAddress = '${emailAddress}';

	UPDATE Identity_Raw SET individualId = NULL WHERE individualId = '${individual_id}' AND createDate >= timestamp '${startDate}';

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	SELECT @@error.message;
	ROLLBACK TRANSACTION;
END