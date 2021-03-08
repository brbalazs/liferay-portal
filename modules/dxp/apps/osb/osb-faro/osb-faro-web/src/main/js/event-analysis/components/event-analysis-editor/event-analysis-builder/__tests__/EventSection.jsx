import client from 'shared/apollo/client';
import EventSection from '../EventSection';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {Provider} from 'react-redux';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('EventSection', () => {
	const WrappedComponent = props => (
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<EventSection {...props} />
			</Provider>
		</ApolloProvider>
	);

	it('render', () => {
		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});

	it('render with event', () => {
		const {container} = render(
			<WrappedComponent event={{name: 'View Article'}} />
		);

		expect(container).toMatchSnapshot();
	});
});
