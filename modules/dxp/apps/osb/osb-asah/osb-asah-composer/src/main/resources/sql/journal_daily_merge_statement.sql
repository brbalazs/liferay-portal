MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.journaldaily` AS replica
USING
	(
		SELECT
			assetId,
			assetTitle,
			browserName,
			canonicalUrl,
			channelId,
			city,
			country,
			deviceType,
			TIMESTAMP_TRUNC(eventDate, DAY, '{{ dag.default_args['ac_project_time_zone_id'] }}') AS eventDate,
			pageTitle,
			platformName,
			region,
			userId,
			SUM(views) AS views
		FROM
			`{{ dag.default_args['ac_project_id'] }}.journalhourly`
		WHERE
			eventDate BETWEEN TIMESTAMP('{{ data_interval_start.to_datetime_string() }}') AND TIMESTAMP('{{ data_interval_end.to_datetime_string() }}')
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
		`assetId`,
		`assetTitle`,
		`browserName`,
		`canonicalUrl`,
		`channelId`,
		`city`,
		`country`,
		`deviceType`,
		`eventDate`,
		`pageTitle`,
		`platformName`,
		`region`,
		`userId`,
		`views`
	)
	VALUES (
		staging.assetId,
		staging.assetTitle,
		staging.browserName,
		staging.canonicalUrl,
		staging.channelId,
		staging.city,
		staging.country,
		staging.deviceType,
		staging.eventDate,
		staging.pageTitle,
		staging.platformName,
		staging.region,
		staging.userId,
		staging.views
	)