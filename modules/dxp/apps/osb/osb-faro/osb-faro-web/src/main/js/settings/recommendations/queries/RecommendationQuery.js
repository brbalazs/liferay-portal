import {gql} from 'apollo-boost';

export default gql`
	query Recommendation($jobId: String!) {
		jobById(id: $jobId) {
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

export const RECOMMENDATION_BY_NAME_QUERY = gql`
	query Recommendation($name: String!) {
		jobByName(name: $name) {
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
