import {
	CreateEventAnalysisMutation,
	EventAnalysisMutationData,
	EventAnalysisMutationVariables,
	UpdateEventAnalysisMutation
} from 'event-analysis/queries/EventAnalysisMutation';
import {useMutation} from '@apollo/react-hooks';

const useSaveEventAnalysis = (eventAnalysisId: string | null) => {
	const Mutation = eventAnalysisId
		? UpdateEventAnalysisMutation
		: CreateEventAnalysisMutation;
	const [saveEventAnalysis] = useMutation<
		EventAnalysisMutationData,
		EventAnalysisMutationVariables
	>(Mutation);

	return (variables: EventAnalysisMutationVariables) =>
		saveEventAnalysis({variables: {...variables, eventAnalysisId}});
};

export default useSaveEventAnalysis;
