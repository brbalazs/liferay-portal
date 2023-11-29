CREATE TABLE `${PROJECT_ID}.${asah_project_id}.new_identityactivitysummary` (
	activitiesCount	INTEGER,
	channelId INTEGER,
	dataSourceId INTEGER,
	eventId STRING,
	firstActivityDate TIMESTAMP,
	identityId STRING,
	individualId STRING,
	lastActivityDate TIMESTAMP
) PARTITION BY DATE(firstActivityDate) AS (
	SELECT * FROM `${PROJECT_ID}.${asah_project_id}.identityactivitysummary`
);

DROP TABLE `${PROJECT_ID}.${asah_project_id}.identityactivitysummary`;

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.new_identityactivitysummary` RENAME TO identityactivitysummary;