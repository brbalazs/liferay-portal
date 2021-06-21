import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {AttributeFilterSection} from '../AttributeFilterSection';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('AttributeFilterSection', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<AttributeFilterSection
					attributes={[]}
					filterOrder={[]}
					filters={[]}
					{...props}
				/>
			</Provider>
		</ApolloProvider>
	);

	it('renders', () => {
		const {container} = render(<WrappedComponent />);

		expect(container.querySelector('.add-attribute')).toBeNull();
		expect(container).toMatchSnapshot();
	});

	it('renders w/ add attribute button', () => {
		const {container} = render(<WrappedComponent eventId='1' />);

		expect(container.querySelector('.add-attribute')).toBeTruthy();
	});

	it('renders w/ filter', () => {
		const {container} = render(
			<WrappedComponent
				attributes={{
					123123: {
						dataType: 'STRING',
						displayName: 'Job Title',
						id: '123123',
						name: 'jobTitle'
					}
				}}
				eventId='1'
				filterOrder={['123123']}
				filters={{
					123123: {
						attributeId: '123123',
						dataType: 'STRING',
						operator: 'eq',
						type: 'event',
						value: ['Stuff']
					}
				}}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
