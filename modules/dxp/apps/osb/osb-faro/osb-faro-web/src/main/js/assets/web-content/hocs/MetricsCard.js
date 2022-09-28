import getMetricsMapper from 'cerebro-shared/hocs/mappers/metrics';
import metrics from './metrics';
import {gql} from 'apollo-boost';
import {graphql} from '@apollo/react-hoc';
import {METRIC_FRAGMENT} from 'shared/queries/fragments';
import {withMetricsCard} from 'cerebro-shared/hocs/MetricsCard';

const METRICS_QUERY = gql`
	query WebContentMetrics(
		$assetId: String!
		$channelId: String
		$devices: String
		$location: String
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
		$title: String
		$touchpoint: String
	) {
		journal(
			assetId: $assetId
			canonicalUrl: $touchpoint
			channelId: $channelId
			country: $location
			deviceType: $devices
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
			title: $title
		) {
			assetId
			assetTitle
			urls
			viewsMetric {
				...metricFragment
			}
		}
	}

	${METRIC_FRAGMENT}
`;

/**
 * HOC
 * @description Web Content Metrics
 */
const withWebContentMetrics = () =>
	graphql(
		METRICS_QUERY,
		getMetricsMapper(result => result.journal, metrics)
	);

export default withMetricsCard(withWebContentMetrics);
