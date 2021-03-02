import {gql} from 'apollo-boost';

export default gql`
	query EventDefinitions(
		$eventType: String!
		$keyword: String
		$page: Int!
		$size: Int!
	) {
		eventDefinitions(
			eventType: $eventType
			keyword: $keyword
			page: $page
			size: $size
		) {
			eventDefinitions {
				description
				displayName
				id
				name
				type
			}
			total
		}
	}
`;
