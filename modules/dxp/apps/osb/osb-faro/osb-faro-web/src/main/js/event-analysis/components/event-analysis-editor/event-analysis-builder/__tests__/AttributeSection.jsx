import AttributeSection from '../AttributeSection';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AttributeSection', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<AttributeSection
					attributes={[]}
					breakdowns={[]}
					filters={[]}
					{...props}
				/>
			</Provider>
		</ApolloProvider>
	);

	it('render', () => {
		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});

	it('render with breakdown & filter', () => {
		const {container} = render(
			<WrappedComponent
				attributes={[
					{
						displayName: 'Article Title',
						id: '321321',
						name: 'articleTitle'
					},
					{
						displayName: 'Job Title',
						id: '123123',
						name: 'jobTitle'
					}
				]}
				breakdowns={[
					{
						attributeId: '321321',
						dataType: 'string',
						type: 'event'
					},
					{
						attributeId: '123123',
						dataType: 'string',
						type: 'event'
					}
				]}
				filters={[
					{
						attributeId: '123123',
						operator: 'eq',
						value: ['Stuff']
					}
				]}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
