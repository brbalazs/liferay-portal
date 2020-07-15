import {gql} from 'apollo-boost';

export default gql`
	query Recommendation($jobId: String!) {
		jobById(id: $jobId) {
			id
			name
			nextRunDate
			parameters {
				name
				value
			}
			status
			runDate
			runFrequency
			runDataPeriod
			type
		}
	}
`;

export const RECOMMENDATION_BY_NAME_QUERY = gql`
	query Recommendation($name: String!) {
		jobByName(name: $name) {
			id
			name
		}
	}
`;
