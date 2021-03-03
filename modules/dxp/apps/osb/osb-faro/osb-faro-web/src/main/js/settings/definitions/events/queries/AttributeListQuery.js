import {gql} from 'apollo-boost';

export default gql`
	query AttributeList(
		$channelId: String
		$eventId: String
		$keywords: String
		$size: Int!
		$start: Int!
	) {
		attributes(
			channelId: $channelId
			eventId: $eventId
			keywords: $keywords
			size: $size
			start: $start
		) {
			attributes {
				name
				attributeId
				dataType
				displayName
				sampleValue
				description
			}
			total
		}
	}
`;
