import {
	jobStatuses,
	jobTrainingFrequencies,
	jobTrainingPeriods,
	jobTypes
} from 'shared/util/constants';

export type JobParameter = {
	name: string;
	value: string;
};

export type Job = {
	id: string;
	name: string;
	parameters: JobParameter[];
	status: jobStatuses;
	trainingDate: string;
	trainingFrequency: jobTrainingFrequencies;
	trainingPeriod: jobTrainingPeriods;
	type: jobTypes;
};
export const JOB_TRAINING_FREQUENCIES_LIST = [
	{
		name: Liferay.Language.get('every-7-days'),
		value: jobTrainingFrequencies.every7Days
	},
	{
		name: Liferay.Language.get('every-14-days'),
		value: jobTrainingFrequencies.every14Days
	},
	{
		name: Liferay.Language.get('every-30-days'),
		value: jobTrainingFrequencies.every30Days
	},
	{
		name: Liferay.Language.get('run-manually'),
		value: jobTrainingFrequencies.manual
	}
];
