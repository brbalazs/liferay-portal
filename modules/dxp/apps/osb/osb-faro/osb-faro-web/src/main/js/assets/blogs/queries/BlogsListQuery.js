import {gql} from 'apollo-boost';

export default gql`
	query BlogsList(
		$channelId: String
		$keywords: String
		$rangeKey: Int
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		blogs(
			channelId: $channelId
			keywords: $keywords
			rangeKey: $rangeKey
			size: $size
			sort: $sort
			start: $start
		) {
			assetMetrics {
				... on BlogMetric {
					assetId
					assetTitle
					commentsMetric {
						value
					}
					ratingsMetric {
						value
					}
					readingTimeMetric {
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
