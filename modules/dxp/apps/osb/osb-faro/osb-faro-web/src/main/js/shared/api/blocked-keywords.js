import sendRequest from 'shared/util/request';

function delete$({groupId, ids}) {
	return sendRequest({
		data: {ids},
		method: 'DELETE',
		path: `main/${groupId}/blocked_keywords`
	});
}
export {delete$ as delete};

export function fetch({groupId, ...data}) {
	return sendRequest({
		data,
		method: 'GET',
		path: `main/${groupId}/blocked_keywords`
	});
}

export function insertMany({groupId, keywords}) {
	return sendRequest({
		data: {keywords},
		method: 'POST',
		path: `main/${groupId}/blocked_keywords`
	});
}
