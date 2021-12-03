import {gql} from 'apollo-boost';
import {Sort} from 'shared/types';

type EventAnalysis = {
	dateCreated: number;
	dateModified: number;
	id: number;
	title: string;
	userName: string;
};

export interface EventAnalysisListData {
	count: number;
	eventAnalysis: EventAnalysis[];
	page: number;
	value: number;
}

export interface EventAnalysisListVariables {
	channelId: string;
	keywords: string;
	rangeEnd: string;
	rangeKey: number;
	rangeStart: string;
	size: number;
	sort: Sort;
	start: number;
}

/**
 * TODO: LRAC-9835 Create real query to fetch event analysis list
 * and remove @client directive
 */

export default gql`
	query EventAnalysisList(
		$channelId: String
		$keywords: String
		$page: Int!
		$size: Int!
		$sort: Sort!
		$start: Int!
	) {
		eventAnalysisList(
			channelId: $channelId
			keywords: $keywords
			page: $page
			size: $size
			sort: $sort
			start: $start
		) @client {
			eventAnalysis {
				dateCreated
				dateModified
				id
				name: title
				userName
			}
			total
		}
	}
`;
