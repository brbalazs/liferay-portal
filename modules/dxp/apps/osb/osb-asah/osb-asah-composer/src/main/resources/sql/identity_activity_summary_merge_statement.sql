MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.identityactivitysummary` AS replica
USING
	(
		SELECT
			COUNT(*) AS activitiesCount,
			Event.channelId,
			Event.dataSourceId,
			Event.eventId,
			MIN(Event.eventDate) AS firstActivityDate,
			Event.userId AS identityId,
			MAX(Identity.individualId) AS individualId,
			MAX(Event.eventDate) AS lastActivityDate
		FROM
			`{{ dag.default_args['ac_project_id'] }}.event` AS Event
		LEFT JOIN `{{ dag.default_args['ac_project_id'] }}.identity` AS Identity ON (
			Event.userId = Identity.id
		)
		WHERE
			Event.eventDate BETWEEN TIMESTAMP('{{ data_interval_start.to_datetime_string() }}') AND TIMESTAMP('{{ data_interval_end.to_datetime_string() }}')
		GROUP BY
			Event.channelId,
			Event.dataSourceId,
			Event.eventId,
			Event.userId
	) AS staging
ON (
	staging.channelId = replica.channelId AND
	staging.dataSourceId = replica.dataSourceId AND
	staging.eventId = replica.eventId AND
	staging.identityId = replica.identityId
)

WHEN MATCHED THEN
	UPDATE SET
		replica.activitiesCount = replica.activitiesCount + staging.activitiesCount,
		replica.individualId = CASE WHEN staging.individualId IS NOT NULL THEN staging.individualId END,
		replica.lastActivityDate = staging.lastActivityDate
WHEN NOT MATCHED THEN
	INSERT (
		`activitiesCount`,
		`channelId`,
		`dataSourceId`,
		`eventId`,
		`firstActivityDate`,
		`identityId`,
		`individualId`,
		`lastActivityDate`
	)
	VALUES (
		staging.activitiesCount,
		staging.channelId,
		staging.dataSourceId,
		staging.eventId,
		staging.firstActivityDate,
		staging.identityId,
		staging.individualId,
		staging.lastActivityDate
	)