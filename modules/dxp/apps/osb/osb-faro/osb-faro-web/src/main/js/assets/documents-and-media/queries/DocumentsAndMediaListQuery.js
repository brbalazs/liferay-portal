import {gql} from 'apollo-boost';

export default gql`
	query DocumentsAndMediaList(
		$channelId: String
		$keywords: String
		$rangeKey: Int
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		documents(
			channelId: $channelId
			keywords: $keywords
			rangeKey: $rangeKey
			size: $size
			sort: $sort
			start: $start
		) {
			assetMetrics {
				... on DocumentMetric {
					assetId
					assetTitle
					commentsMetric {
						value
					}
					downloadsMetric {
						value
					}
					previewsMetric {
						value
					}
					ratingsMetric {
						value
					}
					urls
				}
			}
			total
		}
	}
`;
