import {gql} from 'apollo-boost';

export default gql`
	query AssetsTouchpointQuery(
		$assetType: AssetType!
		$assetId: String!
		$channelId: String
		$rangeKey: Int
		$location: String
		$devices: String
	) {
		assetPages(
			assetType: $assetType
			assetId: $assetId
			channelId: $channelId
			rangeKey: $rangeKey
			country: $location
			deviceType: $devices
		) {
			assetTitle
			assetId
		}
	}
`;
