import client from 'shared/apollo/client';
import EventAnalysisBuilder from '../index';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('Event Analysis Builder', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<EventAnalysisBuilder {...props} />
			</Provider>
		</ApolloProvider>
	);

	it('render', () => {
		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});

	it('render with filters & breakdowns', () => {
		const {container} = render(
			<WrappedComponent
				attributes={[
					{
						id: '321321',
						name: 'Article Title'
					},
					{
						id: '123123',
						name: 'Job Title'
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
				event={{
					id: '123123',
					name: 'Article Views',
					type: 'custom'
				}}
				filters={[
					{
						attributeId: '123123',
						operator: 'eq',
						value: ['Stuff']
					}
				]}
				onEventChange={jest.fn()}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
