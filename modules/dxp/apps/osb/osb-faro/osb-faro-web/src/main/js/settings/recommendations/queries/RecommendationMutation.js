import {gql} from 'apollo-boost';

export const RECOMMENDATION_MUTATION = gql`
	mutation RecommendationMutation(
		$name: String!
		$parameters: [JobParameterInput]
		$trainingFrequency: JobTrainingFrequency
		$trainingPeriod: JobTrainingPeriod
		$type: JobType!
	) {
		createJob(
			name: $name
			parameters: $parameters
			trainingFrequency: $trainingFrequency
			trainingPeriod: $trainingPeriod
			type: $type
		) {
			id
			name
			parameters {
				name
				value
			}
			status
			trainingDate
			trainingFrequency
			trainingPeriod
			type
		}
	}
`;
