MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.documentlibrarydaily` AS replica
USING
	(
		SELECT
			assetId,
			assetTitle,
			browserName,
			canonicalUrl,
			channelId,
			city,
			SUM(comments) AS comments,
			country,
			deviceType,
			SUM(downloads) AS downloads,
			TIMESTAMP_TRUNC(eventDate, DAY, '{{ dag.default_args['ac_project_time_zone_id'] }}') AS eventDate,
			pageTitle,
			platformName,
			SUM(previews) AS previews,
			SUM(ratings) AS ratings,
			SUM(ratingsScore) AS ratingsScore,
			region,
			userId
		FROM
			`{{ dag.default_args['ac_project_id'] }}.documentlibraryhourly`
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
		`comments`,
		`country`,
		`deviceType`,
		`downloads`,
		`eventDate`,
		`pageTitle`,
		`platformName`,
		`previews`,
		`ratings`,
		`ratingsScore`,
		`region`,
		`userId`
	)
	VALUES (
		staging.assetId,
		staging.assetTitle,
		staging.browserName,
		staging.canonicalUrl,
		staging.channelId,
		staging.city,
		staging.comments,
		staging.country,
		staging.deviceType,
		staging.downloads,
		staging.eventDate,
		staging.pageTitle,
		staging.platformName,
		staging.previews,
		staging.ratings,
		staging.ratingsScore,
		staging.region,
		staging.userId
	)