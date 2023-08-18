BEGIN
	BEGIN TRANSACTION;

	UPDATE BQEvent SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE BQIdentityInterestPage SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE BQIdentityInterestScore SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE BQIdentityActivitySummary SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE BQSession SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE BQSessionInterestScore SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE BlogDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE DocumentLibraryDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE FormDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE JournalDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE PageDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');

	UPDATE BQIdentity_Raw SET id = '${new_identity_id}' WHERE individualId = '${individual_id}';

	UPDATE BQIndividual SET suppressed = TRUE WHERE id = '${individual_id}';

	${delete_membership_statement}

	INSERT INTO Suppression (createDate, dataControlTaskBatchId, dataControlTaskCreateDate, emailAddress, hidden) VALUES (CURRENT_TIMESTAMP(), ${data_control_task_batch_id}, timestamp '${data_control_task_create_date}', '${email_address}', false);

	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	SELECT @@error.message;
	ROLLBACK TRANSACTION;
END