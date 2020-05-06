import {gql} from 'apollo-boost';

export default gql`
	query AssetsQuery(
		$channelId: String
		$devices: String
		$location: String
		$rangeKey: Int!
		$touchpoint: String!
	) {
		assets(
			channelId: $channelId
			country: $location
			deviceType: $devices
			rangeKey: $rangeKey
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
