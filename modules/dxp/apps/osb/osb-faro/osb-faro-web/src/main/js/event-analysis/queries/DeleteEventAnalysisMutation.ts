import {gql} from 'apollo-boost';

export default gql`
	mutation DeleteEventAnalysisMutation($eventAnalysisIds: [String]!) {
		deleteEventAnalyses(eventAnalysisIds: $eventAnalysisIds)
	}
`;
