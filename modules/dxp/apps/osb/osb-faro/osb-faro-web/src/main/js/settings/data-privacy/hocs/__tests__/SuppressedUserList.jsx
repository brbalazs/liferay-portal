import Constants, {GDPR_REQUEST_STATUSES} from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import SuppressedUserList from '../SuppressedUserList';
import SuppressedUsersListQuery from '../../queries/SuppressedUsersListQuery';
import {cleanup, render} from '@testing-library/react';
import {CREATE_DATE} from 'shared/util/pagination';
import {mockBag} from 'test/graphql-data';
import {MockedProvider} from '@apollo/react-testing';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router-dom';
import {waitForTable} from 'test/helpers';

const {
	pagination: {orderDescending}
} = Constants;

jest.unmock('react-dom');

const mockItems = [
	{
		createDate: '2019-09-10T00:00',
		dataControlTaskBatchId: '00001',
		dataControlTaskCreateDate: '2019-09-09T00:00',
		dataControlTaskStatus: GDPR_REQUEST_STATUSES.PENDING,
		emailAddress: 'foo@email',
		id: '12345'
	},
	{
		createDate: '2019-09-11T00:00',
		dataControlTaskBatchId: '00002',
		dataControlTaskCreateDate: '2019-09-09T00:00',
		dataControlTaskStatus: GDPR_REQUEST_STATUSES.COMPLETED,
		emailAddress: 'bar@email',
		id: '6789'
	}
];

function mockSuppressedUsersListReq() {
	return {
		request: {
			query: SuppressedUsersListQuery,
			variables: {
				keywords: '',
				size: 2,
				sort: {
					column: CREATE_DATE,
					type: orderDescending.toUpperCase()
				},
				start: 0
			}
		},
		result: {
			data: mockBag({
				items: mockItems,
				itemTypeName: 'Suppression',
				name: 'suppressions',
				typeName: 'SuppressionBag'
			})
		}
	};
}

const WrappedComponent = props => (
	<MockedProvider mocks={[mockSuppressedUsersListReq()]}>
		<Provider store={mockStore()}>
			<StaticRouter>
				<SuppressedUserList
					router={{
						params: {groupId: '23'},
						query: {delta: '2', page: '1'}
					}}
					{...props}
				/>
			</StaticRouter>
		</Provider>
	</MockedProvider>
);

describe('Suppressed User List', () => {
	afterEach(cleanup);

	it('should render', async() => {
		const {container} = render(<WrappedComponent />);

		jest.runAllTimers();

		await waitForTable(container);

		expect(container).toMatchSnapshot();
	});
});
