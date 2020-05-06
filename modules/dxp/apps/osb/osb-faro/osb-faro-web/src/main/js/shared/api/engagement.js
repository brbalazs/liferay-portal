import FaroConstants from 'shared/util/constants';
import sendRequest from 'shared/util/request';

const {timeIntervals} = FaroConstants;

export const DEFAULT_ENGAGEMENT_MAX = 30;

export const DEFAULT_ENGAGEMENT_INTERVAL = timeIntervals.day;

/**
 * A combination of an Individual object with their engagement history.
 * @typedef {Object} IndividualWithEngagementHistory
 * @property {number} change - The amount of engagement change over last period.
 * @property {Array} engagementAggregations - The array of engagement data snapshots.
 * @property {Individual} individual - An Individual object.
 */

export function fetch({
	contactsEntityId,
	contactsEntityType,
	cur,
	delta,
	endDate,
	groupId,
	includeAnonymousUsers = false,
	orderByFields,
	query,
	startDate
}) {
	return sendRequest({
		data: {
			contactsEntityId,
			contactsEntityType,
			cur,
			delta,
			endDate,
			groupId,
			includeAnonymousUsers,
			orderByFields,
			query,
			startDate
		},
		method: 'GET',
		path: `contacts/${groupId}/engagement`
	});
}

export function fetchHistory({
	contactsEntityId,
	contactsEntityType,
	groupId,
	interval = DEFAULT_ENGAGEMENT_INTERVAL,
	max = DEFAULT_ENGAGEMENT_MAX
}) {
	return sendRequest({
		data: {
			contactsEntityId,
			contactsEntityType,
			groupId,
			interval,
			max
		},
		method: 'GET',
		path: `contacts/${groupId}/engagement/history`
	});
}

/**
 * Fetch a list of individuals with engagementHistory included.
 * @returns {{disableSearch: Boolean, items: Array.<IndividualWithEngagementHistory>, total: number}}
 */
export function fetchHistories({
	groupId,
	individualIds = [],
	interval = DEFAULT_ENGAGEMENT_INTERVAL,
	max = DEFAULT_ENGAGEMENT_MAX
}) {
	return sendRequest({
		data: {
			groupId,
			individualIds,
			interval,
			max
		},
		method: 'GET',
		path: `contacts/${groupId}/engagement/histories`
	});
}
