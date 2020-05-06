import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {cleanup, render} from '@testing-library/react';
import {Provider} from 'react-redux';
import {StaticRouter} from 'react-router-dom';
import {SuppressedUsers} from '../SuppressedUsers';

jest.unmock('react-dom');

describe('SuppressedUsers', () => {
	afterEach(cleanup);

	it('should render', () => {
		const {container} = render(
			<ApolloProvider client={client}>
				<Provider store={mockStore()}>
					<StaticRouter>
						<SuppressedUsers
							router={{params: {groupId: '23'}, query: {}}}
						/>
					</StaticRouter>
				</Provider>
			</ApolloProvider>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
