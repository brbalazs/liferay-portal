import {gql} from 'apollo-boost';

export default gql`
	query Recommendation($jobId: String!) {
		job(jobId: $jobId) {
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
