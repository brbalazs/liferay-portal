jest.mock('../CSV', () => 'CSV');
jest.mock('../Liferay', () => 'LiferayDataSource');
jest.mock('../Salesforce', () => 'Salesforce');

import * as data from 'test/data';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {shallow} from 'enzyme';
import {View} from '../View';

const defaultProps = {
	currentUser: data.getImmutableMock(User, data.mockUser),
	groupId: '23',
	id: '24'
};

describe('View', () => {
	it('should render a CSV data-source page', () => {
		const component = shallow(
			<View
				{...defaultProps}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockCSVDataSource
				)}
			/>
		);

		expect(component.name()).toBe('CSV');
	});

	it('should render a Liferay data-source page', () => {
		const component = shallow(
			<View
				{...defaultProps}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource
				)}
			/>
		);

		expect(component.name()).toBe('LiferayDataSource');
	});

	it('should render a Salesforce data-source page', () => {
		const component = shallow(
			<View
				{...defaultProps}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockSalesforceDataSource
				)}
			/>
		);

		expect(component.name()).toBe('Salesforce');
	});
});
