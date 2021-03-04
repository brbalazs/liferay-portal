import {gql} from 'apollo-boost';

export default gql`
	query EventAttributeDefinition($displayName: String, $id: String) {
		eventAttributeDefinition(displayName: $displayName, id: $id) {
			dataType
			description
			displayName
			id
			name
		}
	}
`;

export const EVENT_ATTRIBUTE_DEFINITION_WITH_RECENT_VALUES_QUERY = gql`
	query EventAttributeDefinition($displayName: String, $id: String) {
		eventAttributeDefinition(displayName: $displayName, id: $id) {
			dataType
			description
			displayName
			id
			name
			recentValues {
				lastSeenDate
				value
			}
		}
	}
`;
