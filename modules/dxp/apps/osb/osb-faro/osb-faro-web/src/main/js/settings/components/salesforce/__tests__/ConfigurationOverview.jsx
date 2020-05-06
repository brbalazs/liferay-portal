jest.mock('shared/actions/alerts', () => ({
	actionTypes: {},
	addAlert: jest.fn(() => ({meta: {}, payload: {}, type: 'addAlert'})),
	alertTypes: {}
}));

import * as data from 'test/data';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {DataSource} from 'shared/util/records';
import {ConfigurationOverview as SalesforceConfigurationOverview} from '../ConfigurationOverview';
import {shallow} from 'enzyme';

const pushSpy = jest.fn();

const mockHistory = {
	push: pushSpy
};

const defaultProps = {
	addAlert,
	dataSource: data.getImmutableMock(
		DataSource,
		data.mockSalesforceDataSource
	),
	groupId: '23',
	history: mockHistory,
	id: 'test'
};

describe('SalesforceConfigurationOverview', () => {
	it('should render', () => {
		const component = shallow(
			<SalesforceConfigurationOverview {...defaultProps} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as disabled', () => {
		const component = shallow(
			<SalesforceConfigurationOverview {...defaultProps} disabled />
		);

		expect(component.shallow()).toMatchSnapshot();
	});

	it('should display an alert toast message and navigate to the datasource profile if a service permission error is received from fetching sync counts', () => {
		const errorString = JSON.stringify({status: 403});
		const mockError = new Error(errorString);

		const component = shallow(
			<SalesforceConfigurationOverview {...defaultProps} />
		);

		jest.runAllTimers();

		component.setProps({pollingError: mockError});

		jest.runAllTimers();

		expect(addAlert).toBeCalled();
		expect(pushSpy).toHaveBeenCalled();
	});

	it('should display an alert toast message and navigate to the datasource profile if a service unresponsive error is received from fetching sync counts', () => {
		const errorString = JSON.stringify({status: 404});
		const mockError = new Error(errorString);

		const component = shallow(
			<SalesforceConfigurationOverview {...defaultProps} />
		);

		jest.runAllTimers();

		component.setProps({pollingError: mockError});

		jest.runAllTimers();

		expect(addAlert).toBeCalled();

		expect(pushSpy).toHaveBeenCalled();
	});
});
