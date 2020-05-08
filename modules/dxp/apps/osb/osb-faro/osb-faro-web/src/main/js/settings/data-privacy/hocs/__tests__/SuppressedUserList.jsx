import mockStore from 'test/mock-store';
import React from 'react';
import SuppressedUserList from '../SuppressedUserList';
import {cleanup, render} from '@testing-library/react';
import {GDPR_REQUEST_STATUSES} from 'shared/util/constants';
import {MockedProvider} from '@apollo/react-testing';
import {mockSuppressedUsersListReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router-dom';
import {waitForLoading} from 'test/helpers';

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

const WrappedComponent = props => (
	<MockedProvider mocks={[mockSuppressedUsersListReq(mockItems)]}>
		<Provider store={mockStore()}>
			<StaticRouter>
				<SuppressedUserList
					router={{
						params: {groupId: '23'},
						query: {delta: '5', page: '1'}
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

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
