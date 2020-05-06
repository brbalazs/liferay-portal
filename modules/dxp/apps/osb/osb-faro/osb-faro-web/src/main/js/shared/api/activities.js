import FaroConstants from 'shared/util/constants';
import sendRequest from 'shared/util/request';

export const DEFAULT_ACTIVITY_MAX = 30;

export const DEFAULT_ACTIVITY_INTERVAL = FaroConstants.timeIntervals.day;

export function fetchHistory({
	channelId = '',
	contactsEntityId,
	contactsEntityType,
	groupId,
	interval = DEFAULT_ACTIVITY_INTERVAL,
	max = DEFAULT_ACTIVITY_MAX
}) {
	return sendRequest({
		data: {
			channelId,
			contactsEntityId,
			contactsEntityType,
			interval,
			max
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
	eventId,
	groupId,
	...otherParams
}) {
	return sendRequest({
		data: {
			applicationId,
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
