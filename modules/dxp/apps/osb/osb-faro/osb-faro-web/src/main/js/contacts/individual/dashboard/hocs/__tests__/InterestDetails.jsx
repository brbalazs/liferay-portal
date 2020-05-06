import client from 'shared/apollo/client';
import InterestDetails from '../InterestDetails';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, render} from '@testing-library/react';

jest.unmock('react-dom');

describe('InterestDetails', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<ApolloProvider client={client}>
				<BrowserRouter>
					<InterestDetails
						router={{
							params: {groupId: '123', interestId: 'test'},
							query: {delta: '5', page: '1'}
						}}
					/>
				</BrowserRouter>
			</ApolloProvider>
		);
		expect(container).toMatchSnapshot();
	});
});
