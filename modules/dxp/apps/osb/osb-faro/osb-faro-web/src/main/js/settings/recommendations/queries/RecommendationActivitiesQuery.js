import {gql} from 'apollo-boost';

export default gql`
	query RecommendationActivities(
		$applicationId: String!
		$eventContextPropertyFilters: [PropertyFilter]
		$eventId: String!
		$size: Int!
		$start: Int!
	) {
		activities(
			applicationId: $applicationId
			eventContextPropertyFilters: $eventContextPropertyFilters
			eventId: $eventId
			size: $size
			start: $start
		) {
			activities {
				applicationId
				eventContext
				eventId
				eventProperties
				ownerId
				startTime
			}
			total
		}
	}
`;
