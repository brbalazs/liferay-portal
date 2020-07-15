import {gql} from 'apollo-boost';

export const RECOMMENDATION_MUTATION = gql`
	mutation RecommendationMutation(
		$name: String!
		$parameters: [JobParameterInput]
		$runNow: Boolean
		$runFrequency: JobRunFrequency
		$runDataPeriod: JobRunDataPeriod
		$type: JobType!
	) {
		createJob(
			name: $name
			parameters: $parameters
			runNow: $runNow
			runFrequency: $runFrequency
			runDataPeriod: $runDataPeriod
			type: $type
		) {
			id
			name
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

export const RECOMMENDATION_DELETE_MUTATION = gql`
	mutation RecommendationDeleteMutation($jobIds: [String]!) {
		deleteJobs(jobIds: $jobIds)
	}
`;

export const RECOMMENDATION_RUN_MUTATION = gql`
	mutation RecommendationRunMutation(
		$jobId: String!
		$runDataPeriod: JobRunDataPeriod!
	) {
		runJob(jobId: $jobId, runDataPeriod: $runDataPeriod) {
			id
		}
	}
`;

export const RECOMMENDATION_UPDATE_MUTATION = gql`
	mutation RecommendationUpdateMutation(
		$jobId: String!
		$name: String!
		$parameters: [JobParameterInput]
		$runNow: Boolean
		$runFrequency: JobRunFrequency
		$runDataPeriod: JobRunDataPeriod
	) {
		updateJob(
			jobId: $jobId
			name: $name
			parameters: $parameters
			runNow: $runNow
			runFrequency: $runFrequency
			runDataPeriod: $runDataPeriod
		) {
			id
			name
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
