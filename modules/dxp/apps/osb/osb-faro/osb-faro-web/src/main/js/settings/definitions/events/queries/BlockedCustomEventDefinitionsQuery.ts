import {BlockedCustomEvent} from 'event-analysis/utils/types';
import {gql} from 'apollo-boost';

export interface BlockedCustomEventDefinitionsData {
	blockedCustomEventDefinitions: BlockedCustomEvent[];
	total: number;
}

export interface BlockedCustomEventDefinitionsVariables {
	keyword?: string;
	page?: number;
	size: number;
	sort: {
		column: string;
		type: 'ASC' | 'DESC';
	};
}

export default gql`
	query BlockedCustomEventDefinitions(
		$keyword: String
		$page: Int!
		$size: Int!
		$sort: Sort!
	) {
		blockedCustomEventDefinitions(
			keyword: $keyword
			page: $page
			size: $size
			sort: $sort
		) {
			blockedCustomEventDefinitions {
				hidden
				id
				lastSeenDate
				lastSeenURL
				name
			}
			total
		}
	}
`;
