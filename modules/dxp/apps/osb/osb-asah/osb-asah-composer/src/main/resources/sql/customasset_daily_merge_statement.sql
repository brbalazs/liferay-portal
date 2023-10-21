MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.customassetdaily` AS replica
USING
	(
		SELECT
			sum(abandonments) as abandonments,
			assetPrimaryKey,
			channelId,
			sum(clicks) as clicks,
			sum(downloads) as downloads,
			TIMESTAMP_TRUNC(eventDate, DAY, '{{ dag.default_args['ac_project_time_zone_id'] }}') AS eventDate,
			sum(readTime) as readTime,
			sum(sessions) as sessions,
			sum(submissions) as submissions,
			sum(submissionsTime) as submissionsTime,
			sum(views) as views
		FROM
			`{{ dag.default_args['ac_project_id'] }}.customassethourly`
		WHERE
			eventDate BETWEEN TIMESTAMP('{{ data_interval_start.to_datetime_string() }}') AND TIMESTAMP('{{ data_interval_end.to_datetime_string() }}')
		GROUP BY
			assetPrimaryKey, channelId, eventDate
	) AS staging
ON (
	DATE(replica.eventDate, '{{ dag.default_args['ac_project_time_zone_id'] }}') = DATE(TIMESTAMP('{{ data_interval_start.to_datetime_string() }}'), '{{ dag.default_args['ac_project_time_zone_id'] }}') AND
	staging.assetPrimaryKey = replica.assetPrimaryKey AND
	staging.channelId = replica.channelId AND
	DATE(staging.eventDate) = DATE(replica.eventDate)
)

WHEN NOT MATCHED THEN
	INSERT (
		`abandonments`,
		`assetPrimaryKey`,
		`channelId`,
		`clicks`,
		`downloads`,
		`eventDate`,
		`readTime`,
		`sessions`,
		`submissions`,
		`submissionsTime`,
		`views`
	)
	VALUES (
		staging.abandonments,
		staging.assetPrimaryKey,
		staging.channelId,
		staging.clicks,
		staging.downloads,
		staging.eventDate,
		staging.readTime,
		staging.sessions,
		staging.submissions,
		staging.submissionsTime,
		staging.views
	)