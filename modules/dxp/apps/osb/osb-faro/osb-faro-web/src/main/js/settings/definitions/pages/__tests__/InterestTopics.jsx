import * as API from 'shared/api';
import * as data from 'test/data';
import mockStore from 'test/mock-store';
import Promise from 'metal-promise';
import React from 'react';
import {cleanup, render} from '@testing-library/react';
import {InterestTopics} from '../InterestTopics';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router';
import {User} from 'shared/util/records';
import {UserRoleNames} from 'shared/util/constants';

jest.unmock('react-dom');

const defaultProps = {
	currentUser: new User(data.mockUser()),
	groupId: '23'
};

const mockMemberUser = new User(
	data.mockUser(0, {roleName: UserRoleNames.Member})
);

const DefaultComponent = props => (
	<Provider store={mockStore()}>
		<StaticRouter>
			<InterestTopics {...defaultProps} {...props} />
		</StaticRouter>
	</Provider>
);

describe('InterestTopics', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(<DefaultComponent />);

		expect(container).toMatchSnapshot();
	});

	it('should render a page not found if puts a invalid page', () => {
		API.blockedKeywords.search.mockReturnValueOnce(
			Promise.resolve({items: [], total: 0})
		);

		const {container} = render(<DefaultComponent page={33} />);

		jest.runAllTimers();

		expect(container.querySelector('.no-results-root')).toMatchSnapshot();
	});

	it('should render without an "add keyword" button if the user role is member', () => {
		const {queryByText} = render(
			<DefaultComponent currentUser={mockMemberUser} />
		);

		expect(queryByText('Add Keyword')).toBeNull();
	});

	it('should render with an empty state', () => {
		API.blockedKeywords.search.mockReturnValueOnce(
			Promise.resolve({items: [], total: 0})
		);

		const {container} = render(<DefaultComponent query='foo' />);

		jest.runAllTimers();

		expect(container.querySelector('.no-results-root')).toMatchSnapshot();
	});

	it('should render with a message to add keywords if there are none', () => {
		API.blockedKeywords.search.mockReturnValueOnce(
			Promise.resolve({items: [], total: 0})
		);

		const {container} = render(<DefaultComponent />);

		jest.runAllTimers();

		expect(container.querySelector('.no-results-root')).toMatchSnapshot();
	});

	it('should render with a member-specific message to add keywords if there are none', () => {
		API.blockedKeywords.search.mockReturnValueOnce(
			Promise.resolve({items: [], total: 0})
		);

		const {container} = render(
			<DefaultComponent currentUser={mockMemberUser} />
		);

		jest.runAllTimers();

		expect(container.querySelector('.no-results-root')).toMatchSnapshot();
	});
});
