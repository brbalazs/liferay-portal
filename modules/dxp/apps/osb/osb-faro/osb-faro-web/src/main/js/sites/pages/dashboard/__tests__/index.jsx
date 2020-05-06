import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {ChannelContext} from 'shared/context/channel';
import {cleanup, render} from '@testing-library/react';
import {Dashboard} from '../index';
import {mockChannelContext} from 'test/mock-channel-context';
import {Provider} from 'react-redux';

jest.unmock('react-dom');

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
						<Dashboard router={MOCK_CONTEXT.router} {...props} />
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
});
