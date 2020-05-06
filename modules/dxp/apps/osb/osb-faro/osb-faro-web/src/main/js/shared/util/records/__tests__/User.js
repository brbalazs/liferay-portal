import FaroConstants from 'shared/util/constants';
import User from '../User';

const {
	userRoleNames: {administrator, member, owner}
} = FaroConstants;

const adminUser = new User({roleName: administrator});

const memberUser = new User({roleName: member});

const ownerUser = new User({roleName: owner});

describe('User', () => {
	it('should return a new User', () => {
		const user = new User();

		expect(user).toBeTruthy();
	});

	it('should be able to determine if a user has a permission, given an array of roles or a single role', () => {
		const user = new User({roleName: member});

		expect(user.hasPermission([administrator, owner])).toBe(false);

		expect(user.hasPermission([administrator, member, owner])).toBe(true);

		expect(user.hasPermission(member)).toBe(true);
	});

	it('should be able to determine if a user is a member', () => {
		expect(memberUser.isMember()).toBe(true);

		expect(adminUser.isMember()).toBe(false);

		expect(ownerUser.isMember()).toBe(false);
	});

	it('should be able to determine if a user is an owner', () => {
		expect(ownerUser.isOwner()).toBe(true);

		expect(adminUser.isOwner()).toBe(false);

		expect(memberUser.isOwner()).toBe(false);
	});

	it('should be able to determine if a user has an administrative level of access', () => {
		expect(adminUser.isAdmin()).toBe(true);

		expect(ownerUser.isAdmin()).toBe(true);

		expect(memberUser.isAdmin()).toBe(false);
	});
});
