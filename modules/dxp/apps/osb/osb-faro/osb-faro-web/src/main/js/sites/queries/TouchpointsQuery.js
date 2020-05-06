import {gql} from 'apollo-boost';

export default gql`
	query Touchpoint(
		$channelId: String
		$keywords: String
		$rangeKey: Int!
		$size: Int!
		$sort: Sort!
		$start: Int!
		$terms: String
	) {
		pages(
			channelId: $channelId
			keywords: $keywords
			rangeKey: $rangeKey
			size: $size
			sort: $sort
			start: $start
			terms: $terms
		) {
			assetMetrics {
				... on PageMetric {
					assetTitle
					assetId
					dataSourceId
					avgTimeOnPageMetric {
						value
					}
					bounceRateMetric {
						value
					}
					engagementMetric {
						value
					}
					entrancesMetric {
						value
					}
					exitRateMetric {
						value
					}
					visitorsMetric {
						value
					}
					viewsMetric {
						value
					}
				}
			}
			total
		}
	}
`;
