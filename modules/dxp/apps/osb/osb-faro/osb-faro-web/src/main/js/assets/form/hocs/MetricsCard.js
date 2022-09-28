import getMetricsMapper from 'cerebro-shared/hocs/mappers/metrics';
import metrics from './metrics';
import {gql} from 'apollo-boost';
import {graphql} from '@apollo/react-hoc';
import {METRIC_FRAGMENT} from 'shared/queries/fragments';
import {withMetricsCard} from 'cerebro-shared/hocs/MetricsCard';

const METRICS_QUERY = gql`
	query FormsMetrics(
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
		form(
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
			abandonmentsMetric {
				...metricFragment
			}
			assetId
			assetTitle
			completionTimeMetric {
				...metricFragment
			}
			submissionsMetric {
				...metricFragment
			}
			viewsMetric {
				...metricFragment
			}
		}
	}

	${METRIC_FRAGMENT}
`;

/**
 * HOC
 * @description Forms Metrics
 */
const withFormsMetrics = () =>
	graphql(
		METRICS_QUERY,
		getMetricsMapper(result => result.form, metrics)
	);

export default withMetricsCard(withFormsMetrics);
