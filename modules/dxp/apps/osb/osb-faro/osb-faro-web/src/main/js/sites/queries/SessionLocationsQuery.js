import {GEOLOCATION_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query SiteMetrics($channelId: String, $rangeKey: Int!) {
		site(
			channelId: $channelId
			includePrevious: false
			rangeKey: $rangeKey
		) {
			sessionsMetric {
				...geolocationFragment
			}
		}
	}

	${GEOLOCATION_FRAGMENT}
`;
