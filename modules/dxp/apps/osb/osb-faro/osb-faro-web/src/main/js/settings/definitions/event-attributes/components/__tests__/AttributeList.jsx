import * as data from 'test/data';
import AttributeList from '../AttributeList';
import client from 'shared/apollo/client';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {AttributeTypes} from 'event-analysis/utils/types';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventAttributeDefinitionsReq} from 'test/graphql-data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

describe('AttributeList', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<StaticRouter>
				<MockedProvider
					mocks={[
						mockEventAttributeDefinitionsReq(
							[
								data.mockEventAttributeDefinition(0, {
									__typename: 'EventAttributeDefinition'
								})
							],
							{
								type: AttributeTypes.Local
							}
						)
					]}
				>
					<AttributeList delta={1} groupId='23' {...props} />
				</MockedProvider>
			</StaticRouter>
		</ApolloProvider>
	);

	it('should render', async () => {
		const {container} = render(<WrappedComponent />);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('should render Data Typecast column with a label', () => {
		const {getByText} = render(<WrappedComponent />);

		jest.runAllTimers();

		expect(getByText('STRING').parentElement).toHaveClass('label-info');
	});
});
