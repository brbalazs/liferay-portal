import client from 'shared/apollo/client';
import EventAnalysisCreate from '../EventAnalysisCreate';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {Provider} from 'react-redux';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '456',
		groupId: '123'
	})
}));

describe('Event Analysis Create', () => {
	it('should render', async () => {
		const {container} = render(
			<StaticRouter>
				<ApolloProvider client={client}>
					<Provider store={mockStore()}>
						<EventAnalysisCreate />
					</Provider>
				</ApolloProvider>
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});
});
