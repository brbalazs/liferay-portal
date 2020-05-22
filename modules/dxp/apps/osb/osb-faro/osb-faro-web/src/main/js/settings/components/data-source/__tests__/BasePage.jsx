import * as data from 'test/data';
import BaseDataSourcePage from '../BasePage';
import FaroConstants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {DataSource, User} from 'shared/util/records';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

const {
	credentialTypes: {token},
	dataSourceStates: {undefinedError},
	userRoleNames: {member}
} = FaroConstants;

const mockFnLiferayDataSource = () =>
	data.mockLiferayDataSource(1, {credentials: {type: token}});

describe('BaseDataSourcePage', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<Provider store={mockStore()}>
					<BaseDataSourcePage
						currentUser={data.getImmutableMock(User, data.mockUser)}
						dataSource={data.getImmutableMock(
							DataSource,
							data.mockLiferayDataSource
						)}
						groupId='23'
						id='test'
					/>
				</Provider>
			</StaticRouter>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render a delete button if showDelete is true', () => {
		const {queryByText} = render(
			<StaticRouter>
				<Provider store={mockStore()}>
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
				</Provider>
			</StaticRouter>
		);

		expect(queryByText('Delete Data Source')).toBeTruthy();
	});

	it('should NOT render a delete button if the user is not an admin level', () => {
		const {queryByText} = render(
			<StaticRouter>
				<Provider store={mockStore()}>
					<BaseDataSourcePage
						currentUser={data.getImmutableMock(
							User,
							data.mockUser,
							'23',
							{
								roleName: member
							}
						)}
						dataSource={data.getImmutableMock(
							DataSource,
							data.mockLiferayDataSource
						)}
						groupId='23'
						id='test'
						showDelete
					/>
				</Provider>
			</StaticRouter>
		);

		expect(queryByText('Delete Data Source')).toBeNull();
	});

	it('should NOT render the Upgrade Connection Card if the credential type is token', () => {
		const {container} = render(
			<StaticRouter>
				<Provider store={mockStore()}>
					<BaseDataSourcePage
						currentUser={data.getImmutableMock(User, data.mockUser)}
						dataSource={data.getImmutableMock(
							DataSource,
							mockFnLiferayDataSource
						)}
						groupId='23'
						id='test'
					/>
				</Provider>
			</StaticRouter>
		);

		expect(container).not.toHaveTextContent('Upgrade Your Connection Type');
	});

	it('should render with an UNDEFINED_ERROR message in the datasource status column', () => {
		const {queryByText} = render(
			<StaticRouter>
				<Provider store={mockStore()}>
					<BaseDataSourcePage
						currentUser={data.getImmutableMock(
							User,
							data.mockUser,
							'23',
							{
								roleName: member
							}
						)}
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
				</Provider>
			</StaticRouter>
		);

		expect(queryByText(/A server error occurred/)).toBeTruthy();
	});

	it('should render w/o datasource', () => {
		const {queryByText} = render(
			<StaticRouter>
				<Provider store={mockStore()}>
					<BaseDataSourcePage
						currentUser={data.getImmutableMock(User, data.mockUser)}
						groupId='23'
						id='test'
					/>
				</Provider>
			</StaticRouter>
		);

		expect(
			queryByText(
				'Data source has not been created. Please authorize and save to get started.'
			)
		).toBeTruthy();
	});
});
