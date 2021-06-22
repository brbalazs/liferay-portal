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
		$hidden: Boolean
		$keyword: String
		$page: Int!
		$size: Int!
		$sort: Sort!
	) {
		eventDefinitions(
			eventType: $eventType
			hidden: $hidden
			keyword: $keyword
			page: $page
			size: $size
			sort: $sort
		) {
			eventDefinitions {
				description
				displayName
				hidden
				id
				name
				type
			}
			total
		}
	}
`;
