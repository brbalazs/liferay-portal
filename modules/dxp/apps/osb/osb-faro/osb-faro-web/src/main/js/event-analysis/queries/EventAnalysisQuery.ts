import {BreakdownDataItem, CalculationTypes, Filter} from '../utils/types';
import {gql} from 'apollo-boost';

export interface EventAnalysisData {
	count: number;
	page: number;
	value: number;
	breakdownItems?: BreakdownDataItem[];
}

export interface EventAnalysisVariables {
	analysisType: CalculationTypes;
	channelId: string;
	eventAnalysisFilters: Filter[];
	eventDefinitionId: string;
	rangeEnd: string;
	rangeKey: number | null;
	rangeStart: string;
}

export default gql`
	query EventAnalysis(
		$analysisType: AnalysisType!
		$channelId: String!
		$eventAnalysisFilters: [EventAnalysisFilter]
		$eventDefinitionId: String!
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
	) {
		eventAnalysis(
			analysisType: $analysisType
			channelId: $channelId
			eventAnalysisFilters: $eventAnalysisFilters
			eventDefinitionId: $eventDefinitionId
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
		) {
			count
			page
			value
		}
	}
`;
