import FaroConstants from 'shared/util/constants';
import sendRequest from 'shared/util/request';
import {escapeSingleQuotes} from 'contacts/components/segment-editor/dynamic/utils/odata';
import {FAMILY_NAME, GIVEN_NAME} from 'shared/util/pagination';

const {
	cur: DEFAULT_PAGE,
	delta: DEFAULT_DELTA,
	orderDefault
} = FaroConstants.pagination;

export function fetch({groupId, individualId}) {
	return sendRequest({
		method: 'GET',
		path: `contacts/${groupId}/individual/${individualId}`
	});
}

export function fetchDetails({groupId, individualId}) {
	return sendRequest({
		method: 'GET',
		path: `contacts/${groupId}/individual/${individualId}/details`
	});
}

export function fetchMembership({
	cur,
	delta,
	groupId,
	individualSegmentId,
	orderByFields,
	query
}) {
	return sendRequest({
		data: {
			cur,
			delta,
			individualSegmentId,
			orderByFields,
			query
		},
		method: 'GET',
		path: `contacts/${groupId}/individual`
	});
}

export function fetchFieldValues({fieldMappingId, groupId, query}) {
	return sendRequest({
		data: {
			delta: 20,
			fieldMappingId,
			query: escapeSingleQuotes(query)
		},
		method: 'GET',
		path: `contacts/${groupId}/individual/field_values`
	});
}

export function search(params) {
	const {
		accountId = '',
		channelId = '',
		delta = DEFAULT_DELTA,
		groupId,
		individualSegmentId = '',
		notIndividualSegmentId = '',
		orderByFields = [
			{
				fieldName: GIVEN_NAME,
				orderBy: orderDefault
			},
			{
				fieldName: FAMILY_NAME,
				orderBy: orderDefault
			}
		],
		page = DEFAULT_PAGE,
		query = '',
		...otherParams
	} = params;

	return sendRequest({
		data: {
			accountId,
			channelId,
			cur: page,
			delta,
			individualSegmentId,
			notIndividualSegmentId,
			orderByFields,
			query,
			...otherParams
		},
		method: 'POST',
		path: `contacts/${groupId}/individual/search`
	});
}
