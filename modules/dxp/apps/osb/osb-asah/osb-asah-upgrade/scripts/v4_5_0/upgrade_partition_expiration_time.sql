ALTER TABLE `${PROJECT_ID}.${asah_project_id}.blogdaily`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.customassetdaily`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.documentlibrarydaily`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.event`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.eventproperty`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.identityactivitysummary`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.identityinterestscore`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.formdaily`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.journaldaily`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.pagedaily`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.session`
	SET OPTIONS (partition_expiration_days = 390);

ALTER TABLE `${PROJECT_ID}.${asah_project_id}.sessioninterestscore`
	SET OPTIONS (partition_expiration_days = 390);