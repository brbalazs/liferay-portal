import {Attribute} from '../utils/types';
import {gql} from 'apollo-boost';

export interface EventAttributeDefinitionsData {
	eventAttributeDefinition: Attribute[];
	total: number;
}

export interface EventAttributeDefinitionsVariables {
	keyword?: string;
	page?: number;
	size: number;
	sort: {
		column: string;
		type: 'ASC' | 'DESC';
	};
}

export default gql`
	query EventAttributeDefinitions(
		$keyword: String
		$page: Int!
		$size: Int!
		$sort: Sort!
	) {
		eventAttributeDefinitions(
			keyword: $keyword
			page: $page
			size: $size
			sort: $sort
		) {
			eventAttributeDefinitions {
				dataType
				description
				displayName
				id
				name
				sampleValue
			}
			total
		}
	}
`;
