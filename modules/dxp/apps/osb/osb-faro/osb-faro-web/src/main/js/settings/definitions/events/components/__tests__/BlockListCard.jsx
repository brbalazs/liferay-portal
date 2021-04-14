import * as data from 'test/data';
import BlockListCard from '../BlockListCard';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {mockBlockedCustomEventDefinitionsReq} from 'test/graphql-data';
import {MockedProvider} from '@apollo/react-testing';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

describe('BlockListCard', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<StaticRouter>
					<MockedProvider
						mocks={[
							mockBlockedCustomEventDefinitionsReq([
								data.mockBlockedCustomEventDefinition(0)
							])
						]}
					>
						<BlockListCard delta={1} groupId='23' {...props} />
					</MockedProvider>
				</StaticRouter>
			</Provider>
		</ApolloProvider>
	);

	it('should render', async () => {
		const {container} = render(<WrappedComponent />);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
