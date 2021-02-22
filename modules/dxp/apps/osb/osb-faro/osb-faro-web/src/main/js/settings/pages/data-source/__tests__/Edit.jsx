import * as data from 'test/data';
import React from 'react';
import {DataSource} from 'shared/util/records';
import {Edit} from '../Edit';
import {shallow} from 'enzyme';

const defaultProps = {
	groupId: '23',
	id: '23'
};

describe('Edit', () => {
	it('should render a CSV data-source page', () => {
		const component = shallow(
			<Edit
				{...defaultProps}
				dataSource={new DataSource(data.mockCSVDataSource())}
			/>
		);

		expect(component.name()).toBe('Connect(ConfigureCSV)');
	});

	it('should render a Salesforce data-source page', () => {
		const component = shallow(
			<Edit
				{...defaultProps}
				dataSource={new DataSource(data.mockSalesforceDataSource())}
			/>
		);

		expect(component.name()).toBe('Salesforce');
	});
});
