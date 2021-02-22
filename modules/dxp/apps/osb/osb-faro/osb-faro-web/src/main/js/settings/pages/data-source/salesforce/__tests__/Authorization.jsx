import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import {DataSource, User} from 'shared/util/records';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {SalesforceAuthorization} from '../Authorization';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

const {
	userRoleNames: {member}
} = FaroConstants;

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<SalesforceAuthorization
				currentUser={data.getImmutableMock(User, data.mockUser)}
				dataSource={data.getImmutableMock(
					DataSource,
					data.mockLiferayDataSource
				)}
				groupId='23'
				id='test'
				{...props}
			/>
		</StaticRouter>
	</Provider>
);

describe('SalesforceAuthorization', () => {
	it('should render', () => {
		const {container, queryByText} = render(<DefaultComponent />);

		expect(queryByText('Delete Data Source')).toBeTruthy();
		expect(queryByText('Edit')).toBeTruthy();
		expect(container).toMatchSnapshot();
	});

	it('should render as read-only if the user is not authorized to make changes', () => {
		const {queryByText} = render(
			<DefaultComponent
				currentUser={data.getImmutableMock(User, data.mockUser, '24', {
					roleName: member
				})}
			/>
		);

		expect(queryByText('Delete Data Source')).toBeNull();
		expect(queryByText('Edit')).toBeNull();
	});
});
