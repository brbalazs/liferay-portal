import {gql} from 'apollo-boost';

export default gql`
	query WebContentList(
		$channelId: String
		$keywords: String
		$rangeKey: Int
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		journals(
			channelId: $channelId
			keywords: $keywords
			rangeKey: $rangeKey
			size: $size
			sort: $sort
			start: $start
		) {
			assetMetrics {
				... on JournalMetric {
					assetId
					assetTitle
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
