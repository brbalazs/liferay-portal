import AttributeChip from '../AttributeChip';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';
import {wrapInTestContext} from 'react-dnd-test-utils';

jest.unmock('react-dom');

describe('AttributeChip', () => {
	const AttributeChipContext = wrapInTestContext(AttributeChip);

	it('render', () => {
		const {container} = render(
			<ApolloProvider client={client}>
				<Provider store={mockStore()}>
					<AttributeChipContext
						attribute={{
							dataType: 'string',
							displayName: 'Article View',
							id: '0',
							name: 'articleView'
						}}
						breakdown={{
							attributeId: '0',
							dataType: 'string',
							type: 'event'
						}}
						filter={{
							attributeId: '0',
							operator: 'eq',
							value: ['Stuff']
						}}
						index={1}
					/>
				</Provider>
			</ApolloProvider>
		);

		expect(container).toMatchSnapshot();
	});
});
