MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.formdaily` AS replica
USING
	(
		SELECT
			SUM(abandonments) AS abandonments,
			assetId,
			assetTitle,
			browserName,
			canonicalUrl,
			channelId,
			city,
			country,
			deviceType,
			TIMESTAMP_TRUNC(eventDate, DAY, '{{ dag.default_args['ac_project_time_zone_id'] }}') AS eventDate,
			SUM(finalizedFormViews) AS finalizedFormViews,
			pageTitle,
			platformName,
			region,
			SUM(submissions) AS submissions,
			SUM(submissionsTime) AS submissionsTime,
			userId,
			SUM(views) AS views
		FROM
			`{{ dag.default_args['ac_project_id'] }}.formhourly`
		WHERE
			DATE(eventDate, '{{ dag.default_args['ac_project_time_zone_id'] }}') = DATE(TIMESTAMP('{{ data_interval_start.to_datetime_string() }}'), '{{ dag.default_args['ac_project_time_zone_id'] }}')
		GROUP BY
			assetId, assetTitle, browserName, canonicalUrl, channelId, city,
			country, deviceType, eventDate, pageTitle, platformName, region,
			userId
	) AS staging
ON (
	DATE(replica.eventDate, '{{ dag.default_args['ac_project_time_zone_id'] }}') = DATE(TIMESTAMP('{{ data_interval_start.to_datetime_string() }}'), '{{ dag.default_args['ac_project_time_zone_id'] }}') AND
	staging.assetId = replica.assetId AND
	staging.assetTitle = replica.assetTitle AND
	COALESCE(staging.browserName, '') = COALESCE(replica.browserName, '') AND
	staging.channelId = replica.channelId AND
	COALESCE(staging.city, '') = COALESCE(replica.city, '') AND
	COALESCE(staging.country, '') = COALESCE(replica.country, '') AND
	COALESCE(staging.deviceType, '') = COALESCE(replica.deviceType, '') AND
	DATE(staging.eventDate) = DATE(replica.eventDate) AND
	staging.pageTitle = replica.pageTitle AND
	COALESCE(staging.platformName, '') = COALESCE(replica.platformName, '') AND
	COALESCE(staging.region, '') = COALESCE(replica.region, '') AND
	staging.userId = replica.userId
)

WHEN NOT MATCHED THEN
	INSERT (
		`abandonments`,
		`assetId`,
		`assetTitle`,
		`browserName`,
		`canonicalUrl`,
		`channelId`,
		`city`,
		`country`,
		`deviceType`,
		`eventDate`,
		`finalizedFormViews`,
		`pageTitle`,
		`platformName`,
		`region`,
		`submissions`,
		`submissionsTime`,
		`userId`,
		`views`
	)
	VALUES (
		staging.abandonments,
		staging.assetId,
		staging.assetTitle,
		staging.browserName,
		staging.canonicalUrl,
		staging.channelId,
		staging.city,
		staging.country,
		staging.deviceType,
		staging.eventDate,
		staging.finalizedFormViews,
		staging.pageTitle,
		staging.platformName,
		staging.region,
		staging.submissions,
		staging.submissionsTime,
		staging.userId,
		staging.views
	)