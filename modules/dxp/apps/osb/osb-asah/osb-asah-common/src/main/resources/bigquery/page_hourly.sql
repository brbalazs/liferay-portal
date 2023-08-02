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
		`$[AC_PROJECT_ID].event` AS Event
	WHERE
		eventDate > TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 48 HOUR)
),
PageFinalizedEvent AS (
    SELECT
		PageEvent.applicationId,
		PageEvent.browserName,
		PageEvent.canonicalUrl,
		PageEvent.channelId,
		PageEvent.city,
		PageEvent.country,
		PageEvent.deviceType,
		PageEvent.eventDate,
		PageEvent.eventId,
		PageEvent.platformName,
		PageEvent.region,
		PageEvent.sessionId,
		PageEvent.title,
		PageEvent.userId
    FROM
    	PageEvent
	INNER JOIN
		`$[AC_PROJECT_ID].session` AS Session ON
		    PageEvent.sessionId = Session.id
    WHERE
		Session.sessionStart > TIMESTAMP_SUB(CURRENT_TIMESTAMP(), INTERVAL 48 HOUR)
),
PageBounces AS (
	SELECT
		PageFinalizedEvent.channelId,
		COUNT(*) AS count,
		COUNTIF(
			PageFinalizedEvent.applicationId = 'Page' AND
			PageFinalizedEvent.eventId = 'pageViewed'
		) AS pageViews,
		PageFinalizedEvent.sessionId,
		PageFinalizedEvent.userId
	FROM
		PageFinalizedEvent
	WHERE
		PageFinalizedEvent.eventId NOT IN (
			'blogViewed', 'documentPreviewed', 'formViewed', 'pageLoaded',
			'pageUnloaded', 'webContentViewed'
	  	)
	GROUP BY
		channelId, sessionId, userId
),
PageEntrances AS (
	SELECT
		browserName,
		canonicalUrl,
		channelId,
		city,
		country,
		deviceType,
		rank AS entrances,
		TIMESTAMP_TRUNC(eventDate, HOUR) AS normalizedEventDate,
		platformName,
		region,
		sessionId,
		title,
		userId
	FROM (
		SELECT
			PageFinalizedEvent.browserName,
			PageFinalizedEvent.canonicalUrl,
			PageFinalizedEvent.channelId,
			PageFinalizedEvent.city,
			PageFinalizedEvent.country,
			PageFinalizedEvent.deviceType,
			PageFinalizedEvent.eventDate,
			PageFinalizedEvent.platformName,
			ROW_NUMBER() OVER (
				PARTITION BY
					PageFinalizedEvent.channelId,
					PageFinalizedEvent.sessionId,
					PageFinalizedEvent.userId
				ORDER BY
					PageFinalizedEvent.eventDate ASC
			) AS rank,
			PageFinalizedEvent.region,
			PageFinalizedEvent.sessionId,
			PageFinalizedEvent.title,
			PageFinalizedEvent.userId
		FROM
			PageFinalizedEvent
	) AS EventEntrance
	WHERE
		rank = 1
),
PageExits AS (
	SELECT
		browserName,
		canonicalUrl,
		channelId,
		city,
		country,
		deviceType,
		rank AS exits,
		TIMESTAMP_TRUNC(eventDate, HOUR) AS normalizedEventDate,
		platformName,
		region,
		sessionId,
		title,
		userId
	FROM (
		SELECT
			PageFinalizedEvent.browserName,
			PageFinalizedEvent.canonicalUrl,
			PageFinalizedEvent.channelId,
			PageFinalizedEvent.city,
			PageFinalizedEvent.country,
			PageFinalizedEvent.deviceType,
			PageFinalizedEvent.eventDate,
			PageFinalizedEvent.platformName,
			ROW_NUMBER() OVER (
				PARTITION BY
					PageFinalizedEvent.channelId,
					PageFinalizedEvent.sessionId,
					PageFinalizedEvent.userId
				ORDER BY
					PageFinalizedEvent.eventDate DESC
			) AS rank,
			PageFinalizedEvent.region,
			PageFinalizedEvent.sessionId,
			PageFinalizedEvent.title,
			PageFinalizedEvent.userId
		FROM
			PageFinalizedEvent
	) AS EventExit
	WHERE
		rank = 1
),
PageTimeOnPages AS (
	SELECT
		browserName,
		canonicalUrl,
		channelId,
		city,
		country,
		deviceType,
		TIMESTAMP_TRUNC(eventDate, HOUR) AS normalizedEventDate,
		platformName,
		region,
		sessionId,
		SUM(TIMESTAMP_DIFF(nextTime, eventDate, MILLISECOND)) AS timeOnPage,
		title,
		userId
	FROM (
		SELECT
			PageFinalizedEvent.browserName,
			PageFinalizedEvent.canonicalUrl,
			PageFinalizedEvent.channelId,
			PageFinalizedEvent.city,
			PageFinalizedEvent.country,
			PageFinalizedEvent.deviceType,
			PageFinalizedEvent.eventDate,
			LEAD(
			    PageFinalizedEvent.eventDate
			) OVER (
				PARTITION BY
					PageFinalizedEvent.channelId,
					PageFinalizedEvent.sessionId,
					PageFinalizedEvent.userId
				ORDER BY
					PageFinalizedEvent.eventDate
			) AS nextTime,
			PageFinalizedEvent.platformName,
			PageFinalizedEvent.region,
			PageFinalizedEvent.sessionId,
			PageFinalizedEvent.title,
			PageFinalizedEvent.userId
		FROM
			PageFinalizedEvent
	) AS EventTimeOnPage
	GROUP BY
		browserName, canonicalUrl, channelId, city, country, deviceType,
		normalizedEventDate, platformName, region, sessionId, title, userId
),
PageViews AS (
	SELECT
		browserName,
		canonicalUrl,
		channelId,
		city,
		country,
		COUNTIF(eventId = 'ctaClicked') ctaClicks,
		MAX(description) AS description,
		deviceType,
		COUNTIF(eventId = 'pageViewed' AND referrer = '') AS directAccess,
		ANY_VALUE(experimentId) AS experimentId,
		COUNTIF(eventId = 'pageViewed' AND referrer != '') AS indirectAccess,
		TIMESTAMP_TRUNC(eventDate, HOUR) AS normalizedEventDate,
		platformName,
		COUNTIF(eventId = 'pageRead') reads,
		region,
		sessionId,
		title,
		userId,
		ANY_VALUE(variantId) AS variantId,
		COUNTIF(eventId = 'pageViewed') AS views
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
	CASE
		WHEN
			PageBounces.count > 2 OR PageBounces.pageViews > 1
		THEN
			0
		ELSE
			1
	END AS bounce,
	PageViews.browserName,
	PageViews.canonicalUrl,
	PageViews.channelId,
	PageViews.city,
	PageViews.country,
	PageViews.ctaClicks,
	PageViews.description,
	PageViews.deviceType,
	PageViews.directAccess,
	PageEntrances.entrances,
	PageViews.normalizedEventDate AS eventDate,
	PageExits.exits,
	PageViews.experimentId,
	PageViews.indirectAccess,
	PageViews.platformName,
	PageViews.reads,
	PageViews.region,
	PageTimeOnPages.sessionId,
	PageTimeOnPages.timeOnPage,
	PageViews.title,
	PageViews.userId,
	PageViews.variantId,
	PageViews.views
FROM
	PageViews
LEFT JOIN PageBounces ON (
	PageViews.channelId = PageBounces.channelId AND
	COALESCE(PageViews.sessionId, '') = COALESCE(PageBounces.sessionId, '') AND
	PageViews.userId = PageBounces.userId
)
LEFT JOIN PageEntrances ON (
	PageViews.browserName = PageEntrances.browserName AND
	PageViews.canonicalUrl = PageEntrances.canonicalUrl AND
	PageViews.channelId = PageEntrances.channelId AND
	PageViews.city = PageEntrances.city AND
	PageViews.country = PageEntrances.country AND
	PageViews.deviceType = PageEntrances.deviceType AND
	PageViews.normalizedEventDate = PageEntrances.normalizedEventDate AND
	PageViews.platformName = PageEntrances.platformName AND
	PageViews.region = PageEntrances.region AND
	COALESCE(PageViews.sessionId, '') = COALESCE(PageEntrances.sessionId, '') AND
	PageViews.title = PageEntrances.title AND
	PageViews.userId = PageEntrances.userId
)
LEFT JOIN PageExits ON (
	PageViews.browserName = PageExits.browserName AND
	PageViews.canonicalUrl = PageExits.canonicalUrl AND
	PageViews.channelId = PageExits.channelId AND
	PageViews.city = PageExits.city AND
	PageViews.country = PageExits.country AND
	PageViews.deviceType = PageExits.deviceType AND
	PageViews.normalizedEventDate = PageExits.normalizedEventDate AND
	PageViews.platformName = PageExits.platformName AND
	PageViews.region = PageExits.region AND
	COALESCE(PageViews.sessionId, '') = COALESCE(PageExits.sessionId, '') AND
	PageViews.title = PageExits.title AND
	PageViews.userId = PageExits.userId
)
LEFT JOIN PageTimeOnPages ON (
	PageViews.browserName = PageTimeOnPages.browserName AND
	PageViews.canonicalUrl = PageTimeOnPages.canonicalUrl AND
	PageViews.channelId = PageTimeOnPages.channelId AND
	PageViews.city = PageTimeOnPages.city AND
	PageViews.country = PageTimeOnPages.country AND
	PageViews.deviceType = PageTimeOnPages.deviceType AND
	PageViews.normalizedEventDate = PageTimeOnPages.normalizedEventDate AND
	PageViews.platformName = PageTimeOnPages.platformName AND
	PageViews.region = PageTimeOnPages.region AND
	COALESCE(PageViews.sessionId, '') = COALESCE(PageTimeOnPages.sessionId, '') AND
	PageViews.title = PageTimeOnPages.title AND
	PageViews.userId = PageTimeOnPages.userId
)