jest.mock('shared/actions/alerts', () => ({
	actionTypes: {},
	addAlert: jest.fn(() => ({meta: {}, payload: {}, type: 'addAlert'})),
	alertTypes: {}
}));

import * as data from 'test/data';
import * as Router from 'shared/util/router';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {DataSource} from 'shared/util/records';
import {ConfigurationOverview as LiferayConfigurationOverview} from '../ConfigurationOverview';
import {Routes, toRoute} from 'shared/util/router';
import {shallow} from 'enzyme';

Router.navigate = jest.fn();

const {dataSourceStatuses, dataSourceTypes} = FaroConstants;

const defaultProps = {
	dataSource: data.getImmutableMock(DataSource, data.mockLiferayDataSource),
	groupId: '23',
	id: 'test'
};

describe('LiferayConfigurationOverview', () => {
	it('should render', () => {
		const component = shallow(
			<LiferayConfigurationOverview {...defaultProps} />
		);

		expect(component.shallow()).toMatchSnapshot();
	});

	it('should render as disabled', () => {
		const component = shallow(
			<LiferayConfigurationOverview {...defaultProps} disabled />
		);

		expect(component.shallow()).toMatchSnapshot();
	});

	it('should render with contacts synced', () => {
		jest.useFakeTimers();

		const mockDataSource = data.getImmutableMock(
			DataSource,
			data.mockLiferayDataSource,
			'test',
			{
				provider: {
					contactsConfiguration: {},
					status: dataSourceStatuses.active,
					type: 'LIFERAY'
				}
			}
		);

		const component = shallow(
			<LiferayConfigurationOverview
				{...defaultProps}
				dataSource={mockDataSource}
				progress={data.mockProgress()}
			/>
		);

		jest.runAllTimers();

		expect(
			component
				.shallow()
				.find('ConfigurationItem')
				.first()
				.shallow()
		).toMatchSnapshot();
	});

	it('should display an alert toast message and navigate to the datasource profile if a service permission error is received from fetching sync counts', () => {
		const errorString = JSON.stringify({status: 403});
		const mockError = new Error(errorString);
		const pushSpy = jest.fn();
		const expectedRoute = toRoute(Routes.SETTINGS_DATA_SOURCE, {
			groupId: '23',
			id: 'test'
		});

		const component = shallow(
			<LiferayConfigurationOverview
				{...defaultProps}
				addAlert={addAlert}
				history={{push: pushSpy}}
			/>
		);

		jest.runAllTimers();

		component.setProps({pollingError: mockError});

		jest.runAllTimers();

		expect(addAlert).toBeCalled();
		expect(pushSpy).toBeCalledWith(expectedRoute);
	});

	it('should display an alert toast message and navigate to the datasource profile if a service unresponsive error is received from fetching sync counts', () => {
		const errorString = JSON.stringify({status: 404});
		const mockError = new Error(errorString);
		const pushSpy = jest.fn();
		const expectedRoute = toRoute(Routes.SETTINGS_DATA_SOURCE, {
			groupId: '23',
			id: 'test'
		});

		const component = shallow(
			<LiferayConfigurationOverview
				{...defaultProps}
				addAlert={addAlert}
				history={{push: pushSpy}}
			/>
		);

		jest.runAllTimers();

		component.setProps({pollingError: mockError});

		jest.runAllTimers();

		expect(addAlert).toBeCalled();

		expect(pushSpy).toBeCalledWith(expectedRoute);
	});

	it('should render already configured items with the button text "edit" instead of "configure"', () => {
		const component = shallow(
			<LiferayConfigurationOverview
				{...defaultProps}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource,
					0,
					{
						provider: {
							analyticsConfiguration: {sites: ['1']},
							contactsConfiguration: {enableAllContacts: true},
							type: dataSourceTypes.liferay
						}
					}
				)}
			/>
		);

		jest.runAllTimers();

		expect(component.shallow()).toMatchSnapshot();
	});
});
