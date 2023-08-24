UPDATE BQEvent SET userId = '${new_identity_id}' WHERE userId = '${old_identity_id}' AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
UPDATE BQIdentityActivitySummary SET identityId = '${new_identity_id}' WHERE identityId = '${old_identity_id}' AND firstActivityDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
UPDATE BQIdentityInterestPage SET identityId = '${new_identity_id}' WHERE identityId = '${old_identity_id}';
UPDATE BQIdentityInterestScore SET identityId = '${new_identity_id}' WHERE identityId = '${old_identity_id}' AND recordedDate BETWEEN DATE('${range_start_date}') AND DATE('${range_end_date}');
UPDATE BQSession SET userId = '${new_identity_id}' WHERE userId = '${old_identity_id}' AND sessionStart BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
UPDATE BQSessionInterestScore SET identityId = '${new_identity_id}' WHERE identityId = '${old_identity_id}' AND recordedDate BETWEEN DATE('${range_start_date}') AND DATE('${range_end_date}');
UPDATE BlogDaily SET userId = '${new_identity_id}' WHERE userId = '${old_identity_id}' AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
UPDATE DocumentLibraryDaily SET userId = '${new_identity_id}' WHERE userId = '${old_identity_id}' AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
UPDATE FormDaily SET userId = '${new_identity_id}' WHERE userId = '${old_identity_id}' AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
UPDATE JournalDaily SET userId = '${new_identity_id}' WHERE userId = '${old_identity_id}' AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';
UPDATE PageDaily SET userId = '${new_identity_id}' WHERE userId = '${old_identity_id}' AND eventDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';

UPDATE BQIdentity_Raw SET id = '${new_identity_id}' WHERE id = '${old_identity_id}' AND createDate BETWEEN timestamp '${range_start_date}' AND timestamp '${range_end_date}';