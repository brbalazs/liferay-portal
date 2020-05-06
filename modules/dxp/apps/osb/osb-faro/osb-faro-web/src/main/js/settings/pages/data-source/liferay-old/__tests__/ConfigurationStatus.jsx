import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {ConfigurationStatus as LiferayConfigurationStatus} from '../ConfigurationStatus';
import {shallow} from 'enzyme';
import {User} from 'shared/util/records';

const {userRoleNames} = FaroConstants;

const defaultProps = {
	currentUser: data.getImmutableMock(User, data.mockUser),
	dataSource: data.mockLiferayDataSource(),
	groupId: '23',
	id: '23'
};

describe('LiferayConfigurationStatus', () => {
	beforeEach(() => {
		jest.useFakeTimers();
	});

	it('should render', () => {
		const component = shallow(
			<LiferayConfigurationStatus {...defaultProps} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as read-only if the user is not authorized to make changes', () => {
		const component = shallow(
			<LiferayConfigurationStatus
				{...defaultProps}
				currentUser={data.getImmutableMock(User, data.mockUser, 0, {
					roleName: userRoleNames.member
				})}
			/>
		);

		expect(
			component
				.find('Body')
				.children()
				.prop('disabled')
		).toBe(true);
	});
});
