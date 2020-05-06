import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {CSV} from '../CSV';
import {DataSource, User} from 'shared/util/records';
import {shallow} from 'enzyme';

const {userRoleNames} = FaroConstants;

const defaultProps = {
	currentUser: new User(data.mockUser()),
	dataSource: new DataSource(data.mockCSVDataSource()),
	groupId: '23',
	id: 'test'
};

describe('CSV', () => {
	it('should render', () => {
		const component = shallow(<CSV {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should not render an Edit CSV Configuration button if the user role is member', () => {
		const component = shallow(
			<CSV
				{...defaultProps}
				currentUser={
					new User(data.mockUser(0, {roleName: userRoleNames.member}))
				}
			/>
		);

		expect(component.findWhere(n => n.text() === 'Edit CSV').length).toBe(
			0
		);
	});
});
