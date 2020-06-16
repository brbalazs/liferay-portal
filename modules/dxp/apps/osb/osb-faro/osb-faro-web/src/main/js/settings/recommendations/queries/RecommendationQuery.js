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
