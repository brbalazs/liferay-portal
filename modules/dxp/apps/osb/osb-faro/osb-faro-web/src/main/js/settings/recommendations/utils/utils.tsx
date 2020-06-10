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
		name: Liferay.Language.get('run-manually'),
		value: jobTrainingFrequencies.manual
	},
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
	}
];

export const JOB_TRAINING_PERIODS_LIST = [
	{
		name: Liferay.Language.get('last-7-days'),
		value: jobTrainingPeriods.last7Days
	},
	{
		name: Liferay.Language.get('last-30-days'),
		value: jobTrainingPeriods.last30Days
	},
	{
		name: Liferay.Language.get('last-180-days'),
		value: jobTrainingPeriods.last180Days
	},
	{
		name: Liferay.Language.get('last-year'),
		value: jobTrainingPeriods.last365Days
	}
];

export const RULE_NAME_LABEL_MAP = {
	excludeFilter: Liferay.Language.get('exclude'),
	includeFilter: Liferay.Language.get('include')
};
