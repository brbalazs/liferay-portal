import * as data from 'test/data';
import mockStore from 'test/mock-store';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';
import {User} from 'shared/util/records';
import {UserList} from '../UserList';
import {UserRoleNames} from 'shared/util/constants';

jest.unmock('react-dom');

const defaultProps = {
	currentUser: new User(data.mockUser()),
	groupId: '23'
};

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<UserList {...defaultProps} {...props} />
		</StaticRouter>
	</Provider>
);

describe('UserList', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it("should render rows as disabled without row actions, invite members button, or checkboxes if the current user's role is member", () => {
		const {container, queryByTestId, queryByText} = render(
			<DefaultComponent
				currentUser={
					new User(data.mockUser(0, {roleName: UserRoleNames.Member}))
				}
			/>
		);

		jest.runAllTimers();

		expect(
			container.querySelector(
				'.table > tbody:nth-of-type(1) > tr.disabled'
			)
		).not.toBeNull();

		expect(queryByTestId('select-all-checkbox')).not.toBeInTheDocument();

		expect(queryByText('Invite Users')).not.toBeInTheDocument();
	});
});
