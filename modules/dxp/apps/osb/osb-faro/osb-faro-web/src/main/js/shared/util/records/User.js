import FaroConstants from 'shared/util/constants';
import {isArray} from 'lodash';
import {Record} from 'immutable';

const {
	userRoleNames: {administrator, member, owner},
	userStatuses: {approved}
} = FaroConstants;

export default class User extends (new Record({
	emailAddress: null,
	groupId: null,
	id: null,
	name: '',
	roleName: null,
	screenName: '',
	status: approved,
	userId: null
})) {
	constructor(params = {}) {
		super(params);
	}

	hasPermission(permissions) {
		const {roleName} = this;

		return isArray(permissions)
			? permissions.includes(roleName)
			: roleName === permissions;
	}

	isAdmin() {
		return this.roleName === administrator || this.roleName === owner;
	}

	isMember() {
		return this.roleName === member;
	}

	isOwner() {
		return this.roleName === owner;
	}
}
