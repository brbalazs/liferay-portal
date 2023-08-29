ALTER TABLE Experiment ADD COLUMN IF NOT EXISTS publishable BOOLEAN;

UPDATE Experiment SET publishable = FALSE;

UPDATE Experiment SET publishable = TRUE WHERE id IN (
	WITH 
	LatestExperimentDate AS (
		SELECT 
			channelId,
			max(createDate) AS createDate, 
			dataSourceId,
			pageURL
		FROM 
			Experiment 
		GROUP BY
			channelId,
			dataSourceId,
			pageURL
	)
	SELECT 
		id 
	FROM 
		Experiment 
	INNER JOIN
		LatestExperimentDate 
	ON 
		Experiment.channelId = LatestExperimentDate.channelId
	AND 
		Experiment.createDate = LatestExperimentDate.createDate
	AND 
		Experiment.dataSourceId = LatestExperimentDate.dataSourceId
	AND 
		Experiment.pageURL = LatestExperimentDate.pageURL 
);
