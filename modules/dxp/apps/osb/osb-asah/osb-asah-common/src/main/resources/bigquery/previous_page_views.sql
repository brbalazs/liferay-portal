WITH AdjacentPage AS (
	SELECT
		canonicalUrl,
		channelId,
		eventDate,
		COALESCE(
			LAG (canonicalUrl) OVER (
				PARTITION BY
					channelId,
					sessionId,
					userId
				ORDER BY
					eventDate
			),
			NULLIF(referrer, '')) AS previousCanonicalUrl,
		LAG (title) OVER (
			PARTITION BY
				channelId,
				sessionId,
				userId
			ORDER BY
				eventDate
		) AS previousTitle,
		referrer,
		title,
		userId
	FROM
		`$[AC_PROJECT_ID].event`
	WHERE
		applicationId = 'Page'
		AND eventId = 'pageViewed'
)
SELECT
	canonicalUrl,
	channelId,
	eventDate,
	previousCanonicalUrl,
	(CASE WHEN previousTitle IS NULL THEN true ELSE false END) AS previousExternal,
	previousTitle,
	title,
	userId
FROM
	AdjacentPage