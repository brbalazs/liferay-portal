import gql from 'graphql-tag';
import {Breakdown, CalculationTypes, Filter} from 'event-analysis/utils/types';

export interface EventAnalysisMutationData {
	id: string;
}

export interface EventAnalysisMutationVariables {
	analysisType: CalculationTypes;
	channelId: string;
	compareToPrevious: boolean;
	eventAnalysisBreakdowns?: Breakdown[];
	eventAnalysisFilters?: Filter[];
	eventDefinitionId: string;
	name: string;
	rangeEnd?: string | null;
	rangeKey?: number | null;
	rangeStart?: string | null;
	userId: string;
	userName: string;
	eventAnalysisId?: string | null;
}

export const CreateEventAnalysisMutation = gql`
	mutation CreateEventAnalysis(
		$analysisType: AnalysisType!
		$channelId: String!
		$compareToPrevious: Boolean!
		$eventAnalysisBreakdowns: [EventAnalysisBreakdownInput]
		$eventAnalysisFilters: [EventAnalysisFilterInput]
		$eventDefinitionId: String!
		$name: String!
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
		$userId: String!
		$userName: String!
	) {
		createEventAnalysis(
			analysisType: $analysisType
			channelId: $channelId
			compareToPrevious: $compareToPrevious
			eventAnalysisBreakdowns: $eventAnalysisBreakdowns
			eventAnalysisFilters: $eventAnalysisFilters
			eventDefinitionId: $eventDefinitionId
			name: $name
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
			userId: $userId
			userName: $userName
		) {
			id
		}
	}
`;

export const UpdateEventAnalysisMutation = gql`
	mutation UpdateEventAnalysis(
		$analysisType: AnalysisType!
		$channelId: String!
		$compareToPrevious: Boolean!
		$eventAnalysisBreakdowns: [EventAnalysisBreakdownInput]
		$eventAnalysisFilters: [EventAnalysisFilterInput]
		$eventAnalysisId: String!
		$eventDefinitionId: String!
		$name: String!
		$rangeEnd: String
		$rangeKey: Int
		$rangeStart: String
		$userId: String!
		$userName: String!
	) {
		updateEventAnalysis(
			analysisType: $analysisType
			channelId: $channelId
			compareToPrevious: $compareToPrevious
			eventAnalysisBreakdowns: $eventAnalysisBreakdowns
			eventAnalysisFilters: $eventAnalysisFilters
			eventAnalysisId: $eventAnalysisId
			eventDefinitionId: $eventDefinitionId
			name: $name
			rangeEnd: $rangeEnd
			rangeKey: $rangeKey
			rangeStart: $rangeStart
			userId: $userId
			userName: $userName
		) {
			id
		}
	}
`;
