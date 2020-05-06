import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import sendRequest from 'shared/util/request';
import {escapeSingleQuotes} from 'contacts/components/segment-editor/dynamic/utils/odata';
import {RESTParams} from 'shared/types';

const {cur: defaultCur, delta: defaultDelta} = FaroConstants.pagination;

interface IFetchFieldValues extends RESTParams {
	fieldName?: string;
	filter?: string;
}

export const fetchFieldValues = ({
	delta = defaultDelta,
	fieldName,
	filter,
	groupId,
	page = defaultCur,
	query
}: IFetchFieldValues): Promise<{
	disableSearch: boolean;
	items: string[];
	total: number;
}> =>
	sendRequest({
		data: {
			cur: page,
			delta,
			fieldName,
			filter,
			query: escapeSingleQuotes(query)
		},
		method: 'GET',
		path: `contacts/${groupId}/session/values`
	});
