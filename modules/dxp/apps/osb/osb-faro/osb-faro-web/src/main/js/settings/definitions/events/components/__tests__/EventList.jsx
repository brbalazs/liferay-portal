import * as data from 'test/data';
import client from 'shared/apollo/client';
import EventList from '../EventList';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventDefinitionsReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

describe('EventList', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<StaticRouter>
					<MockedProvider
						mocks={[
							mockEventDefinitionsReq([
								data.mockEventDefinition(0, {
									__typename: 'EventDefinition'
								})
							])
						]}
					>
						<EventList delta={1} groupId='23' {...props} />
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
