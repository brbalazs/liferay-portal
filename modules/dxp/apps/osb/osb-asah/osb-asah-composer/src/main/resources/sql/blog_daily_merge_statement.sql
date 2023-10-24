MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.blogdaily` AS replica
USING
	(
		SELECT
			assetId,
			assetTitle,
			browserName,
			canonicalUrl,
			channelId,
			city,
			SUM(clicks) AS clicks,
			SUM(comments) AS comments,
			country,
			deviceType,
			TIMESTAMP_TRUNC(eventDate, DAY, '{{ dag.default_args['ac_project_time_zone_id'] }}') AS eventDate,
			pageTitle,
			platformName,
			SUM(ratings) AS ratings,
			SUM(ratingsScore) AS ratingsScore,
			SUM(readTime) AS readTime,
			region,
			SUM(sessions) AS sessions,
			userId,
			SUM(views) AS views
		FROM
			`{{ dag.default_args['ac_project_id'] }}.bloghourly`
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
		`assetId`,
		`assetTitle`,
		`browserName`,
		`canonicalUrl`,
		`channelId`,
		`city`,
		`clicks`,
		`comments`,
		`country`,
		`deviceType`,
		`eventDate`,
		`pageTitle`,
		`platformName`,
		`ratings`,
		`ratingsScore`,
		`readTime`,
		`region`,
		`sessions`,
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
		staging.clicks,
		staging.comments,
		staging.country,
		staging.deviceType,
		staging.eventDate,
		staging.pageTitle,
		staging.platformName,
		staging.ratings,
		staging.ratingsScore,
		staging.readTime,
		staging.region,
		staging.sessions,
		staging.userId,
		staging.views
	)