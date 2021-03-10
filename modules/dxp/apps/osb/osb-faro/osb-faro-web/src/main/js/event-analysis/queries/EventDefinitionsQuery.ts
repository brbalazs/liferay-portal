import {Event, EventTypes} from '../utils/types';
import {gql} from 'apollo-boost';

export interface EventDefinitionsData {
	eventDefinition: Event[];
	total: number;
}

export interface EventDefinitionsVariables {
	eventType: EventTypes;
	keyword?: string;
	page?: number;
	size: number;
	sort: {
		column: string;
		type: 'ASC' | 'DESC';
	};
}

export default gql`
	query EventDefinitions(
		$eventType: EventDefinitionType!
		$keyword: String
		$page: Int!
		$size: Int!
		$sort: Sort!
	) {
		eventDefinitions(
			eventType: $eventType
			keyword: $keyword
			page: $page
			size: $size
			sort: $sort
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
