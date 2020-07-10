import FaroConstants from 'shared/util/constants';
import sendRequest from 'shared/util/request';
import {pickBy} from 'lodash';

export const DEFAULT_ACTIVITY_MAX = 30;

export const DEFAULT_ACTIVITY_INTERVAL = FaroConstants.timeIntervals.day;

export function fetchHistory({
	channelId = '',
	contactsEntityId,
	contactsEntityType,
	groupId,
	interval = DEFAULT_ACTIVITY_INTERVAL,
	max = DEFAULT_ACTIVITY_MAX,
	rangeEnd = null,
	rangeStart = null
}) {
	return sendRequest({
		data: {
			channelId,
			contactsEntityId,
			contactsEntityType,
			interval,
			...pickBy({
				max,
				rangeEnd,
				rangeStart
			})
		},
		method: 'GET',
		path: `contacts/${groupId}/activity/history`
	});
}

export function fetchGroup({
	channelId = '',
	contactsEntityId,
	contactsEntityType,
	cur,
	delta,
	endDate,
	groupId,
	orderByFields,
	query,
	startDate
}) {
	return sendRequest({
		data: {
			channelId,
			contactsEntityId,
			contactsEntityType,
			cur,
			delta,
			endDate,
			orderByFields,
			query,
			startDate
		},
		method: 'GET',
		path: `contacts/${groupId}/activity_group`
	});
}

export function searchAssets({
	applicationId,
	channelId = '',
	eventId,
	groupId,
	...otherParams
}) {
	return sendRequest({
		data: {
			applicationId,
			channelId,
			eventId,
			...otherParams
		},
		method: 'GET',
		path: `contacts/${groupId}/activity/asset`
	});
}

export function searchCount({action, groupId, ...otherParams}) {
	return sendRequest({
		data: {
			...otherParams,
			action
		},
		method: 'GET',
		path: `contacts/${groupId}/activity/count`
	});
}
