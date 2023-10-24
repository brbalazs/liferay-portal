MERGE INTO
	`${PROJECT_ID}.${asah_project_id}.pagedaily` AS replica
USING
	(
		WITH PageEvent AS (
			SELECT
				applicationId,
				COALESCE(browserName, '') AS browserName,
				canonicalUrl,
				channelId,
				COALESCE(city, '') AS city,
				COALESCE(country, '') AS country,
				COALESCE(description, '') AS description,
				COALESCE(deviceType, '') AS deviceType,
				eventDate,
				eventId,
				experimentId,
				COALESCE(platformName, '') AS platformName,
				referrer,
				COALESCE(region, '') AS region,
				sessionId,
				title,
				url,
				userId,
				variantId
			FROM
				`${PROJECT_ID}.${asah_project_id}.event` AS Event
			WHERE
				DATE(Event.eventDate, '${asah_project_time_zone}') >= DATE('2023-09-05') AND
				DATE(Event.eventDate, '${asah_project_time_zone}') < CURRENT_DATE('${asah_project_time_zone}')
		),
		PageViews AS (
			SELECT
				browserName,
				canonicalUrl,
				channelId,
				city,
				country,
				MAX(description) description,
				deviceType,
				TIMESTAMP_TRUNC(eventDate, DAY, '${asah_project_time_zone}') AS normalizedEventDate,
				platformName,
				region,
				sessionId,
				title,
				userId
			FROM
				PageEvent
			WHERE
				applicationId = 'Page' AND
				eventId IN ('ctaClicked', 'pageRead', 'pageViewed')
			GROUP BY
				browserName, canonicalUrl, channelId, city, country, deviceType,
				normalizedEventDate, platformName, region, sessionId, title, userId
		)
		SELECT
			browserName,
			canonicalUrl,
			channelId,
			city,
			country,
			description,
			deviceType,
			normalizedEventDate AS eventDate,
			platformName,
			region,
			sessionId,
			title,
			userId,
		FROM
			PageViews
		WHERE
			sessionId IS NOT NULL
		GROUP BY
			browserName, canonicalUrl, channelId, city, country, description, deviceType,
			eventDate, platformName, region, sessionId, title, userId
	) AS staging
ON
	COALESCE(staging.browserName, '') = COALESCE(replica.browserName, '') AND
	staging.canonicalUrl = replica.canonicalUrl AND
	staging.channelId = replica.channelId AND
	COALESCE(staging.city, '') = COALESCE(replica.city, '') AND
	COALESCE(staging.country, '') = COALESCE(replica.country, '') AND
	COALESCE(staging.deviceType, '') = COALESCE(replica.deviceType, '') AND
	staging.eventDate = replica.eventDate AND
	COALESCE(staging.platformName, '') = COALESCE(replica.platformName, '') AND
	COALESCE(staging.region, '') = COALESCE(replica.region, '') AND
	staging.sessionId = replica.sessionId AND
	staging.title = replica.title AND
	staging.userId = replica.userId
WHEN MATCHED THEN
	UPDATE SET description = staging.description