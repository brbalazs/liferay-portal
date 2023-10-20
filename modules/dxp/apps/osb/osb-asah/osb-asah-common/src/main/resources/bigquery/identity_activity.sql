WITH TimeZone AS (
	SELECT
		(
			CASE
				WHEN
					EXISTS (SELECT * FROM EXTERNAL_QUERY("$[AC_EXTERNAL_CONNECTION_NAME]", "SELECT value FROM $[AC_PROJECT_ID].preference WHERE id = 'time-zone-id';"))
				THEN
					(SELECT * FROM EXTERNAL_QUERY("$[AC_EXTERNAL_CONNECTION_NAME]", "SELECT value FROM $[AC_PROJECT_ID].preference WHERE id = 'time-zone-id';"))
				ELSE
					'UTC'
			END
		) AS value
),
IdentityActivity AS (
	SELECT
		*
	FROM
		`$[AC_PROJECT_ID].identityactivitysummary`
	UNION ALL
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
		`$[AC_PROJECT_ID].event` AS Event
	LEFT JOIN `$[AC_PROJECT_ID].identity` AS Identity ON (
		Event.userId = Identity.id
	)
	WHERE
		DATE(Event.eventDate, (SELECT value FROM TimeZone)) >= CURRENT_DATE((SELECT value FROM TimeZone))
	GROUP BY
		Event.channelId,
		Event.dataSourceId,
		Event.eventId,
		Event.userId
)

SELECT
	SUM(activitiesCount) AS activitiesCount,
	channelId,
	dataSourceId,
	eventId,
	MIN(firstActivityDate) AS firstActivityDate,
	identityId,
	MAX(individualId) AS individualId,
	MAX(lastActivityDate) AS lastActivityDate
FROM
	IdentityActivity
GROUP BY
	channelId, dataSourceId, eventId, identityId