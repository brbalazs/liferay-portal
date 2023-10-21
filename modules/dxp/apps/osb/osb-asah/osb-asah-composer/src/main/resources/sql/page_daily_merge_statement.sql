MERGE INTO
	`{{ dag.default_args['ac_project_id'] }}.pagedaily` AS replica
USING
	(
		SELECT
			SUM(bounce) AS bounce,
			browserName,
			canonicalUrl,
			channelId,
			city,
			country,
			SUM(ctaClicks) AS ctaClicks,
			description,
			deviceType,
			SUM(directAccess) AS directAccess,
			SUM(entrances) AS entrances,
			TIMESTAMP_TRUNC(eventDate, DAY, '{{ dag.default_args['ac_project_time_zone_id'] }}') AS eventDate,
			SUM(exits) exits,
			ANY_VALUE(experimentId) AS experimentId,
			SUM(indirectAccess) AS indirectAccess,
			platformName,
			SUM(reads) AS reads,
			region,
			sessionId,
			SUM(timeOnPage) AS timeOnPage,
			title,
			userId,
			ANY_VALUE(variantId) AS variantId,
			SUM(views) AS views
		FROM
			`{{ dag.default_args['ac_project_id'] }}.pagehourly`
		WHERE
			eventDate BETWEEN TIMESTAMP('{{ data_interval_start.to_datetime_string() }}') AND TIMESTAMP('{{ data_interval_end.to_datetime_string() }}') AND
			sessionId IS NOT NULL
		GROUP BY
			browserName, canonicalUrl, channelId, city, country, description,
			deviceType, eventDate, platformName, region, sessionId, title,
			userId
	) AS staging
ON (
	DATE(replica.eventDate, '{{ dag.default_args['ac_project_time_zone_id'] }}') = DATE(TIMESTAMP('{{ data_interval_start.to_datetime_string() }}'), '{{ dag.default_args['ac_project_time_zone_id'] }}') AND
	COALESCE(staging.browserName, '') = COALESCE(replica.browserName, '') AND
	staging.canonicalUrl = replica.canonicalUrl AND
	staging.channelId = replica.channelId AND
	COALESCE(staging.city, '') = COALESCE(replica.city, '') AND
	COALESCE(staging.country, '') = COALESCE(replica.country, '') AND
	COALESCE(staging.deviceType, '') = COALESCE(replica.deviceType, '') AND
	DATE(staging.eventDate) = DATE(replica.eventDate) AND
	COALESCE(staging.platformName, '') = COALESCE(replica.platformName, '') AND
	COALESCE(staging.region, '') = COALESCE(replica.region, '') AND
	staging.sessionId = replica.sessionId AND
	staging.title = replica.title AND
	staging.userId = replica.userId
)

WHEN NOT MATCHED THEN
	INSERT (
		`bounce`,
		`browserName`,
		`canonicalUrl`,
		`channelId`,
		`city`,
		`country`,
		`ctaClicks`,
		`description`,
		`deviceType`,
		`directAccess`,
		`entrances`,
		`eventDate`,
		`exits`,
		`experimentId`,
		`indirectAccess`,
		`platformName`,
		`reads`,
		`region`,
		`sessionId`,
		`timeOnPage`,
		`title`,
		`userId`,
		`variantId`,
		`views`
	)
	VALUES (
		staging.bounce,
		staging.browserName,
		staging.canonicalUrl,
		staging.channelId,
		staging.city,
		staging.country,
		staging.ctaClicks,
		staging.description,
		staging.deviceType,
		staging.directAccess,
		staging.entrances,
		staging.eventDate,
		staging.exits,
		staging.experimentId,
		staging.indirectAccess,
		staging.platformName,
		staging.reads,
		staging.region,
		staging.sessionId,
		staging.timeOnPage,
		staging.title,
		staging.userId,
		staging.variantId,
		staging.views
	)