import {gql} from 'apollo-boost';

export default gql`
	query AssetsQuery(
		$channelId: String
		$devices: String
		$location: String
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
		$touchpoint: String!
	) {
		assets(
			channelId: $channelId
			country: $location
			deviceType: $devices
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
			url: $touchpoint
		) {
			assetId
			assetTitle
			assetType
			defaultMetric {
				value
			}
		}
	}
`;
