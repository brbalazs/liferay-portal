import {BROWSER_FRAGMENT, DEVICE_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query SiteMetrics($channelId: String, $rangeKey: Int!) {
		site(
			channelId: $channelId
			includePrevious: false
			rangeKey: $rangeKey
		) {
			sessionsMetric {
				...browserFragment
				...deviceFragment

				value
			}
		}
	}

	${BROWSER_FRAGMENT}
	${DEVICE_FRAGMENT}
`;
