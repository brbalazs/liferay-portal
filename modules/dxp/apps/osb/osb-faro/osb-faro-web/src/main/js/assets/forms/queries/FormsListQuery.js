import {gql} from 'apollo-boost';

export default gql`
	query FormsList(
		$channelId: String
		$keywords: String
		$rangeKey: Int
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		forms(
			channelId: $channelId
			keywords: $keywords
			rangeKey: $rangeKey
			size: $size
			sort: $sort
			start: $start
		) {
			assetMetrics {
				... on FormMetric {
					abandonmentsMetric {
						value
					}
					assetId
					assetTitle
					completionTimeMetric {
						value
					}
					submissionsMetric {
						value
					}
					urls
					viewsMetric {
						value
					}
				}
			}
			total
		}
	}
`;
