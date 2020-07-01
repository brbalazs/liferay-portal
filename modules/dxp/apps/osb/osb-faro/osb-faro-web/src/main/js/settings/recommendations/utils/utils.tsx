import {
	jobRunStatuses,
	jobStatuses,
	jobTrainingFrequencies,
	jobTrainingPeriods,
	jobTypes
} from 'shared/util/constants';

export const CANONICAL_URL = 'canonicalUrl';
export const DESCRIPTION = 'description';
export const TITLE = 'title';
export const URL = 'url';

export const METADATA_TAGS = [CANONICAL_URL, DESCRIPTION, TITLE, URL];

export const EXCLUDE = 'excludeFilter';
export const INCLUDE = 'includeFilter';

export type Filter = {
	id: string;
	name: string;
	value: string;
};

export type JobParameter = {
	name: string;
	value: string;
};

export type JobProperty = {
	filter: string;
	negate: boolean;
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

export const JOB_RUN_STATUSES_DISPLAY_MAP = {
	[jobRunStatuses.completed]: 'success',
	[jobRunStatuses.failed]: 'danger',
	[jobRunStatuses.running]: 'warning'
};

export const JOB_RUN_STATUSES_LABEL_MAP = {
	[jobRunStatuses.completed]: Liferay.Language.get('completed'),
	[jobRunStatuses.failed]: Liferay.Language.get('failed'),
	[jobRunStatuses.running]: Liferay.Language.get('training')
};

export const JOB_STATUSES_DISPLAY_MAP = {
	[jobStatuses.failed]: 'danger',
	[jobStatuses.pending]: 'secondary',
	[jobStatuses.ready]: 'success',
	[jobStatuses.scheduled]: 'info',
	[jobStatuses.training]: 'warning'
};

export const JOB_STATUSES_LABEL_MAP = {
	[jobStatuses.failed]: Liferay.Language.get('failed'),
	[jobStatuses.pending]: Liferay.Language.get('pending'),
	[jobStatuses.ready]: Liferay.Language.get('ready'),
	[jobStatuses.scheduled]: Liferay.Language.get('scheduled'),
	[jobStatuses.training]: Liferay.Language.get('training')
};

export const JOB_TRAINING_FREQUENCIES_LABEL_MAP = {
	[jobTrainingFrequencies.every7Days]: Liferay.Language.get('every-7-days'),
	[jobTrainingFrequencies.every14Days]: Liferay.Language.get('every-14-days'),
	[jobTrainingFrequencies.every30Days]: Liferay.Language.get('every-30-days'),
	[jobTrainingFrequencies.manual]: Liferay.Language.get('run-manually')
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

export const JOB_TRAINING_PERIODS_LABEL_MAP = {
	[jobTrainingPeriods.last7Days]: Liferay.Language.get('last-7-days'),
	[jobTrainingPeriods.last30Days]: Liferay.Language.get('last-30-days'),
	[jobTrainingPeriods.last180Days]: Liferay.Language.get('last-180-days'),
	[jobTrainingPeriods.last365Days]: Liferay.Language.get('last-year')
};

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

export const JOB_TYPES_LABEL_MAP = {
	[jobTypes.itemSimilarity]: Liferay.Language.get('item-similarity')
};

export const RULE_NAME_LABEL_MAP = {
	excludeFilter: Liferay.Language.get('exclude'),
	includeFilter: Liferay.Language.get('include')
};

export const getPropertiesFromItems = (itemFilters: Filter[]): JobProperty[] =>
	itemFilters.map(({name, value}) => ({
		filter: value,
		negate: name === EXCLUDE
	}));

export const getFilterValueBreakdown = (
	filter: string
): {exactMatchSign: string; metadataTag: string; rule: string} => {
	const [rule, exactMatchSign, metadataTag] = filter
		.split(/\s*([=~])\s*/, 3)
		.reverse();

	return {exactMatchSign, metadataTag, rule};
};
