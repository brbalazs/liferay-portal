import client from 'shared/apollo/client';
import EventAnalysisEdit from '../EventAnalysisEdit';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {render, waitForElementToBeRemoved} from '@testing-library/react';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '456',
		groupId: '123',
		id: '2'
	})
}));

describe('EventAnalysisEdit', () => {
	it('should render', async () => {
		const {container} = render(
			<BrowserRouter>
				<ApolloProvider client={client}>
					<EventAnalysisEdit />
				</ApolloProvider>
			</BrowserRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});
});
