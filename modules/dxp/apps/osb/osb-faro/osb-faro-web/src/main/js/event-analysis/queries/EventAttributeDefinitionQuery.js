import {gql} from 'apollo-boost';

export default gql`
	query EventAttributeDefinition($displayName: String, $id: String) {
		eventAttributeDefinition(displayName: $displayName, id: $id) {
			dataType
			description
			displayName
			id
			name
			sampleValue
		}
	}
`;
