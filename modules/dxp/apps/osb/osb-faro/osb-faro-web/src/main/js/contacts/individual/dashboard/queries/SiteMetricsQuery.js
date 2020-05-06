import {gql} from 'apollo-boost';
import {TREND_FRAGMENT} from 'shared/queries/fragments';

export default gql`
	query SiteMetrics($channelId: String, $interval: String!, $rangeKey: Int!) {
		site(channelId: $channelId, interval: $interval, rangeKey: $rangeKey) {
			anonymousVisitorsMetric {
				...trendFragment
			}
			knownVisitorsMetric {
				...trendFragment
			}
			visitorsMetric {
				...trendFragment
			}
		}
	}

	${TREND_FRAGMENT}
`;
