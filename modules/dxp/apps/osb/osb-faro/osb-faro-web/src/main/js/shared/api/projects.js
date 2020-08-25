import sendRequest from 'shared/util/request';

export const activate = ({groupId}) =>
	sendRequest({
		method: 'POST',
		path: `main/project/${groupId}/activate`
	});

export function create({
	corpProjectUuid,
	emailAddressDomains,
	friendlyURL,
	name,
	serverLocation
}) {
	return sendRequest({
		data: {
			corpProjectUuid,
			emailAddressDomains,
			friendlyURL,
			name,
			serverLocation
		},
		method: 'POST',
		path: 'main/project'
	});
}

export function configure({emailAddressDomains, friendlyURL, groupId, name}) {
	return sendRequest({
		data: {
			emailAddressDomains,
			friendlyURL,
			name
		},
		method: 'PUT',
		path: `main/project/${groupId}/configure`
	});
}

export function createTrial({
	emailAddressDomains,
	friendlyURL,
	name,
	serverLocation
}) {
	return sendRequest({
		data: {
			emailAddressDomains,
			friendlyURL,
			name,
			serverLocation
		},
		method: 'POST',
		path: 'main/project/trial'
	});
}

export function fetchMany() {
	return sendRequest({
		method: 'GET',
		path: 'main/project'
	});
}

export function fetch({groupId}) {
	return sendRequest({
		method: 'GET',
		path: `main/project/${groupId}`
	});
}

export function fetchEmailAddressDomains({groupId}) {
	return sendRequest({
		method: 'GET',
		path: `main/project/${groupId}/email_address_domains`
	});
}

export function fetchProjectState({groupId}) {
	return sendRequest({
		method: 'GET',
		path: `main/project/${groupId}`
	});
}

export function fetchProjectViaCorpProjectUuid({corpProjectUuid}) {
	return sendRequest({
		method: 'GET',
		path: `main/project/corpProjectUuid/${corpProjectUuid}`
	});
}

export function update({emailAddressDomains, friendlyURL, groupId, name}) {
	return sendRequest({
		data: {
			emailAddressDomains,
			friendlyURL: friendlyURL && `/${friendlyURL}`,
			name
		},
		method: 'PUT',
		path: `main/project/${groupId}`
	});
}

export function fetchJoinableProjects() {
	return sendRequest({
		method: 'GET',
		path: 'main/project/joinable'
	});
}

export function sendRequestAccess(groupId) {
	return sendRequest({
		method: 'POST',
		path: `main/${groupId}/user/join_request`
	});
}
