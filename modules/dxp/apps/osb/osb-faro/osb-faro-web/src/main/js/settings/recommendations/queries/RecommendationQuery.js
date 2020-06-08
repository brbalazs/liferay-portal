import {gql} from 'apollo-boost';

// TODO: add parameters once avail
export default gql`
	query Recommendation($jobId: String!) {
		job(jobId: $jobId) {
			id
			name
			status
			trainingDate
			trainingFrequency
			trainingPeriod
			type
		}
	}
`;
