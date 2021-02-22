import * as API from 'shared/api';
import * as data from 'test/data';
import BaseListPage from '../BaseListPage';
import FaroConstants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import Promise from 'metal-promise';
import React from 'react';
import {ChannelContext} from 'shared/context/channel';
import {cleanup, render, waitForElement} from '@testing-library/react';
import {mockChannelContext} from 'test/mock-channel-context';
import {noop, times} from 'lodash';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';
import {User} from 'shared/util/records';

const {userRoleNames} = FaroConstants;

const MEMBER_USER = new User(
	data.mockUser(24, {roleName: userRoleNames.member})
);

const TOTAL = 5;

const ACCOUNTS = times(TOTAL, i => data.mockAccount(i));

const USER = new User(data.mockUser());

const defaultProps = {
	channelId: '123123',
	columns: [
		{
			accessor: 'name',
			label: 'Name'
		},
		{
			accessor: 'id',
			label: 'Id'
		}
	],
	currentUser: USER,
	dataSourceFn: jest.fn(() =>
		Promise.resolve({items: ACCOUNTS, total: TOTAL})
	),
	entityLabel: 'Accounts',
	groupId: '23'
};

const WrappedComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<ChannelContext.Provider value={mockChannelContext()}>
				<BaseListPage {...defaultProps} {...props} />
			</ChannelContext.Provider>
		</StaticRouter>
	</Provider>
);

jest.unmock('react-dom');
jest.useRealTimers();

describe('BaseListPage', () => {
	afterEach(cleanup);

	it('should render', async () => {
		const {container} = render(<WrappedComponent />);

		await waitForElement(() => container.querySelector('tbody'));

		expect(container).toMatchSnapshot();
	});

	it('should render with an empty query state', async () => {
		const {findByText} = render(
			<WrappedComponent
				dataSourceFn={jest.fn(() =>
					Promise.resolve({items: [], total: 0})
				)}
				query='non-existent datasource'
			/>
		);

		const noResult = await waitForElement(() =>
			findByText('non-existent datasource')
		);

		expect(noResult.parentNode).toMatchSnapshot();
	});

	it('should render with a no results display if there are no results and active filters', async () => {
		const {findByText} = render(
			<WrappedComponent
				dataSourceFn={jest.fn(() =>
					Promise.resolve({items: [], total: 0})
				)}
				query='non-existent datasource'
			/>
		);

		const noResult = await waitForElement(() =>
			findByText('There are no Accounts found.')
		);

		expect(noResult).toBeInTheDocument();
	});

	it('should render with a message to connect datasources', async () => {
		API.dataSource.search.mockReturnValueOnce(
			Promise.resolve(data.mockSearch(noop, 0))
		);

		const {container, getByText} = render(
			<WrappedComponent
				dataSourceFn={jest.fn(() =>
					Promise.resolve({items: [], total: 0})
				)}
			/>
		);

		await waitForElement(() => getByText('No Data Sources Connected'));

		expect(container).toMatchSnapshot();
	});

	it('should render with a member-specific message to connect datasources', async () => {
		API.dataSource.search.mockReturnValueOnce(
			Promise.resolve(data.mockSearch(noop, 0))
		);

		const {container, getByText} = render(
			<WrappedComponent
				currentUser={MEMBER_USER}
				dataSourceFn={jest.fn(() =>
					Promise.resolve({items: [], total: 0})
				)}
			/>
		);

		await waitForElement(() => getByText('No Data Sources Connected'));

		expect(container).toMatchSnapshot();
	});

	it('should render with an embedded alert', async () => {
		API.dataSource.search.mockReturnValueOnce(
			Promise.resolve(data.mockSearch(noop, 0))
		);

		const {getByRole} = render(
			<WrappedComponent alerts={[{message: 'foo alert'}]} />
		);

		const alertElement = await waitForElement(() => getByRole('alert'));

		expect(alertElement).toMatchSnapshot();
	});
});
