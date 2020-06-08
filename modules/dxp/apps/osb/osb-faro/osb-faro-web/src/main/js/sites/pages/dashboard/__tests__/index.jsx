import * as data from 'test/data';
import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import FaroConstants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {ChannelContext} from 'shared/context/channel';
import {cleanup, render} from '@testing-library/react';
import {Dashboard} from '../index';
import {mockChannelContext} from 'test/mock-channel-context';
import {Provider} from 'react-redux';
import {User} from 'shared/util/records';
import {WarningStripeContext} from 'shared/context/warningStripe';

jest.unmock('react-dom');

const {
	userRoleNames: {administrator, member}
} = FaroConstants;

const ADMIN_USER = new User(data.mockUser(24, {roleName: administrator}));

const MEMBER_USER = new User(data.mockUser(23, {roleName: member}));

const MOCK_CONTEXT = {
	rangeKey: {defaultValue: '30'},
	router: {
		params: {
			channelId: '123',
			groupId: '2000'
		},
		query: {
			rangeKey: '30'
		}
	}
};

const WrappedComponent = props => (
	<ApolloProvider client={client}>
		<Provider store={mockStore()}>
			<ChannelContext.Provider value={mockChannelContext()}>
				<BasePage.Context.Provider value={MOCK_CONTEXT}>
					<BrowserRouter>
						<Dashboard
							currentUser={MEMBER_USER}
							router={MOCK_CONTEXT.router}
							{...props}
						/>
					</BrowserRouter>
				</BasePage.Context.Provider>
			</ChannelContext.Provider>
		</Provider>
	</ApolloProvider>
);

describe('Sites Dashboard Index', () => {
	afterEach(cleanup);

	beforeAll(() => {
		delete window.location;
	});

	it('renders', () => {
		window.location = {
			pathname: '/workspace/2000/123/sites'
		};

		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});

	it('renders w/ "No Sites Connected" as title', () => {
		window.location = {
			pathname: '/workspace/2000/123/sites'
		};

		const CHANNEL_CONTEXT_MOCK = {
			channelDispatch: () => {},
			channels: [],
			selectedChannel: null
		};

		const WrappedComponentWithContext = props => (
			<ApolloProvider client={client}>
				<Provider store={mockStore()}>
					<ChannelContext.Provider value={CHANNEL_CONTEXT_MOCK}>
						<BasePage.Context.Provider value={MOCK_CONTEXT}>
							<BrowserRouter>
								<Dashboard
									currentUser={MEMBER_USER}
									router={MOCK_CONTEXT.router}
									{...props}
								/>
							</BrowserRouter>
						</BasePage.Context.Provider>
					</ChannelContext.Provider>
				</Provider>
			</ApolloProvider>
		);

		const {container} = render(<WrappedComponentWithContext />);

		expect(container.querySelector('.title-section')).toHaveTextContent(
			'No Sites Connected'
		);
	});

	it('Should render a warning stripe if the user is admin and the showWarningStripe is true', () => {
		window.location = {
			pathname: '/workspace/2000/123/sites'
		};

		const WARNING_STRIPE_CONTEXT_MOCK = {
			showWarningStripe: true
		};

		const WrappedComponentWithContext = props => (
			<ApolloProvider client={client}>
				<Provider store={mockStore()}>
					<WarningStripeContext.Provider
						value={WARNING_STRIPE_CONTEXT_MOCK}
					>
						<ChannelContext.Provider value={mockChannelContext()}>
							<BasePage.Context.Provider value={MOCK_CONTEXT}>
								<BrowserRouter>
									<Dashboard
										currentUser={ADMIN_USER}
										router={MOCK_CONTEXT.router}
										{...props}
									/>
								</BrowserRouter>
							</BasePage.Context.Provider>
						</ChannelContext.Provider>
					</WarningStripeContext.Provider>
				</Provider>
			</ApolloProvider>
		);

		const {container} = render(<WrappedComponentWithContext />);

		expect(container.querySelector('.btn-warning')).toHaveTextContent(
			'Go to Datasources'
		);
	});
});
