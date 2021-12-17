import {gql} from 'apollo-boost';
import {Sort} from 'shared/types';

type EventAnalysis = {
	modifiedDate: number;
	id: number;
	name: string;
	userName: string;
};

export interface EventAnalysisListData {
	total: number;
	eventAnalyses: EventAnalysis[];
}

export interface EventAnalysisListVariables {
	channelId: string;
	keyword: string;
	page: number;
	size: number;
	sort: Sort;
}

/**
 * TODO: LRAC-9835 Create real query to fetch event analysis list
 * and remove @client directive
 */

export default gql`
	query EventAnalysisList(
		$channelId: String!
		$keyword: String
		$page: Int!
		$size: Int!
		$sort: Sort!
	) {
		eventAnalyses(
			channelId: $channelId
			keyword: $keyword
			page: $page
			size: $size
			sort: $sort
		) {
			eventAnalyses {
				... on EventAnalysis {
					userName: createdByUserName
					id
					modifiedDate
					name
				}
			}
			total
		}
	}
`;
