import BasePage from 'shared/components/base-page';
import client from 'shared/apollo/client';
import Overview from '../Overview';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {ChannelContext} from 'shared/context/channel';
import {cleanup, render} from '@testing-library/react';
import {mockChannelContext} from 'test/mock-channel-context';
import {StaticRouter} from 'react-router-dom';

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

describe('Sites Dashboard Overview', () => {
	afterEach(cleanup);

	it('render', () => {
		const {container} = render(
			<ApolloProvider client={client}>
				<StaticRouter>
					<ChannelContext.Provider value={mockChannelContext()}>
						<BasePage.Context.Provider value={MOCK_CONTEXT}>
							<Overview
								channelName='Test Channel'
								router={{
									params: {channelId: '456', groupId: '123'}
								}}
							/>
						</BasePage.Context.Provider>
					</ChannelContext.Provider>
				</StaticRouter>
			</ApolloProvider>
		);

		expect(container).toMatchSnapshot();
	});
});
