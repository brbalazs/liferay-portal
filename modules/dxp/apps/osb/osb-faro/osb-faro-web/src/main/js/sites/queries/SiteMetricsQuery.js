import {gql} from 'apollo-boost';
import {METRIC_FRAGMENT} from 'shared/queries/fragments';

export default gql`
	query SiteMetrics($channelId: String, $interval: String!, $rangeKey: Int!) {
		site(channelId: $channelId, interval: $interval, rangeKey: $rangeKey) {
			anonymousVisitorsMetric {
				...metricFragment
			}
			bounceRateMetric {
				...metricFragment
			}
			engagementMetric {
				...metricFragment
			}
			knownVisitorsMetric {
				...metricFragment
			}
			sessionDurationMetric {
				...metricFragment
			}
			sessionsPerVisitorMetric {
				...metricFragment
			}
			visitorsMetric {
				...metricFragment
			}
		}
	}

	${METRIC_FRAGMENT}
`;
