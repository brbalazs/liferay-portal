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
	keywords: string;
	page: number;
	size: number;
	sort: Sort;
}

/**
 * TODO: LRAC-9835 Create real query to fetch event analysis list
 * and remove @client directive
 */

export const EventAnalysisListQuery = gql`
	query EventAnalysisList(
		$channelId: String!
		$keywords: String
		$page: Int!
		$size: Int!
		$sort: Sort!
	) {
		eventAnalyses(
			channelId: $channelId
			keywords: $keywords
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

export interface DeleteEventAnalysisData {
	null;
}

export interface DeleteEventAnalysisVariables {
	eventAnalysisIds: Array<string>;
}

export const DeleteEventAnalysisMutation = gql`
	mutation DeleteEventAnalysisMutation($eventAnalysisIds: [String]!) {
		deleteEventAnalyses(eventAnalysisIds: $eventAnalysisIds)
	}
`;
