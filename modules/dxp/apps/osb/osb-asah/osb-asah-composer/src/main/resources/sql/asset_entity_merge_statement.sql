MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.assetentity` AS replica
USING
	(
		SELECT
			analyticsDeleteMessage.deleted,
			assetentity.*
		FROM (
			SELECT
				*
			FROM (
				SELECT
					*,
					ROW_NUMBER() OVER (
						PARTITION BY
							datasourceId, assetEntryId
						ORDER BY
							uploadDate DESC
					) AS rowNumber
				FROM
					`{{ dag.default_args['ac_project_id'] }}.assetentity_raw`
			)
			WHERE
				rowNumber = 1
		) AS assetentity
		LEFT JOIN (
			SELECT
				*
			FROM (
				SELECT
					dataSourceId,
					TRUE AS deleted,
					projectId,
					(
						SELECT
							SAFE_CAST(value AS INT64)
						FROM
							UNNEST(fields)
						WHERE
							name = 'classPK'
					) AS classPK,
					(
						SELECT
							SAFE_CAST(value AS STRING)
						FROM
							UNNEST(fields)
						WHERE
							name = 'className'
					) AS className
				FROM
					`{{ dag.default_args['ac_project_id'] }}.dxpentity`
				WHERE
					type = 'com.liferay.analytics.message.storage.model.AnalyticsDeleteMessage'
			)
			WHERE
				className = 'com.liferay.asset.kernel.model.AssetEntry'
		) AS analyticsDeleteMessage
		ON
			assetentity.datasourceId = analyticsDeleteMessage.datasourceId AND
			assetentity.assetEntryId = analyticsDeleteMessage.classPK AND
			assetentity.projectId = analyticsDeleteMessage.projectId
		WHERE
			assetentity.uploadDate >=
				{% if prev_start_date_success is not none %}
					'{{ prev_start_date_success }}'
				{% else %}
					'1970-01-01T00:00:00'
				{% endif %}
		) AS staging
ON
	staging.dataSourceId = replica.dataSourceId AND
	staging.assetEntryId = replica.assetEntryId AND
	staging.projectId = replica.projectId
WHEN MATCHED AND staging.deleted IS NULL AND staging.modifiedDate > replica.modifiedDate THEN
	UPDATE SET
		replica.categoryIds = staging.categoryIds,
		replica.channelId = staging.channelId,
		replica.classTypeId = staging.classTypeId,
		replica.classTypeName = staging.classTypeName,
		replica.expirationDate = staging.expirationDate,
		replica.groupId = staging.groupId,
		replica.modifiedDate = staging.modifiedDate,
		replica.publishDate = staging.publishDate,
		replica.tags = staging.tags,
		replica.title = staging.title
WHEN MATCHED AND staging.deleted = true THEN
	DELETE
WHEN NOT MATCHED BY TARGET AND staging.deleted IS NULL THEN
	INSERT (
		`assetEntryId`,
		`categoryIds`,
		`channelId`,
		`className`,
		`classPK`,
		`classTypeId`,
		`classTypeName`,
		`createDate`,
		`dataSourceId`,
		`expirationDate`,
		`groupId`,
		`modifiedDate`,
		`projectId`,
		`publishDate`,
		`tags`,
		`title`
	)
	VALUES (
		staging.assetEntryId,
		staging.categoryIds,
		staging.channelId,
		staging.className,
		staging.classPK,
		staging.classTypeId,
		staging.classTypeName,
		staging.createDate,
		staging.dataSourceId,
		staging.expirationDate,
		staging.groupId,
		staging.modifiedDate,
		staging.projectId,
		staging.publishDate,
		staging.tags,
		staging.title
	)