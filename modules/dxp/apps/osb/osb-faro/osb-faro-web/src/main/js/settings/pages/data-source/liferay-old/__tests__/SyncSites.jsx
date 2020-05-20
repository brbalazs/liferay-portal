jest.mock('shared/actions/alerts', () => ({
	actionTypes: {},
	addAlert: jest.fn(() => ({meta: {}, payload: {}, type: 'addAlert'})),
	alertTypes: {}
}));

import * as API from 'shared/api';
import * as data from 'test/data';
import mockStore from 'test/mock-store';
import Promise from 'metal-promise';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {cleanup, fireEvent, render} from '@testing-library/react';
import {DataSource, User} from 'shared/util/records';
import {Provider} from 'react-redux';
import {SelectionProvider} from 'shared/context/selection';
import {StaticRouter} from 'react-router';
import {SyncSites} from '../SyncSites';

jest.unmock('react-dom');

const pushSpy = jest.fn();
const mockHistory = {
	push: pushSpy
};

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<SelectionProvider>
				<SyncSites {...props} />
			</SelectionProvider>
		</StaticRouter>
	</Provider>
);

function getMockLiferayDataSource(id = 0, config = {}) {
	return data.getImmutableMock(
		DataSource,
		data.mockLiferayDataSource,
		id,
		config
	);
}

const defaultProps = {
	currentUser: data.getImmutableMock(User, data.mockUser),
	dataSource: getMockLiferayDataSource(),
	groupId: '23',
	id: '23'
};

describe('SyncSites', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent {...defaultProps} />);

		expect(container).toMatchSnapshot();
	});

	it('should render with preselected items', () => {
		API.dataSource.fetchSites.mockReturnValueOnce(
			Promise.resolve(data.mockSearch(data.mockSite, 2))
		);

		API.dataSource.fetchSitesById.mockReturnValueOnce(
			Promise.resolve([data.mockSite(1)])
		);

		const {container} = render(
			<DefaultComponent
				{...defaultProps}
				dataSource={getMockLiferayDataSource(23, {
					provider: {
						analyticsConfiguration: {
							sites: [data.mockSite(1)]
						},
						contactsConfiguration: null,
						type: 'LIFERAY'
					}
				})}
			/>
		);

		jest.runAllTimers();

		expect(
			container.querySelector(
				'.table > tbody:nth-of-type(2) > tr .custom-checkbox input'
			).checked
		).toBe(true);
	});

	it('should render with the configure button enabled after deselecting preselected items', () => {
		API.dataSource.fetchSites.mockReturnValueOnce(
			Promise.resolve(data.mockSearch(data.mockSite, 2))
		);

		API.dataSource.fetchSitesById.mockReturnValueOnce(
			Promise.resolve([data.mockSite(1)])
		);

		API.dataSource.fetch.mockReturnValueOnce(
			Promise.resolve(
				data.mockLiferayDataSource(23, {
					provider: {
						analyticsConfiguration: {
							sites: [data.mockSite(1)]
						},
						contactsConfiguration: null,
						type: 'LIFERAY'
					}
				})
			)
		);

		const {container} = render(
			<DefaultComponent
				{...defaultProps}
				dataSource={getMockLiferayDataSource(23, {
					provider: {
						analyticsConfiguration: {
							sites: [data.mockSite(1)]
						},
						contactsConfiguration: null,
						type: 'LIFERAY'
					}
				})}
			/>
		);

		jest.runAllTimers();

		const preselectedCheckbox = container.querySelector(
			'.table > tbody:nth-of-type(2) > tr .custom-checkbox input'
		);

		expect(preselectedCheckbox.checked).toBe(true);

		fireEvent.click(preselectedCheckbox);

		expect(preselectedCheckbox.checked).toBe(false);

		jest.runAllTimers();

		expect(
			container.querySelector('.form-navigation-root .btn-primary')
				.disabled
		).toBe(false);
	});

	it('should display an alert toast message and navigate to the datasource profile if the credentials were revoked', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				analyticsConfiguration: {
					sites: [data.mockSite(1)]
				}
			}
		});

		const errorString = JSON.stringify({status: 403});

		API.dataSource.fetchSitesById.mockImplementation(() =>
			Promise.reject(new Error(errorString))
		);

		render(
			<DefaultComponent
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

	it('should display an alert toast message and navigate to the datasource profile if the data source is unresponsive', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				analyticsConfiguration: {
					sites: [data.mockSite(1)]
				}
			}
		});

		const errorString = JSON.stringify({status: 404});

		API.dataSource.fetchSitesById.mockImplementation(() =>
			Promise.reject(new Error(errorString))
		);

		render(
			<DefaultComponent
				{...defaultProps}
				addAlert={addAlert}
				dataSource={mockLiferayDataSource}
				history={mockHistory}
			/>
		);

		jest.runAllTimers();

		expect(addAlert).toBeCalled();

		expect(pushSpy).toHaveBeenCalled();
	});

	it('should render an error display if fetching sites had a server error', () => {
		const mockLiferayDataSource = getMockLiferayDataSource(23, {
			provider: {
				analyticsConfiguration: {
					sites: [data.mockSite(1)]
				}
			}
		});

		API.dataSource.fetchSitesById.mockImplementation(() =>
			Promise.reject(new Error('Request Error'))
		);

		const {container} = render(
			<DefaultComponent
				{...defaultProps}
				dataSource={mockLiferayDataSource}
			/>
		);

		jest.runAllTimers();

		expect(container.querySelector('.error-display-root'))
			.toBeInTheDocument;
	});
});
