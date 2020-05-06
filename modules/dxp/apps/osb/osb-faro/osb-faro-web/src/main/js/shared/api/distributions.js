import FaroConstants from 'shared/util/constants';
import sendRequest from 'shared/util/request';

const {
	fieldContexts: {demographics}
} = FaroConstants;

export function fetch(params) {
	const {context = demographics, groupId, ...otherParams} = params;

	const entityType = context === demographics ? 'individual' : 'account';

	return sendRequest({
		data: {
			...otherParams
		},
		method: 'GET',
		path: `contacts/${groupId}/${entityType}/distribution`
	});
}
