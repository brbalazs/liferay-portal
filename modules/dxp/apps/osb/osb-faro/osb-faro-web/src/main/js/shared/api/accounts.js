import FaroConstants from 'shared/util/constants';
import sendRequest from 'shared/util/request';
import {ACCOUNT_NAME} from 'shared/util/pagination';
import {escapeSingleQuotes} from 'contacts/components/segment-editor/dynamic/utils/odata';

const {
	pagination: {cur: DEFAULT_PAGE, delta: DEFAULT_DELTA, orderDefault}
} = FaroConstants;

export function fetch({accountId, groupId}) {
	return sendRequest({
		method: 'GET',
		path: `contacts/${groupId}/account/${accountId}`
	});
}

export function fetchDetails({accountId, groupId}) {
	return sendRequest({
		method: 'GET',
		path: `contacts/${groupId}/account/${accountId}/details`
	});
}

export function fetchFieldValues({fieldMappingId, groupId, query}) {
	return sendRequest({
		data: {
			delta: DEFAULT_DELTA,
			fieldMappingId,
			query: escapeSingleQuotes(query)
		},
		method: 'GET',
		path: `contacts/${groupId}/account/field_values`
	});
}

export function search({
	channelId = '',
	delta = DEFAULT_DELTA,
	groupId,
	orderByFields = [
		{
			fieldName: ACCOUNT_NAME,
			orderBy: orderDefault
		}
	],
	page = DEFAULT_PAGE,
	query = '',
	...otherParams
}) {
	return sendRequest({
		data: {
			channelId,
			cur: page,
			delta,
			orderByFields,
			query,
			...otherParams
		},
		method: 'POST',
		path: `contacts/${groupId}/account/search`
	});
}
