import FaroConstants from 'shared/util/constants';
import sendRequest from 'shared/util/request';
import {INDIVIDUAL_COUNT, NAME, SCORE} from 'shared/util/pagination';

const {
	pagination: {cur: DEFAULT_PAGE, delta: DEFAULT_DELTA, orderDescending},
	timeIntervals: {day: DEFAULT_TIME_INTERVAL}
} = FaroConstants;

const DEFAULT_MAX = 30;

export function fetch(params) {
	return search({
		...params,
		orderByFields: [
			{fieldName: NAME, orderBy: orderDescending, system: true}
		]
	}).then(({items}) => {
		if (items && items.length) {
			return items[0];
		}

		throw new Error('No entity by that name exists');
	});
}

export function search(params) {
	const {
		contactsEntityId,
		delta = DEFAULT_DELTA,
		groupId,
		interval = DEFAULT_TIME_INTERVAL,
		max = DEFAULT_MAX,
		name,
		orderByFields = [
			{fieldName: SCORE, orderBy: orderDescending, system: true}
		],
		page = DEFAULT_PAGE,
		query = ''
	} = params;

	return sendRequest({
		data: {
			contactsEntityId,
			cur: page,
			delta,
			interval,
			max,
			name,
			orderByFields,
			query
		},
		method: 'GET',
		path: `contacts/${groupId}/interest`
	});
}

export function searchKeywords(params) {
	const {delta = DEFAULT_DELTA, groupId, page, query} = params;

	return sendRequest({
		data: {
			cur: page,
			delta,
			query
		},
		method: 'GET',
		path: `contacts/${groupId}/interest/keywords`
	});
}
export function searchKeywordAggregations(params) {
	const {
		contactsEntityId,
		contactsEntityType,
		delta = DEFAULT_DELTA,
		groupId,
		orderByFields = [
			{fieldName: INDIVIDUAL_COUNT, orderBy: orderDescending}
		],
		page = DEFAULT_PAGE,
		query = ''
	} = params;

	return sendRequest({
		data: {
			contactsEntityId,
			contactsEntityType,
			cur: page,
			delta,
			orderByFields,
			query
		},
		method: 'GET',
		path: `contacts/${groupId}/interest/keywords/aggregations`
	});
}
