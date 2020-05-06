import 'test/mock-modal';

jest.mock('shared/actions/alerts', () => ({
	actionTypes: {},
	addAlert: jest.fn(() => ({meta: {}, payload: {}, type: 'addAlert'})),
	alertTypes: {}
}));

import * as API from 'shared/api';
import * as data from 'test/data';
import Promise from 'metal-promise';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {DataSource} from 'shared/util/records';
import {open} from 'shared/actions/modals';
import {range} from 'lodash';
import {Routes, toRoute} from 'shared/util/router';
import {shallow} from 'enzyme';
import {SyncContacts} from '../SyncContacts';

const pushSpy = jest.fn();
const mockHistory = {
	push: pushSpy
};

function getMockLiferayDataSource(id, config) {
	return data.getImmutableMock(
		DataSource,
		data.mockLiferayDataSource,
		id,
		config
	);
}

const defaultProps = {
	dataSource: getMockLiferayDataSource(23, {
		provider: {
			contactsConfiguration: {
				enableAllContacts: false,
				organizations: [],
				userGroups: []
			}
		}
	}),
	groupId: '23',
	id: '23'
};

describe('SyncContacts', () => {
	afterEach(() => {
		jest.clearAllMocks();
	});

	it('should render', () => {
		const component = shallow(<SyncContacts {...defaultProps} />);

		expect(component).toMatchSnapshot();
	});

	it('should render with counts selected', () => {
		API.dataSource.fetchOrganizationsById.mockReturnValueOnce(
			Promise.resolve(range(9).map(i => data.mockOrganization(i)))
		);

		API.dataSource.fetchUserGroupsById.mockReturnValueOnce(
			Promise.resolve(range(9).map(i => data.mockUserGroup(i)))
		);

		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				contactsConfiguration: {
					enableAllContacts: false,
					organizations: [data.mockOrganization(1)],
					userGroups: [data.mockUserGroup(1)]
				}
			}
		});

		const component = shallow(
			<SyncContacts
				{...defaultProps}
				dataSource={mockLiferayDataSource}
			/>
		);

		jest.runAllTimers();

		component.find('SyncItem').map(wrapper => {
			expect(
				wrapper
					.shallow()
					.find('Sticker')
					.prop('className')
			).toBe('has-selection');
		});
	});

	it('should open the sync user groups modal', () => {
		expect(open).not.toBeCalled();

		const component = shallow(
			<SyncContacts {...defaultProps} open={open} />
		);

		component.instance().handleSyncUserGroupsModal();

		jest.runAllTimers();

		expect(open).toBeCalled();
	});

	it('should open the sync organizations modal', () => {
		expect(open).not.toBeCalled();

		const component = shallow(
			<SyncContacts {...defaultProps} open={open} />
		);

		component.instance().handleSyncOrganizationsModal();

		jest.runAllTimers();

		expect(open).toBeCalled();
	});

	it('should render with the save button enabled if the user removes syncAll from a prior contactsConfiguration', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				contactsConfiguration: {
					enableAllContacts: true,
					organizations: [],
					userGroups: []
				}
			}
		});

		const component = shallow(
			<SyncContacts
				{...defaultProps}
				dataSource={mockLiferayDataSource}
			/>
		);

		component.instance().handleSyncAll();

		jest.runAllTimers();

		expect(component.find('FormNavigation').prop('enableNext')).toBe(true);
	});

	it('should render with the save button disabled if the user has not made any selections and has no prior contactsConfiguration selections', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				contactsConfiguration: {
					enableAllContacts: false,
					organizations: [],
					userGroups: []
				}
			}
		});

		const component = shallow(
			<SyncContacts
				{...defaultProps}
				dataSource={mockLiferayDataSource}
			/>
		);

		jest.runAllTimers();

		expect(component.find('FormNavigation').prop('enableNext')).toBe(false);
	});

	it('should display an alert toast message and navigate to the datasource profile if the datasource credentials are invalid', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				contactsConfiguration: {
					enableAllContacts: false,
					organizations: [data.mockOrganization(1)],
					userGroups: []
				}
			}
		});

		const errorString = JSON.stringify({status: 403});

		API.dataSource.fetchUserGroupsById.mockImplementation(() =>
			Promise.reject(new Error(errorString))
		);

		API.dataSource.fetchOrganizationsById.mockImplementation(() =>
			Promise.reject(new Error(errorString))
		);

		shallow(
			<SyncContacts
				{...defaultProps}
				addAlert={addAlert}
				dataSource={mockLiferayDataSource}
				history={mockHistory}
			/>
		);

		jest.runAllTimers();

		expect(addAlert).toBeCalled();

		expect(pushSpy).toHaveBeenCalledTimes(1);
	});

	it('should display an alert toast message and navigate to the datasource profile if a service permission error is received from fetching sync counts', () => {
		const errorString = JSON.stringify({status: 403});

		API.dataSource.fetchLiferaySyncCounts.mockImplementationOnce(() =>
			Promise.reject(new Error(errorString))
		);

		shallow(
			<SyncContacts
				{...defaultProps}
				addAlert={addAlert}
				history={mockHistory}
			/>
		);

		jest.runAllTimers();

		expect(addAlert).toBeCalled();

		expect(pushSpy).toHaveBeenCalledTimes(1);
	});

	it('should display an alert toast message and navigate to the datasource profile if a service unresponsive error is received from fetching sync counts', () => {
		const errorString = JSON.stringify({status: 404});

		API.dataSource.fetchLiferaySyncCounts.mockImplementationOnce(() =>
			Promise.reject(new Error(errorString))
		);

		shallow(
			<SyncContacts
				{...defaultProps}
				addAlert={addAlert}
				history={mockHistory}
			/>
		);

		jest.runAllTimers();

		expect(addAlert).toBeCalled();

		expect(pushSpy).toHaveBeenCalledTimes(1);
	});

	it('should render a loading spinner before fetching selected userGroups & organizations is complete', () => {
		const component = shallow(<SyncContacts {...defaultProps} />);

		expect(component.find('Spinner').length).toBe(1);
	});

	it('should render an error display if fetching userGroups/organizations failed', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				contactsConfiguration: {
					enableAllContacts: false,
					organizations: [],
					userGroups: [data.mockUserGroup(1)]
				}
			}
		});

		API.dataSource.fetchOrganizationsById.mockImplementation(() =>
			Promise.reject(new Error('Request Error'))
		);

		API.dataSource.fetchUserGroupsById.mockImplementation(() =>
			Promise.reject(new Error('Request Error'))
		);

		const component = shallow(
			<SyncContacts
				{...defaultProps}
				dataSource={mockLiferayDataSource}
			/>
		);

		jest.runAllTimers();

		expect(component.find('ErrorDisplay').length).toBe(1);
	});

	it('should route the user back to the Configure DataSource tab page if they remove all contacts and save the changes', () => {
		API.dataSource.updateLiferay.mockReturnValueOnce(
			Promise.resolve(
				getMockLiferayDataSource(23, {
					provider: {
						contactsConfiguration: {
							enableAllContacts: false,
							organizations: [],
							userGroups: []
						}
					}
				})
			)
		);

		const mockIdParams = {
			groupId: '23',
			id: '23'
		};

		const expectedRoute = toRoute(
			Routes.SETTINGS_LIFERAY_CONFIGURATION_STATUS,
			mockIdParams
		);

		const component = shallow(
			<SyncContacts
				{...defaultProps}
				history={mockHistory}
				updateLiferayDataSource={jest.fn(() => Promise.resolve({}))}
			/>
		);

		component.setState({
			selectedOrganizations: [],
			selectedUserGroups: [],
			syncAll: false
		});

		jest.runAllTimers();

		component.instance().handleUpdateLiferay();

		jest.runAllTimers();

		expect(pushSpy).toHaveBeenCalledWith(expectedRoute);
	});

	it('should render with Sync All contacts count even if contactsConfiguration has no configuration', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				contactsConfiguration: {
					enableAllContacts: false,
					organizations: [],
					userGroups: []
				}
			}
		});

		const component = shallow(
			<SyncContacts
				{...defaultProps}
				dataSource={mockLiferayDataSource}
			/>
		);

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render with "Syncing x fewer contacts" verbiage if totalUsersCount is less than currentUsersCount', () => {
		API.dataSource.fetchLiferaySyncCounts.mockReturnValueOnce(
			Promise.resolve({
				allUsersCount: 1015,
				currentUsersCount: 1015,
				organizationsUsersCount: 0,
				totalUsersCount: 0,
				userGroupsUsersCount: 0
			})
		);

		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				contactsConfiguration: {
					enableAllContacts: false,
					organizations: [],
					userGroups: []
				}
			}
		});

		const component = shallow(
			<SyncContacts
				{...defaultProps}
				dataSource={mockLiferayDataSource}
			/>
		);

		jest.runAllTimers();

		expect(component.find('.total-sync-info')).toMatchSnapshot();
	});
});
