import {TimeSpans} from 'shared/util/constants';

export const EVER = 'ever';
export const SINCE = 'since';

export const isKnown = 'is-known';
export const isUnknown = 'is-unknown';

/**
 * Constants for date formatting
 */

export const INPUT_DATE_FORMAT = 'YYYY-MM-DD';
export const INPUT_DATE_TIME_FORMAT = 'YYYY-MM-DDTHH:mmZ';
export const INPUT_DISPLAY_DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm';

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

export enum PropertyTypes {
	AccountNumber = 'account-number',
	AccountText = 'account-text',
	Behavior = 'behavior',
	Boolean = 'boolean',
	Date = 'date',
	DateTime = 'date-time',
	Duration = 'duration',
	Interest = 'interest',
	Number = 'number',
	OrganizationBoolean = 'organization-boolean',
	OrganizationDate = 'organization-date',
	OrganizationDateTime = 'organization-date-time',
	OrganizationNumber = 'organization-number',
	OrganizationSelectText = 'organization-select-text',
	OrganizationText = 'organization-text',
	SelectText = 'select-text',
	SessionDateTime = 'session-date-time',
	SessionGeolocation = 'session-geolocation',
	SessionNumber = 'session-number',
	SessionText = 'session-text',
	Text = 'text'
}

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

export const SUPPORTED_OPERATORS_MAP = {
	[PropertyTypes.AccountNumber]: [
		{
			key: ACCOUNTS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ACCOUNTS_FILTER
		}
	],
	[PropertyTypes.AccountText]: [
		{
			key: ACCOUNTS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ACCOUNTS_FILTER
		}
	],
	[PropertyTypes.Behavior]: [
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
	[PropertyTypes.Boolean]: [
		{
			key: EQ,
			label: Liferay.Language.get('is-fragment'),
			name: EQ
		}
	],
	[PropertyTypes.Date]: [
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
	[PropertyTypes.DateTime]: [
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
	[PropertyTypes.Duration]: [
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
	[PropertyTypes.Interest]: [
		{
			key: INTERESTS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: INTERESTS_FILTER
		}
	],
	[PropertyTypes.Number]: [
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
	[PropertyTypes.OrganizationBoolean]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[PropertyTypes.OrganizationDate]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[PropertyTypes.OrganizationDateTime]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[PropertyTypes.OrganizationNumber]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[PropertyTypes.OrganizationSelectText]: [
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
	[PropertyTypes.OrganizationText]: [
		{
			key: ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: ORGANIZATIONS_FILTER
		}
	],
	[PropertyTypes.SelectText]: [
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
	[PropertyTypes.SessionDateTime]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[PropertyTypes.SessionGeolocation]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[PropertyTypes.SessionNumber]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[PropertyTypes.SessionText]: [
		{
			key: SESSIONS_FILTER,
			label: Liferay.Language.get('is-fragment'),
			name: SESSIONS_FILTER
		}
	],
	[PropertyTypes.Text]: [
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

export const SUPPORTED_PROPERTY_TYPES_MAP = {
	[PropertyTypes.AccountNumber]: [ACCOUNTS_FILTER],
	[PropertyTypes.AccountText]: [ACCOUNTS_FILTER],
	[PropertyTypes.Behavior]: [
		ACTIVITIES_FILTER_BY_COUNT,
		NOT_ACTIVITIES_FILTER_BY_COUNT
	],
	[PropertyTypes.Boolean]: [EQ],
	[PropertyTypes.Date]: [EQ, GE, GT, LE, LT, NE],
	[PropertyTypes.DateTime]: [EQ, GE, GT, LE, LT, NE],
	[PropertyTypes.Duration]: [GT, LT],
	[PropertyTypes.Interest]: [INTERESTS_FILTER],
	[PropertyTypes.Number]: [EQ, GE, GT, LE, LT, NE],
	[PropertyTypes.SessionDateTime]: [PropertyTypes.SessionDateTime],
	[PropertyTypes.SessionNumber]: [SESSIONS_FILTER],
	[PropertyTypes.SessionText]: [SESSIONS_FILTER],
	[PropertyTypes.Text]: [EQ, NE, CONTAINS, NOT_CONTAINS, IS_KNOWN, IS_UNKNOWN]
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

export const TIME_PERIOD_OPTIONS = [
	{
		label: Liferay.Language.get('last-24-hours'),
		value: TimeSpans.Last24Hours
	},
	{
		label: Liferay.Language.get('yesterday'),
		value: TimeSpans.Yesterday
	},
	{
		label: Liferay.Language.get('last-seven-days'),
		value: TimeSpans.Last7Days
	},
	{
		label: Liferay.Language.get('last-28-days'),
		value: TimeSpans.Last28Days
	},
	{
		label: Liferay.Language.get('last-30-days'),
		value: TimeSpans.Last30Days
	},
	{
		label: Liferay.Language.get('last-90-days'),
		value: TimeSpans.Last90Days
	}
];

export {TimeSpans};

/**
 * Constants for static property groups.
 */

export {default as INDIVIDUAL_PROPERTIES} from './individual-properties';
export {default as ORGANIZATION_PROPERTIES} from './organization-properties';
export {default as WEB_BEHAVIORS} from './web-behaviors';
export {default as SESSION_PROPERTIES} from './session-properties';
