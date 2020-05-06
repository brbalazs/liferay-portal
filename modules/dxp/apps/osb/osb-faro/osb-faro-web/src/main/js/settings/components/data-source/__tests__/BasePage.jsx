import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {BaseDataSourcePage} from '../BasePage';
import {DataSource, User} from 'shared/util/records';
import {shallow} from 'enzyme';

const {
	dataSourceStates: {undefinedError},
	userRoleNames: {member}
} = FaroConstants;

describe('BaseDataSourcePage', () => {
	it('should render', () => {
		const component = shallow(
			<BaseDataSourcePage
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource
				)}
				groupId='23'
				id='test'
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render a delete button if showDelete is true', () => {
		const component = shallow(
			<BaseDataSourcePage
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource
				)}
				groupId='23'
				id='test'
				showDelete
			/>
		);

		expect(component.prop('pageActions')).toMatchSnapshot();
	});

	it('should NOT render a delete button if the user is not an admin level', () => {
		const component = shallow(
			<BaseDataSourcePage
				currentUser={data.getImmutableMock(User, data.mockUser, '23', {
					roleName: member
				})}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource
				)}
				groupId='23'
				id='test'
				showDelete
			/>
		);

		expect(component.prop('pageActions')).toMatchSnapshot();
	});

	it('should render with an UNDEFINED_ERROR message in the datasource status column', () => {
		const component = shallow(
			<BaseDataSourcePage
				currentUser={data.getImmutableMock(User, data.mockUser, '23', {
					roleName: member
				})}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource,
					'test',
					{state: undefinedError}
				)}
				groupId='23'
				id='test'
				showDelete
			/>
		);

		expect(component.find('DataSourceStatus')).toMatchSnapshot();
	});
});
