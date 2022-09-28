import getMetricsMapper from 'cerebro-shared/hocs/mappers/metrics';
import metrics from './metrics';
import {gql} from 'apollo-boost';
import {graphql} from '@apollo/react-hoc';
import {METRIC_FRAGMENT} from 'shared/queries/fragments';
import {withMetricsCard} from 'cerebro-shared/hocs/MetricsCard';

const METRICS_QUERY = gql`
	query DocumentsAndMediaMetrics(
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
		document(
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
			commentsMetric {
				...metricFragment
			}
			downloadsMetric {
				...metricFragment
			}
			previewsMetric {
				...metricFragment
			}
			ratingsMetric {
				...metricFragment
			}
		}
	}

	${METRIC_FRAGMENT}
`;

/**
 * HOC
 * @description Documents And Media Metrics
 */
const withDocumentsAndMediaMetrics = () =>
	graphql(
		METRICS_QUERY,
		getMetricsMapper(result => result.document, metrics)
	);

export default withMetricsCard(withDocumentsAndMediaMetrics);
