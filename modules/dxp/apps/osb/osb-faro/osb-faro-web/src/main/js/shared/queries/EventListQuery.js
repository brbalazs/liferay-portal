import {gql} from 'apollo-boost';

export default gql`
	query EventList(
		$channelId: String
		$eventType: String
		$keywords: String
		$size: Int!
		$start: Int!
	) {
		events(
			channelId: $channelId
			eventType: $eventType
			keywords: $keywords
			size: $size
			start: $start
		) {
			events {
				name
				eventId
				displayName
				description
				eventType
			}
			total
		}
	}
`;
