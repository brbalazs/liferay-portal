import client from 'shared/apollo/client';
import InterestDetails from '../InterestDetails';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Individuals Dashboard Individuals Interest Details', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<ApolloProvider client={client}>
				<BrowserRouter>
					<InterestDetails
						router={{
							params: {groupId: '123'},
							query: {}
						}}
					/>
				</BrowserRouter>
			</ApolloProvider>
		);

		expect(container).toMatchSnapshot();
	});
});
