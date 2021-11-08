import {gql} from 'apollo-boost';

export default gql`
	query Touchpoint(
		$channelId: String
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
		$sort: Sort!
	) {
		pages(
			channelId: $channelId
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
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
