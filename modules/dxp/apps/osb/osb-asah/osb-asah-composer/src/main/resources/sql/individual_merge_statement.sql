MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.individual` AS replica
USING
	(
		WITH BQUserExpandoFields AS (
			SELECT
				expandoValue.dataSourceId,
				user.emailAddress,
				user.modifiedDate,
				expandoValue.fieldName AS name,
				expandoValue.value
			FROM
				`{{ dag.default_args['ac_project_id'] }}.expandovalue` AS expandoValue
			INNER JOIN
				`{{ dag.default_args['ac_project_id'] }}.expandocolumn` AS expandoColumn
			ON
				expandoValue.columnId = expandoColumn.columnId
			INNER JOIN
				`{{ dag.default_args['ac_project_id'] }}.user` AS user
			ON
				expandoValue.classPK = CAST(user.dxpUserId AS STRING)
			WHERE
				value IS NOT NULL AND value != ''
		),
		BQUserFields AS (
			SELECT
				* EXCEPT(fields)
			FROM
				`{{ dag.default_args['ac_project_id'] }}.user`,
				UNNEST(fields)
		),
		BQUserMemberships AS (
			SELECT
				dataSourceId,
				dxpUserId,
				(
					SELECT
						value
					FROM
						UNNEST(fields)
					WHERE
						name = 'groupIds'
				) AS groupIds,
				individualId,
				(
					SELECT
						value
					FROM
						UNNEST(fields)
					WHERE
						name = 'organizationIds'
				) AS organizationIds,
				(
					SELECT
						value
					FROM
						UNNEST(fields)
					WHERE
						name = 'roleIds'
				) AS roleIds,
				(
					SELECT
						value
					FROM
						UNNEST(fields)
					WHERE
						name = 'teamIds'
				) AS teamIds,
				(
					SELECT
						value
					FROM
						UNNEST(fields)
					WHERE
						name = 'userGroupIds'
				) AS userGroupIds
			FROM
				`{{ dag.default_args['ac_project_id'] }}.user`
			WHERE
				emailAddress != 'default@liferay.com'
		),
		BQGroupIds AS (
			SELECT
				BQGroup.* EXCEPT(groupIds),
				Group_.id AS bqGroupId,
				groupId
			FROM (
				SELECT
					* EXCEPT(groupIds),
					JSON_EXTRACT_ARRAY(groupIds, '$') AS groupIds
				FROM
					BQUserMemberships
			) AS BQGroup,
			UNNEST(groupIds) groupId
			JOIN
				`{{ dag.default_args['ac_project_id'] }}.group`AS Group_
			ON
				CAST(groupId AS STRING) = CAST(Group_.groupId AS STRING) AND
				BQGroup.dataSourceId = Group_.dataSourceId
		),
		BQOrganizationIds AS (
			SELECT
				BQOrganization.* EXCEPT(organizationIds),
				Organization.id AS bqOrganizationId,
				organizationId
			FROM (
				SELECT
					* EXCEPT(organizationIds),
					JSON_EXTRACT_ARRAY(organizationIds, '$') AS organizationIds
				FROM
					BQUserMemberships
			) AS BQOrganization,
			UNNEST(organizationIds) organizationId
			JOIN
				`{{ dag.default_args['ac_project_id'] }}.organization` AS Organization
			ON
				CAST(organizationId AS STRING) = CAST(Organization.organizationId AS STRING) AND
				BQOrganization.dataSourceId = Organization.dataSourceId
		),
		BQRoleIds AS (
			SELECT
				BQRole.* EXCEPT(roleIds),
				Role.id AS bqRoleId,
				roleId
			FROM (
				SELECT
					* EXCEPT(roleIds),
					JSON_EXTRACT_ARRAY(roleIds, '$') AS roleIds
				FROM
					BQUserMemberships
			) AS BQRole,
			UNNEST(roleIds) roleId
			JOIN
				`{{ dag.default_args['ac_project_id'] }}.role` AS Role
			ON
				CAST(roleId AS STRING) = CAST(Role.roleId AS STRING) AND
				BQRole.dataSourceId = Role.dataSourceId
		),
		BQTeamIds AS (
			SELECT
				BQTeam.* EXCEPT(teamIds),
				Team.id AS bqTeamId,
				teamId
			FROM (
				SELECT
					* EXCEPT(teamIds),
					JSON_EXTRACT_ARRAY(teamIds, '$') AS teamIds
				FROM
					BQUserMemberships
			) AS BQTeam,
			UNNEST(teamIds) teamId
			JOIN
				`{{ dag.default_args['ac_project_id'] }}.team` AS Team
			ON
				CAST(teamId AS STRING) = CAST(Team.teamId AS STRING) AND
				BQTeam.dataSourceId = Team.dataSourceId
		),
		BQUserGroupIds AS (
			SELECT
				BQUserGroup.* EXCEPT(userGroupIds),
				UserGroup.id AS bqUserGroupId,
				userGroupId
			FROM (
				SELECT
					* EXCEPT(userGroupIds),
					JSON_EXTRACT_ARRAY(userGroupIds, '$') AS userGroupIds
				FROM
					BQUserMemberships
			) AS BQUserGroup,
			UNNEST(userGroupIds) userGroupId
			JOIN
				`{{ dag.default_args['ac_project_id'] }}.usergroup` AS UserGroup
			ON
				CAST(userGroupId AS STRING) = CAST(UserGroup.userGroupId AS STRING) AND
				BQUserGroup.dataSourceId = UserGroup.dataSourceId
		),
		BQMemberships AS (
			SELECT
				User.emailAddress,
				ARRAY_AGG(
					STRUCT(MembershipIds.ids, MembershipIds.name)
				) AS memberships
			FROM
				`{{ dag.default_args['ac_project_id'] }}.user` AS User
			JOIN
			(
				SELECT
					dataSourceId,
					dxpUserId,
					ARRAY_AGG(DISTINCT BQGroupIds.bqGroupId) AS ids,
					'groupIds' AS name
				FROM
					BQGroupIds
				GROUP BY
					BQGroupIds.dataSourceId, BQGroupIds.dxpUserId
				UNION ALL
				SELECT
					dataSourceId,
					dxpUserId,
					ARRAY_AGG(DISTINCT BQOrganizationIds.bqOrganizationId) AS ids,
					'organizationIds' AS name
				FROM
					BQOrganizationIds
				GROUP BY
					BQOrganizationIds.dataSourceId, BQOrganizationIds.dxpUserId
				UNION ALL
				SELECT
					dataSourceId,
					dxpUserId,
					ARRAY_AGG(DISTINCT BQRoleIds.bqRoleId) AS ids,
					'roleIds' AS name
				FROM
					BQRoleIds
				GROUP BY
					BQRoleIds.dataSourceId, BQRoleIds.dxpUserId
				UNION ALL
				SELECT
					dataSourceId,
					dxpUserId,
					ARRAY_AGG(DISTINCT BQTeamIds.bqTeamId) AS ids,
					'teamIds' AS name
				FROM
					BQTeamIds
				GROUP BY
					BQTeamIds.dataSourceId, BQTeamIds.dxpUserId
				UNION ALL
				SELECT
					dataSourceId,
					dxpUserId,
					ARRAY_AGG(DISTINCT BQUserGroupIds.bqUserGroupId) AS ids,
					'userGroupIds' AS name
				FROM
					BQUserGroupIds
				GROUP BY
					BQUserGroupIds.dataSourceId, BQUserGroupIds.dxpUserId
			) AS MembershipIds
			ON
				MembershipIds.dataSourceId = User.dataSourceId AND
				MembershipIds.dxpUserId = User.dxpUserId
			WHERE
				User.emailAddress != 'default@liferay.com'
			GROUP BY
				User.emailAddress
		)

		SELECT
			BQStagingFields.emailAddress,
			(
				SELECT
					CASE WHEN SAFE_CAST(value AS INT64) BETWEEN -62135596800000 AND 253402300799999 THEN TIMESTAMP_MILLIS(SAFE_CAST(value AS INT64)) ELSE null END
				FROM
					UNNEST(stagingFields)
				WHERE
					name = 'birthday'
			) AS birthday,
			(
				SELECT
					SAFE_CAST(value AS STRING)
				FROM
					UNNEST(stagingFields)
				WHERE
					name = 'firstName'
			) AS firstName,
			(
				SELECT
					SAFE_CAST(value AS STRING)
				FROM
					UNNEST(stagingFields)
				WHERE
					name = 'jobTitle'
			) AS jobTitle,
			(
				SELECT
					SAFE_CAST(value AS STRING)
				FROM
					UNNEST(stagingFields)
				WHERE
					name = 'languageId'
			) AS languageId,
			(
				SELECT
					SAFE_CAST(value AS STRING)
				FROM
					UNNEST(stagingFields)
				WHERE
					name = 'lastName'
			) AS lastName,
			BQMemberships.memberships,
			(
				SELECT
					SAFE_CAST(value AS STRING)
				FROM
					UNNEST(stagingFields)
				WHERE
					name = 'middleName'
			) AS middleName,
			modifiedDate,
			(
				SELECT
					SAFE_CAST(value AS STRING)
				FROM
					UNNEST(stagingFields)
				WHERE
					name = 'screenName'
			) AS screenName,
			ARRAY(
				SELECT AS STRUCT
					dataSourceId,
					name,
					value
				FROM
					UNNEST(stagingFields)
			) AS stagingFields
		FROM (
			SELECT
				emailAddress,
				MAX(modifiedDate) AS modifiedDate,
				ARRAY_AGG(
					STRUCT(dataSourceId, name, value)
				) AS stagingFields
			FROM (
				SELECT
					dataSourceId,
					emailAddress,
					modifiedDate,
					name,
					value,
					ROW_NUMBER() OVER(
						PARTITION BY
							emailAddress,
							name
						ORDER BY
							modifiedDate DESC
					) AS rowNumber
				FROM (
					SELECT
						dataSourceId,
						emailAddress,
						modifiedDate,
						name,
						value
					FROM
						BQUserExpandoFields
					UNION ALL
					SELECT
						dataSourceId,
						emailAddress,
						modifiedDate,
						name,
						value
					FROM
						BQUserFields
				)
			)
			WHERE
				emailAddress != 'default@liferay.com' AND
				rowNumber = 1
			GROUP BY
				emailAddress
		) AS BQStagingFields
		LEFT JOIN
			BQMemberships
		ON
			BQStagingFields.emailAddress = BQMemberships.emailAddress
	) AS staging
ON
	LOWER(replica.emailAddress) = staging.emailAddress
WHEN MATCHED THEN
	UPDATE SET
		replica.birthday = staging.birthday,
		replica.firstName = staging.firstName,
		replica.fields = staging.stagingFields,
		replica.jobTitle = staging.jobTitle,
		replica.languageId = staging.languageId,
		replica.lastName = staging.lastName,
		replica.memberships = staging.memberships,
		replica.middleName = staging.middleName,
		replica.modifiedDate = staging.modifiedDate,
		replica.screenName = staging.screenName
WHEN NOT MATCHED BY TARGET THEN
	INSERT(
		`birthday`,
		`createDate`,
		`emailAddress`,
		`firstName`,
		`fields`,
		`id`,
		`jobTitle`,
		`languageId`,
		`lastName`,
		`memberships`,
		`middleName`,
		`modifiedDate`,
		`screenName`
	)
	VALUES (
		staging.birthday,
		staging.modifiedDate,
		LOWER(staging.emailAddress),
		staging.firstName,
		staging.stagingFields,
		TO_HEX(SHA256(LOWER(staging.emailAddress))),
		staging.jobTitle,
		staging.languageId,
		staging.lastName,
		staging.memberships,
		staging.middleName,
		staging.modifiedDate,
		staging.screenName
	)
WHEN NOT MATCHED BY SOURCE THEN
	DELETE