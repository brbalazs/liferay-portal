INSERT INTO Channel (id, name) VALUES (1, 'Test Channel 1');

INSERT INTO CustomAssetDashboard(id, assetId, assetTitle, category, channelId) VALUES ('1', '1', '1', 'default', 1);

INSERT INTO Experiment(id, channelId, name) VALUES (1, 1, 'Experiment 1');

INSERT INTO ExperimentMetric(id, experimentId) VALUES (1, 1);

INSERT INTO ExperimentVariant(id, experimentId) VALUES (1, 1);

INSERT INTO ExperimentVariantMetric(id, experimentMetricId) VALUES (1, 1);

INSERT INTO Segment (id, channelId, name) VALUES (1, 1, 'Segment 1');