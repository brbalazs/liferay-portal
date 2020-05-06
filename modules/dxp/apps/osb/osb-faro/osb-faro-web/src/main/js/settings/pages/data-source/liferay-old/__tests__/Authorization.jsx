import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {LiferayAuthorization} from '../Authorization';
import {shallow} from 'enzyme';

const {
	userRoleNames: {member}
} = FaroConstants;

const defaultProps = {
	currentUser: data.getImmutableMock(User, data.mockUser),
	dataSource: data.getImmutableMock(DataSource, data.mockLiferayDataSource),
	groupId: '23',
	id: 'test'
};

describe('LiferayAuthorization', () => {
	it('should render', () => {
		const component = shallow(<LiferayAuthorization {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render as read-only if the user is not authorized to make changes', () => {
		const component = shallow(
			<LiferayAuthorization
				{...defaultProps}
				currentUser={data.getImmutableMock(User, data.mockUser, '23', {
					roleName: member
				})}
			/>
		);

		expect(component.children().prop('authorized')).toBe(false);
	});
});
