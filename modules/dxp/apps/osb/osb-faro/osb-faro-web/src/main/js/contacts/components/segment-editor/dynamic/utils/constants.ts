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

export enum Conjunctions {
	AND = 'and',
	OR = 'or'
}

export enum CustomFunctionOperators {
	AccountsFilter = 'accounts-filter',
	AccountsFilterByCount = 'accounts-filter-by-count',
	ActivitiesFilter = 'activities-filter',
	ActivitiesFilterByCount = 'activities-filter-by-count',
	InterestsFilter = 'interests-filter',
	OrganizationsFilter = 'organizations-filter',
	SessionsFilter = 'sessions-filter'
}

export enum DisplayOnlyOperators {
	IsKnown = 'ne',
	IsUnknown = 'eq'
}

export enum FunctionalOperators {
	Between = 'between',
	Contains = 'contains'
}

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

export enum RelationalOperators {
	EQ = 'eq',
	GE = 'ge',
	GT = 'gt',
	LE = 'le',
	LT = 'lt',
	NE = 'ne'
}

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
	AccountsFilter,
	AccountsFilterByCount,
	ActivitiesFilter,
	ActivitiesFilterByCount,
	InterestsFilter,
	OrganizationsFilter,
	SessionsFilter
} = CustomFunctionOperators;
const {AND, OR} = Conjunctions;
const {EQ, GE, GT, LE, LT, NE} = RelationalOperators;
const {
	NOT_ACTIVITIES_FILTER_BY_COUNT,
	NOT_CONTAINS,
	NOT_ORGANIZATIONS_FILTER
} = NOT_OPERATORS;

export const CUSTOM_FUNCTION_OPERATOR_KEY_MAP = {
	['accounts.filter']: AccountsFilter,
	['accounts.filterByCount']: AccountsFilterByCount,
	['activities.filter']: ActivitiesFilter,
	['activities.filterByCount']: ActivitiesFilterByCount,
	['interests.filter']: InterestsFilter,
	['organizations.filter']: OrganizationsFilter,
	['sessions.filter']: SessionsFilter
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
			key: AccountsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: AccountsFilter
		}
	],
	[PropertyTypes.AccountText]: [
		{
			key: AccountsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: AccountsFilter
		}
	],
	[PropertyTypes.Behavior]: [
		{
			key: ActivitiesFilterByCount,
			label: Liferay.Language.get('has-fragment'),
			name: ActivitiesFilterByCount
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
			key: InterestsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: InterestsFilter
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
			name: DisplayOnlyOperators.IsKnown
		},
		{
			key: isUnknown,
			label: Liferay.Language.get('is-unknown-fragment'),
			name: DisplayOnlyOperators.IsUnknown
		}
	],
	[PropertyTypes.OrganizationBoolean]: [
		{
			key: OrganizationsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: OrganizationsFilter
		}
	],
	[PropertyTypes.OrganizationDate]: [
		{
			key: OrganizationsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: OrganizationsFilter
		}
	],
	[PropertyTypes.OrganizationDateTime]: [
		{
			key: OrganizationsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: OrganizationsFilter
		}
	],
	[PropertyTypes.OrganizationNumber]: [
		{
			key: OrganizationsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: OrganizationsFilter
		}
	],
	[PropertyTypes.OrganizationSelectText]: [
		{
			key: OrganizationsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: OrganizationsFilter
		},
		{
			key: NOT_ORGANIZATIONS_FILTER,
			label: Liferay.Language.get('is-not-fragment'),
			name: NOT_ORGANIZATIONS_FILTER
		}
	],
	[PropertyTypes.OrganizationText]: [
		{
			key: OrganizationsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: OrganizationsFilter
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
			key: SessionsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: SessionsFilter
		}
	],
	[PropertyTypes.SessionGeolocation]: [
		{
			key: SessionsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: SessionsFilter
		}
	],
	[PropertyTypes.SessionNumber]: [
		{
			key: SessionsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: SessionsFilter
		}
	],
	[PropertyTypes.SessionText]: [
		{
			key: SessionsFilter,
			label: Liferay.Language.get('is-fragment'),
			name: SessionsFilter
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
			key: FunctionalOperators.Contains,
			label: Liferay.Language.get('contains-fragment'),
			name: FunctionalOperators.Contains
		},
		{
			key: NOT_CONTAINS,
			label: Liferay.Language.get('does-not-contain-fragment'),
			name: NOT_CONTAINS
		},
		{
			key: isKnown,
			label: Liferay.Language.get('is-known-fragment'),
			name: DisplayOnlyOperators.IsKnown
		},
		{
			key: isUnknown,
			label: Liferay.Language.get('is-unknown-fragment'),
			name: DisplayOnlyOperators.IsUnknown
		}
	]
};

export const SUPPORTED_PROPERTY_TYPES_MAP = {
	[PropertyTypes.AccountNumber]: [AccountsFilter],
	[PropertyTypes.AccountText]: [AccountsFilter],
	[PropertyTypes.Behavior]: [
		ActivitiesFilterByCount,
		NOT_ACTIVITIES_FILTER_BY_COUNT
	],
	[PropertyTypes.Boolean]: [EQ],
	[PropertyTypes.Date]: [EQ, GE, GT, LE, LT, NE],
	[PropertyTypes.DateTime]: [EQ, GE, GT, LE, LT, NE],
	[PropertyTypes.Duration]: [GT, LT],
	[PropertyTypes.Interest]: [InterestsFilter],
	[PropertyTypes.Number]: [EQ, GE, GT, LE, LT, NE],
	[PropertyTypes.SessionDateTime]: [PropertyTypes.SessionDateTime],
	[PropertyTypes.SessionNumber]: [SessionsFilter],
	[PropertyTypes.SessionText]: [SessionsFilter],
	[PropertyTypes.Text]: [
		EQ,
		NE,
		FunctionalOperators.Contains,
		NOT_CONTAINS,
		DisplayOnlyOperators.IsKnown,
		DisplayOnlyOperators.IsUnknown
	]
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
		value: FunctionalOperators.Contains
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
		value: FunctionalOperators.Between
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
