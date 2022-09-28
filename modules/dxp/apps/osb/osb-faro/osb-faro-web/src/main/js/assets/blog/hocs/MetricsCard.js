import getMetricsMapper from 'cerebro-shared/hocs/mappers/metrics';
import metrics from './metrics';
import {gql} from 'apollo-boost';
import {graphql} from '@apollo/react-hoc';
import {METRIC_FRAGMENT} from 'shared/queries/fragments';
import {withMetricsCard} from 'cerebro-shared/hocs/MetricsCard';

const METRICS_QUERY = gql`
	query BlogsMetrics(
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
		blog(
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
			commentsMetric {
				...metricFragment
			}
			ratingsMetric {
				...metricFragment
			}
			readingTimeMetric {
				...metricFragment
			}
			viewsMetric {
				...metricFragment

				previousValue
				value
			}
		}
	}

	${METRIC_FRAGMENT}
`;
/**
 * HOC
 * @description Blogs Metrics
 */
const withBlogsMetrics = () =>
	graphql(
		METRICS_QUERY,
		getMetricsMapper(result => result.blog, metrics)
	);

export default withMetricsCard(withBlogsMetrics);
