import {gql} from 'apollo-boost';

export interface DeleteEventAnalysisData {
	null;
}

export interface DeleteEventAnalysisVariables {
	eventAnalysisIds: Array<string>;
}

export default gql`
	mutation DeleteEventAnalysisMutation($eventAnalysisIds: [String]!) {
		deleteEventAnalyses(eventAnalysisIds: $eventAnalysisIds)
	}
`;
