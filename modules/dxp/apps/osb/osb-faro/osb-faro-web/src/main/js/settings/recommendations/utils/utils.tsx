import {
	jobRunDataPeriods,
	jobRunFrequencies,
	jobRunStatuses,
	jobStatuses,
	jobTypes,
	LAST_180_DAYS,
	LAST_30_DAYS,
	LAST_7_DAYS,
	LAST_YEAR
} from 'shared/util/constants';

export const CANONICAL_URL = 'canonicalUrl';
export const DESCRIPTION = 'description';
export const KEYWORDS = 'keywords';
export const TITLE = 'title';
export const URL = 'url';

export const METADATA_TAGS = [CANONICAL_URL, DESCRIPTION, KEYWORDS, TITLE, URL];

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
	runDataPeriod: jobRunDataPeriods;
	runDate: string;
	runFrequency: jobRunFrequencies;
	status: jobStatuses;
	type: jobTypes;
};

export const JOB_RUN_STATUSES_DISPLAY_MAP = {
	[jobRunStatuses.completed]: 'success',
	[jobRunStatuses.failed]: 'danger',
	[jobRunStatuses.published]: 'primary',
	[jobRunStatuses.running]: 'warning'
};

export const JOB_RUN_STATUSES_LABEL_MAP = {
	[jobRunStatuses.completed]: Liferay.Language.get('completed'),
	[jobRunStatuses.failed]: Liferay.Language.get('failed'),
	[jobRunStatuses.published]: Liferay.Language.get('live-version'),
	[jobRunStatuses.running]: Liferay.Language.get('training')
};

export const JOB_STATUSES_DISPLAY_MAP = {
	[jobStatuses.failed]: 'danger',
	[jobStatuses.pending]: 'secondary',
	[jobStatuses.ready]: 'success',
	[jobStatuses.running]: 'warning',
	[jobStatuses.scheduled]: 'info'
};

export const JOB_STATUSES_LABEL_MAP = {
	[jobStatuses.failed]: Liferay.Language.get('failed'),
	[jobStatuses.pending]: Liferay.Language.get('pending'),
	[jobStatuses.ready]: Liferay.Language.get('ready'),
	[jobStatuses.running]: Liferay.Language.get('training'),
	[jobStatuses.scheduled]: Liferay.Language.get('scheduled')
};

export const JOB_RUN_FREQUENCIES_LABEL_MAP = {
	[jobRunFrequencies.every7Days]: Liferay.Language.get('every-7-days'),
	[jobRunFrequencies.every14Days]: Liferay.Language.get('every-14-days'),
	[jobRunFrequencies.every30Days]: Liferay.Language.get('every-30-days'),
	[jobRunFrequencies.manual]: Liferay.Language.get('run-manually')
};

export const JOB_RUN_FREQUENCIES_LIST = [
	{
		name: Liferay.Language.get('run-manually'),
		value: jobRunFrequencies.manual
	},
	{
		name: Liferay.Language.get('every-7-days'),
		value: jobRunFrequencies.every7Days
	},
	{
		name: Liferay.Language.get('every-14-days'),
		value: jobRunFrequencies.every14Days
	},
	{
		name: Liferay.Language.get('every-30-days'),
		value: jobRunFrequencies.every30Days
	}
];

export const JOB_RUN_DATA_PERIODS_LABEL_MAP = {
	[jobRunDataPeriods.last7Days]: Liferay.Language.get('last-7-days'),
	[jobRunDataPeriods.last30Days]: Liferay.Language.get('last-30-days'),
	[jobRunDataPeriods.last180Days]: Liferay.Language.get('last-180-days'),
	[jobRunDataPeriods.last365Days]: Liferay.Language.get('last-year')
};

export const JOB_RUN_DATA_PERIODS_RANGE_KEY_MAP = {
	[jobRunDataPeriods.last7Days]: LAST_7_DAYS,
	[jobRunDataPeriods.last30Days]: LAST_30_DAYS,
	[jobRunDataPeriods.last180Days]: LAST_180_DAYS,
	[jobRunDataPeriods.last365Days]: LAST_YEAR
};

export const JOB_RUN_DATA_PERIODS_LIST = [
	{
		name: Liferay.Language.get('last-7-days'),
		value: jobRunDataPeriods.last7Days
	},
	{
		name: Liferay.Language.get('last-30-days'),
		value: jobRunDataPeriods.last30Days
	},
	{
		name: Liferay.Language.get('last-180-days'),
		value: jobRunDataPeriods.last180Days
	},
	{
		name: Liferay.Language.get('last-year'),
		value: jobRunDataPeriods.last365Days
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
