import {gql} from 'apollo-boost';

export default gql`
	query TouchpointPath(
		$channelId: String
		$title: String
		$touchpoint: String!
		$rangeKey: Int!
		$location: String
		$devices: String
	) {
		page(
			channelId: $channelId
			url: $touchpoint
			rangeKey: $rangeKey
			country: $location
			deviceType: $devices
			title: $title
		) {
			assetTitle
			directAccessMetric {
				value
			}
			indirectAccessMetric {
				value
			}
			pageReferrerMetrics {
				assetTitle
				external
				referrer
				accessMetric {
					value
				}
			}
			viewsMetric {
				value
			}
		}
	}
`;
