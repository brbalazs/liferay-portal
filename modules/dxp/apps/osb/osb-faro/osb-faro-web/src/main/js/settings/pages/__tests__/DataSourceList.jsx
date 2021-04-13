import * as API from 'shared/api';
import * as data from 'test/data';
import mockStore from 'test/mock-store';
import Promise from 'metal-promise';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {
	DataSourceList,
	DataSourceName,
	disableRow,
	StatusRenderer
} from '../DataSourceList';
import {DataSourceStates} from 'shared/util/constants';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';
import {User} from 'shared/util/records';

jest.unmock('react-dom');

const defaultProps = {
	currentUser: new User(data.mockUser()),
	groupId: '23'
};

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<DataSourceList {...defaultProps} {...props} />
		</StaticRouter>
	</Provider>
);

const mockMemberUser = data.getImmutableMock(User, data.mockMemberUser);

describe('DataSourceList', () => {
	afterEach(() => {
		jest.runAllTimers();

		API.dataSource.search.mockClear();

		cleanup();
	});

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render without an "add data source" button if the user role is member', () => {
		const {queryByText} = render(
			<DefaultComponent currentUser={mockMemberUser} />
		);

		jest.runAllTimers();

		expect(queryByText('Add Data Source')).toBeNull();
	});

	it('should render with an empty state', () => {
		API.dataSource.search.mockReturnValueOnce(
			Promise.resolve({items: [], total: 0})
		);

		const {container} = render(<DefaultComponent query='foo' />);

		jest.runAllTimers();

		expect(container.querySelector('.no-results-root')).toMatchSnapshot();
	});

	it('should render with a message to connect datasources if there are none', () => {
		API.dataSource.search.mockReturnValueOnce(
			Promise.resolve({items: [], total: 0})
		);

		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container.querySelector('.no-results-root')).toMatchSnapshot();
	});

	it('should render with a member-specific message to connect datasources if there are none', () => {
		API.dataSource.search.mockReturnValueOnce(
			Promise.resolve({items: [], total: 0})
		);

		const {container} = render(
			<DefaultComponent currentUser={mockMemberUser} />
		);

		jest.runAllTimers();

		expect(container.querySelector('.no-results-root')).toMatchSnapshot();
	});

	it('should render toast for one data source with invalid credentials', () => {
		API.dataSource.search.mockReturnValue(
			Promise.resolve({
				items: [
					data.mockLiferayDataSource(1, {
						credentials: {
							oAuthOwner: {emailAddress: 'test@liferay.com'}
						},
						state: DataSourceStates.CredentialsInvalid
					})
				],
				total: 1
			})
		);

		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(
			container.querySelectorAll('.embedded-alert-list-root')[1]
		).toMatchSnapshot();
	});

	it("should render toast for one data source with invalid credentials for a member's view", () => {
		API.dataSource.search.mockReturnValue(
			Promise.resolve({
				items: [
					data.mockLiferayDataSource(1, {
						credentials: {
							oAuthOwner: {emailAddress: 'test@liferay.com'}
						},
						state: DataSourceStates.credentialsInvalid
					})
				],
				total: 1
			})
		);

		const {container} = render(
			<DefaultComponent currentUser={mockMemberUser} />
		);

		jest.runAllTimers();

		expect(
			container.querySelectorAll('.embedded-alert-list-root')[1]
		).toMatchSnapshot();
	});

	it('should render toast for multiple data sources with invalid credentials', () => {
		API.dataSource.search.mockReturnValue(
			Promise.resolve({
				items: [
					data.mockLiferayDataSource(1, {
						credentials: {
							oAuthOwner: {emailAddress: 'test@liferay.com'}
						},
						state: DataSourceStates.CredentialsInvalid
					})
				],
				total: 2
			})
		);

		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(
			container.querySelectorAll('.embedded-alert-list-root')[1]
		).toMatchSnapshot();
	});

	it("should render toast for multiple data sources with invalid credentials for a member's view", () => {
		API.dataSource.search.mockReturnValue(
			Promise.resolve({
				items: [
					data.mockLiferayDataSource(1, {
						credentials: {
							oAuthOwner: {emailAddress: 'test@liferay.com'}
						},
						state: DataSourceStates.CredentialsInvalid
					})
				],
				total: 2
			})
		);

		const {container} = render(
			<DefaultComponent currentUser={mockMemberUser} />
		);

		jest.runAllTimers();

		expect(
			container.querySelectorAll('.embedded-alert-list-root')[1]
		).toMatchSnapshot();
	});
});

describe('CellRenderers', () => {
	afterEach(cleanup);

	it('should show data-source as not configured', () => {
		const {getByText} = render(<StatusRenderer data={{state: null}} />);

		expect(getByText('Not Configured')).toBeTruthy();
	});

	it('should render as disabled if the datasource is in the process of being deleted', () => {
		const {container} = render(
			<DataSourceName
				data={{state: DataSourceStates.InProgressDeleting}}
			/>
		);

		expect(container.querySelector('a')).toBeNull();
	});
});

describe('disableRow', () => {
	it('should return true if datasource state is inProgressDeleting', () => {
		expect(disableRow({state: DataSourceStates.InProgressDeleting})).toBe(
			true
		);
	});

	it('should return false if datasource state is NOT inProgressDeleting', () => {
		expect(disableRow({state: DataSourceStates.Ready})).toBe(false);
	});
});
