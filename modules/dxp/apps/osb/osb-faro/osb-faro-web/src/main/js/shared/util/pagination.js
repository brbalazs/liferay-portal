import FaroConstants from 'shared/util/constants';
import {ACCOUNTS, INDIVIDUALS, SEGMENTS, USERS} from 'shared/util/router';
import {Map} from 'immutable';
import {PropTypes} from 'prop-types';

const {
	cur: defaultCur,
	delta: defaultDelta,
	orderAscending,
	orderDefault,
	orderDescending
} = FaroConstants.pagination;

export const ABANDONMENTS_METRIC = 'abandonmentsMetric';
export const ACCOUNT_NAME = 'accountName';
export const ACCOUNT_TYPE = 'accountType';
export const ACTIVITIES_COUNT = 'activitiesCount';
export const AVG_TIME_ON_PAGE_METRIC = 'avgTimeOnPageMetric';
export const BOUNCE_RATE_METRIC = 'bounceRateMetric';
export const COMMENTS_METRIC = 'commentsMetric';
export const COMPLETION_TIME_METRIC = 'completionTimeMetric';
export const COUNT = 'count';
export const CREATE_DATE = 'createDate';
export const DATE_ADDED = 'dateAdded';
export const DATE_CHANGED = 'dateChanged';
export const DATE_CREATED = 'dateCreated';
export const DATE_FIRST = 'dateFirst';
export const DATE_RECORDED = 'dateRecorded';
export const DISPLAY_NAME = 'displayName';
export const DOWNLOADS_METRIC = 'downloadsMetric';
export const EMAIL_ADDRESS = 'emailAddress';
export const ENTRANCES_METRIC = 'entrancesMetric';
export const EXIT_RATE_METRIC = 'exitRateMetric';
export const FAMILY_NAME = 'familyName';
export const FIRST_NAME = 'firstName';
export const GIVEN_NAME = 'givenName';
export const INDIVIDUAL_COUNT = 'individualCount';
export const INDIVIDUAL_EMAIL = 'individualEmail';
export const INDIVIDUAL_NAME = 'individualName';
export const INTERESTS = 'interests';
export const JOB_TITLE = 'jobTitle';
export const KEYWORD = 'keyword';
export const LAST_ACTIVITY_DATE = 'lastActivityDate';
export const LAST_NAME = 'lastName';
export const LOCATION = 'location';
export const MODIFIED_DATE = 'modifiedDate';
export const NAME = 'name';
export const OPERATION = 'operation';
export const PREVIEWS_METRIC = 'previewsMetric';
export const PROVIDER_TYPE = 'provider.type';
export const RATINGS_METRIC = 'ratingsMetric';
export const READING_TIME_METRIC = 'readingTimeMetric';
export const ROLE_NAME = 'roleName';
export const SCORE = 'score';
export const START_TIME = 'startTime';
export const STATUS = 'status';
export const SUBMISSIONS_METRIC = 'submissionsMetric';
export const TITLE = 'title';
export const TOTAL_ACTIVITIES = 'totalActivities';
export const USER_NAME = 'author/name';
export const URL = 'url';
export const UNIQUE_VISITS_COUNT = 'uniqueVisitsCount';
export const VIEWS_METRIC = 'viewsMetric';
export const VISITORS_METRIC = 'visitorsMetric';

const INVERTED_SORT_FIELDS = [
	ABANDONMENTS_METRIC,
	ACTIVITIES_COUNT,
	AVG_TIME_ON_PAGE_METRIC,
	BOUNCE_RATE_METRIC,
	COMMENTS_METRIC,
	COMPLETION_TIME_METRIC,
	COUNT,
	CREATE_DATE,
	DATE_ADDED,
	DATE_CHANGED,
	DATE_CREATED,
	DATE_FIRST,
	DATE_RECORDED,
	DOWNLOADS_METRIC,
	ENTRANCES_METRIC,
	EXIT_RATE_METRIC,
	INDIVIDUAL_COUNT,
	LAST_ACTIVITY_DATE,
	MODIFIED_DATE,
	PREVIEWS_METRIC,
	RATINGS_METRIC,
	READING_TIME_METRIC,
	SCORE,
	SUBMISSIONS_METRIC,
	START_TIME,
	TOTAL_ACTIVITIES,
	UNIQUE_VISITS_COUNT,
	VIEWS_METRIC,
	VISITORS_METRIC
];

export const paginationDefaults = {
	delta: defaultDelta,
	filterBy: new Map(),
	orderBy: orderDefault,
	orderByField: '',
	page: defaultCur,
	query: ''
};

export const paginationConfig = {
	delta: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
	filterBy: PropTypes.instanceOf(Map),
	orderBy: PropTypes.string,
	orderByField: PropTypes.string,
	page: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
	query: PropTypes.string
};

export const ACCESSOR_TO_FIELD_MAP = {
	['properties.accountType']: ACCOUNT_TYPE,
	['properties.individualCount']: INDIVIDUAL_COUNT,
	['properties.interests']: INTERESTS,
	['properties.jobTitle']: JOB_TITLE,
	['properties.location']: LOCATION,
	['properties.totalActivities']: TOTAL_ACTIVITIES,
	userName: USER_NAME,
	viewCount: UNIQUE_VISITS_COUNT
};

const SYSTEM_FIELDS = [
	ACTIVITIES_COUNT,
	DATE_CHANGED,
	DATE_CREATED,
	DATE_FIRST,
	INDIVIDUAL_COUNT,
	INDIVIDUAL_NAME,
	INDIVIDUAL_EMAIL,
	LAST_ACTIVITY_DATE,
	OPERATION,
	USER_NAME
];

export function buildOrderByFields({field, sortOrder}, entityType) {
	if (entityType === INDIVIDUALS && field === NAME) {
		return [GIVEN_NAME, FAMILY_NAME].map(columnAccessor =>
			createOrderByField(columnAccessor, sortOrder)
		);
	} else if (entityType === SEGMENTS && field === NAME) {
		return [
			{
				fieldName: field,
				orderBy: sortOrder,
				system: true
			}
		];
	} else if (entityType === ACCOUNTS && field === NAME) {
		return [createOrderByField(ACCOUNT_NAME, sortOrder)];
	} else if (entityType === USERS && field === NAME) {
		return [FIRST_NAME, LAST_NAME].map(columnAccessor =>
			createOrderByField(columnAccessor, sortOrder)
		);
	} else {
		return [createOrderByField(field, sortOrder)];
	}
}

export function createOrderByField(field, sortOrder) {
	return {
		fieldName: field,
		orderBy: sortOrder,
		system: SYSTEM_FIELDS.includes(field)
	};
}

export const getDefaultSortOrder = fieldName =>
	INVERTED_SORT_FIELDS.includes(fieldName) ? orderDescending : orderAscending;

export const invertOrder = currentOrder => {
	if (currentOrder) {
		return currentOrder === orderAscending
			? orderDescending
			: orderAscending;
	} else {
		return orderDefault;
	}
};
