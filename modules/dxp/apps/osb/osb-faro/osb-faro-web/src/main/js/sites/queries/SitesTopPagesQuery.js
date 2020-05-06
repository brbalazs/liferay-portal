import {gql} from 'apollo-boost';

export default gql`
	query Touchpoint($channelId: String, $rangeKey: Int!, $sort: Sort!) {
		pages(
			channelId: $channelId
			rangeKey: $rangeKey
			size: 5
			sort: $sort
			start: 0
		) {
			assetMetrics {
				... on PageMetric {
					assetTitle
					assetId
					entrancesMetric {
						value
					}
					exitRateMetric {
						value
					}
					visitorsMetric {
						value
					}
				}
			}
			total
		}
	}
`;
