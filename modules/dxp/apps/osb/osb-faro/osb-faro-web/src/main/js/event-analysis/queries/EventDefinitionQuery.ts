import {Event} from '../utils/types';
import {gql} from 'apollo-boost';

export interface EventDefinitionData {
	eventDefinition: Event[];
	total: number;
}

export interface EventDefinitionVariables {
	displayName?: string;
	id?: string;
}

export default gql`
	query EventDefinition($displayName: String, $id: String) {
		eventDefinition(displayName: $displayName, id: $id) {
			description
			displayName
			eventAttributeDefinitions {
				dataType
				description
				displayName
				id
				name
				sampleValue
			}
			id
			name
			type
		}
	}
`;
