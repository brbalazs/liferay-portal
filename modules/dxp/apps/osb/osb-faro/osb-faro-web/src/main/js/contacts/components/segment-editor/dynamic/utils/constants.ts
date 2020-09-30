/**
 * Constants for static property groups.
 */

export {default as INDIVIDUAL_PROPERTIES} from './individual-properties';
export {default as ORGANIZATION_PROPERTIES} from './organization-properties';
export {default as WEB_BEHAVIORS} from './web-behaviors';
export {default as SESSION_PROPERTIES} from './session-properties';

export const EVER = 'ever';
export const SINCE = 'since';

export const isKnown = 'is-known';
export const isUnknown = 'is-unknown';

/**
 * Constants for date formatting
 */

export const INPUT_DATE_FORMAT = 'YYYY-MM-DD';
export const INPUT_DATE_TIME_FORMAT = 'YYYY-MM-DDTHH:mmZ';
export const INPUT_DISPLAY_DATE_TIME_FORMAT = `YYYY-MM-DD HH:mm`;

/**
 * Constants for OData query.
 */

export const CONJUNCTIONS = {
	AND: 'and',
	OR: 'or'
};

export const CUSTOM_FUNCTION_OPERATORS = {
	ACCOUNTS_FILTER: 'accounts-filter',
	ACCOUNTS_FILTER_BY_COUNT: 'accounts-filter-by-count',
	ACTIVITIES_FILTER: 'activities-filter',
	ACTIVITIES_FILTER_BY_COUNT: 'activities-filter-by-count',
	INTERESTS_FILTER: 'interests-filter',
	ORGANIZATIONS_FILTER: 'organizations-filter',
	SESSIONS_FILTER: 'sessions-filter'
};

export const DISPLAY_ONLY_OPERATORS = {
	IS_KNOWN: 'ne',
	IS_UNKNOWN: 'eq'
};

export const FUNCTIONAL_OPERATORS = {
	BETWEEN: 'between',
	CONTAINS: 'contains'
};

export const NOT_OPERATORS = {
	NOT_ACCOUNTS_FILTER: 'not-accounts-filter',
	NOT_ACCOUNTS_FILTER_BY_COUNT: 'not-accounts-filter-by-count',
	NOT_ACTIVITIES_FILTER: 'not-activities-filter',
	NOT_ACTIVITIES_FILTER_BY_COUNT: 'not-activities-filter-by-count',
	NOT_CONTAINS: 'not-contains',
	NOT_ORGANIZATIONS_FILTER: 'not-organizations-filter',
	NOT_SESSIONS_FILTER: 'not-sessions-filter'
};

export const GROUP = 'GROUP';

export const RELATIONAL_OPERATORS = {
	EQ: 'eq',
	GE: 'ge',
	GT: 'gt',
	LE: 'le',
	LT: 'lt',
	NE: 'ne'
};

/**
 * Constants to match property types in the passed in supportedProperties array.
 */

export const PROPERTY_TYPES = {
	ACCOUNT_NUMBER: 'account-number',
	ACCOUNT_TEXT: 'account-text',
	BEHAVIOR: 'behavior',
	BOOLEAN: 'boolean',
	DATE: 'date',
	DATE_TIME: 'date-time',
	DURATION: 'duration',
	INTEREST: 'interest',
	NUMBER: 'number',
	ORGANIZATION_BOOLEAN: 'organization-boolean',
	ORGANIZATION_DATE: 'organization-date',
	ORGANIZATION_DATE_TIME: 'organization-date-time',
	ORGANIZATION_NUMBER: 'organization-number',
	ORGANIZATION_SELECT_TEXT: 'organization-select-text',
	ORGANIZATION_TEXT: 'organization-text',
	SELECT_TEXT: 'select-text',
	SESSION_DATE_TIME: 'session-date-time',
	SESSION_GEOLOCATION: 'session-geolocation',
	SESSION_NUMBER: 'session-number',
	SESSION_TEXT: 'session-text',
	TEXT: 'text'
};

/**
 * Constants for CriteriaBuilder component.
 */

const {
	ACCOUNTS_FILTER,
	ACCOUNTS_FILTER_BY_COUNT,
	ACTIVITIES_FILTER,
	ACTIVITIES_FILTER_BY_COUNT,
	INTERESTS_FILTER,
	ORGANIZATIONS_FILTER,
	SESSIONS_FILTER
} = CUSTOM_FUNCTION_OPERATORS;
const {AND, OR} = CONJUNCTIONS;
const {BETWEEN, CONTAINS} = FUNCTIONAL_OPERATORS;
const {EQ, GE, GT, LE, LT, NE} = RELATIONAL_OPERATORS;
const {IS_KNOWN, IS_UNKNOWN} = DISPLAY_ONLY_OPERATORS;
const {
	NOT_ACTIVITIES_FILTER_BY_COUNT,
	NOT_CONTAINS,
	NOT_ORGANIZATIONS_FILTER
} = NOT_OPERATORS;

const {
	ACCOUNT_NUMBER,
	ACCOUNT_TEXT,
	BEHAVIOR,
	BOOLEAN,
	DATE,
	DATE_TIME,
	DURATION,
	INTEREST,
	NUMBER,
	ORGANIZATION_BOOLEAN,
	ORGANIZATION_DATE,
	ORGANIZATION_DATE_TIME,
	ORGANIZATION_NUMBER,
	ORGANIZATION_SELECT_TEXT,
	ORGANIZATION_TEXT,
	SELECT_TEXT,
	SESSION_DATE_TIME,
	SESSION_GEOLOCATION,
	SESSION_NUMBER,
	SESSION_TEXT,
	TEXT
} = PROPERTY_TYPES;

export const CUSTOM_FUNCTION_OPERATOR_KEY_MAP = {
	['accounts.filter']: ACCOUNTS_FILTER,
	['accounts.filterByCount']: ACCOUNTS_FILTER_BY_COUNT,
	['activities.filter']: ACTIVITIES_FILTER,
	['activities.filterByCount']: ACTIVITIES_FILTER_BY_COUNT,
	['interests.filter']: INTERESTS_FILTER,
	['organizations.filter']: ORGANIZATIONS_FILTER,
	['sessions.filter']: SESSIONS_FILTER
};

export const SUPPORTED_CONJUNCTIONS = [
	{
		key: 'and',
		label: Liferay.Language.get('and'),
		name: AND
	},
	{
		key: 'or',
		label: Liferay.Language.get('or'),
		name: OR
	}
];

export const SUPPORTED_OPERATORS = {
	[ACCOUNT_NUMBER]: [
		{
			key: ACCOUNTS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ACCOUNTS_FILTER
		}
	],
	[ACCOUNT_TEXT]: [
		{
			key: ACCOUNTS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ACCOUNTS_FILTER
		}
	],
	[BEHAVIOR]: [
		{
			key: ACTIVITIES_FILTER_BY_COUNT,
			label: Liferay.Language.get('has-fragment'),
			name: ACTIVITIES_FILTER_BY_COUNT
		},
		{
			key: NOT_ACTIVITIES_FILTER_BY_COUNT,
			label: Liferay.Language.get('has-not-fragment'),
			name: NOT_ACTIVITIES_FILTER_BY_COUNT
		}
	],
	[BOOLEAN]: [
		{
			key: EQ,
			label: Liferay.Language.get('is-fragment'),
			name: EQ
		}
	],
	[DATE]: [
		{
			key: LT,
			label: Liferay.Language.get('is-before-fragment'),
			name: LT
		},
		{
			key: EQ,
			label: Liferay.Language.get('is-fragment'),
			name: EQ
		},
		{
			key: GT,
			label: Liferay.Language.get('is-after-fragment'),
			name: GT
		}
	],
	[DATE_TIME]: [
		{
			key: LT,
			label: Liferay.Language.get('is-before-fragment'),
			name: LT
		},
		{
			key: EQ,
			label: Liferay.Language.get('is-fragment'),
			name: EQ
		},
		{
			key: GT,
			label: Liferay.Language.get('is-after-fragment'),
			name: GT
		}
	],
	[DURATION]: [
		{
			key: GT,
			label: Liferay.Language.get('greater-than-fragment'),
			name: GT
		},
		{
			key: LT,
			label: Liferay.Language.get('less-than-fragment'),
			name: LT
		}
	],
	[INTEREST]: [
		{
			key: INTERESTS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: INTERESTS_FILTER
		}
	],
	[NUMBER]: [
		{
			key: EQ,
			label: Liferay.Language.get('is-equal-to-fragment'),
			name: EQ
		},
		{
			key: GT,
			label: Liferay.Language.get('greater-than-fragment'),
			name: GT
		},
		{
			key: LT,
			label: Liferay.Language.get('less-than-fragment'),
			name: LT
		},
		{
			key: NE,
			label: Liferay.Language.get('is-not-equal-to-fragment'),
			name: NE
		},
		{
			key: isKnown,
			label: Liferay.Language.get('is-known-fragment'),
			name: IS_KNOWN
		},
		{
			key: isUnknown,
			label: Liferay.Language.get('is-unknown-fragment'),
			name: IS_UNKNOWN
		}
	],
	[ORGANIZATION_BOOLEAN]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[ORGANIZATION_DATE]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[ORGANIZATION_DATE_TIME]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[ORGANIZATION_NUMBER]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[ORGANIZATION_SELECT_TEXT]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		},
		{
			key: NOT_ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-not-fragment'),
			name: NOT_ORGANIZATIONS_FILTER
		}
	],
	[ORGANIZATION_TEXT]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[SELECT_TEXT]: [
		{
			key: EQ,
			label: Liferay.Language.get('is-fragment'),
			name: EQ
		},
		{
			key: NE,
			label: Liferay.Language.get('is-not-fragment'),
			name: NE
		}
	],
	[SESSION_DATE_TIME]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[SESSION_GEOLOCATION]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[SESSION_NUMBER]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[SESSION_TEXT]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[TEXT]: [
		{
			key: EQ,
			label: Liferay.Language.get('is-fragment'),
			name: EQ
		},
		{
			key: NE,
			label: Liferay.Language.get('is-not-fragment'),
			name: NE
		},
		{
			key: CONTAINS,
			label: Liferay.Language.get('contains-fragment'),
			name: CONTAINS
		},
		{
			key: NOT_CONTAINS,
			label: Liferay.Language.get('does-not-contain-fragment'),
			name: NOT_CONTAINS
		},
		{
			key: isKnown,
			label: Liferay.Language.get('is-known-fragment'),
			name: IS_KNOWN
		},
		{
			key: isUnknown,
			label: Liferay.Language.get('is-unknown-fragment'),
			name: IS_UNKNOWN
		}
	]
};

export const SUPPORTED_PROPERTY_TYPES = {
	[ACCOUNT_NUMBER]: [ACCOUNTS_FILTER],
	[ACCOUNT_TEXT]: [ACCOUNTS_FILTER],
	[BEHAVIOR]: [ACTIVITIES_FILTER_BY_COUNT, NOT_ACTIVITIES_FILTER_BY_COUNT],
	[BOOLEAN]: [EQ],
	[DATE]: [EQ, GE, GT, LE, LT, NE],
	[DATE_TIME]: [EQ, GE, GT, LE, LT, NE],
	[DURATION]: [GT, LT],
	[INTEREST]: [INTERESTS_FILTER],
	[NUMBER]: [EQ, GE, GT, LE, LT, NE],
	[SESSION_DATE_TIME]: [SESSION_DATE_TIME],
	[SESSION_NUMBER]: [SESSIONS_FILTER],
	[SESSION_TEXT]: [SESSIONS_FILTER],
	[TEXT]: [EQ, NE, CONTAINS, NOT_CONTAINS, IS_KNOWN, IS_UNKNOWN]
};

/**
 * Values for criteria row inputs.
 */

export const BOOLEAN_OPTIONS = [
	{
		label: 'TRUE',
		value: 'true'
	},
	{
		label: 'FALSE',
		value: 'false'
	}
];

export const INTEREST_BOOLEAN_OPTIONS = [
	{
		label: Liferay.Language.get('is-fragment'),
		value: 'true'
	},
	{
		label: Liferay.Language.get('is-not-fragment'),
		value: 'false'
	}
];

export const OCCURENCE_OPTIONS = [
	{
		key: GE,
		label: Liferay.Language.get('at-least-fragment'),
		value: GE
	},
	{
		key: 'le',
		label: Liferay.Language.get('at-most-fragment'),
		value: LE
	}
];

export const GEOLOCATION_OPTIONS = [
	{
		label: Liferay.Language.get('was-fragment'),
		value: EQ
	},
	{
		label: Liferay.Language.get('was-not-fragment'),
		value: NE
	},
	{
		label: Liferay.Language.get('contained-fragment'),
		value: CONTAINS
	},
	{
		label: Liferay.Language.get('did-not-contain-fragment'),
		value: NOT_CONTAINS
	}
];

export const TIME_CONJUNCTION_OPTIONS = [
	{
		label: Liferay.Language.get('since-fragment'),
		value: SINCE
	},
	{
		label: Liferay.Language.get('after-fragment'),
		value: GT
	},
	{
		label: Liferay.Language.get('before-fragment'),
		value: LT
	},
	{
		label: Liferay.Language.get('between-fragment'),
		value: BETWEEN
	},
	{
		label: Liferay.Language.get('ever-fragment'),
		value: EVER
	},
	{
		label: Liferay.Language.get('on-fragment'),
		value: EQ
	}
];

export const ACTIVITY_KEY = 'activityKey';
export const LAST_24_HOURS = 'last24Hours';
export const LAST_7_DAYS = 'last7Days';
export const LAST_28_DAYS = 'last28Days';
export const LAST_30_DAYS = 'last30Days';
export const LAST_90_DAYS = 'last90Days';
export const YESTERDAY = 'yesterday';

export const TIME_PERIOD_OPTIONS = [
	{
		label: Liferay.Language.get('last-24-hours'),
		value: LAST_24_HOURS
	},
	{
		label: Liferay.Language.get('yesterday'),
		value: YESTERDAY
	},
	{
		label: Liferay.Language.get('last-seven-days'),
		value: LAST_7_DAYS
	},
	{
		label: Liferay.Language.get('last-28-days'),
		value: LAST_28_DAYS
	},
	{
		label: Liferay.Language.get('last-30-days'),
		value: LAST_30_DAYS
	},
	{
		label: Liferay.Language.get('last-90-days'),
		value: LAST_90_DAYS
	}
];
