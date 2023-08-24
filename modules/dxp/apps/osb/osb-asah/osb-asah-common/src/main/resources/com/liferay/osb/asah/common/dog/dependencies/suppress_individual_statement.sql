BEGIN
	BEGIN TRANSACTION;

	UPDATE BQEvent SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND eventDate <= timestamp '${range_end_date}';
	UPDATE BQIdentityInterestPage SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}');
	UPDATE BQIdentityInterestScore SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND recordedDate <= DATE('${range_end_date}');
	UPDATE BQIdentityActivitySummary SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND firstActivityDate <= timestamp '${range_end_date}';
	UPDATE BQSession SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND sessionStart <= timestamp '${range_end_date}';
	UPDATE BQSessionInterestScore SET identityId = '${new_identity_id}' WHERE identityId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND recordedDate <= DATE('${range_end_date}');
	UPDATE BlogDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND eventDate <= timestamp '${range_end_date}';
	UPDATE DocumentLibraryDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND eventDate <= timestamp '${range_end_date}';
	UPDATE FormDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND eventDate <= timestamp '${range_end_date}';
	UPDATE JournalDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND eventDate <= timestamp '${range_end_date}';
	UPDATE PageDaily SET userId = '${new_identity_id}' WHERE userId IN (SELECT id FROM BQIdentity WHERE individualId = '${individual_id}') AND eventDate <= timestamp '${range_end_date}';

	UPDATE BQIdentity_Raw SET id = '${new_identity_id}' WHERE individualId = '${individual_id}' AND createDate <= timestamp '${range_end_date}';

	UPDATE BQIndividual SET suppressed = TRUE WHERE id = '${individual_id}';

	${delete_membership_statement}
	INSERT INTO Suppression (createDate, dataControlTaskBatchId, dataControlTaskCreateDate, emailAddress, hidden) VALUES (CURRENT_TIMESTAMP(), ${data_control_task_batch_id}, timestamp '${data_control_task_create_date}', '${email_address}', false);
	COMMIT TRANSACTION;

EXCEPTION WHEN ERROR THEN
	ROLLBACK TRANSACTION;

	SELECT ERROR(@@error.message);
END