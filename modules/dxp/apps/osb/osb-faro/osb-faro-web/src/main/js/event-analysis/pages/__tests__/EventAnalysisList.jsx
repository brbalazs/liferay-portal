import client from 'shared/apollo/client';
import EventAnalysisList from '../EventAnalysisList';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {Provider} from 'react-redux';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {StaticRouter} from 'react-router-dom';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '456',
		groupId: '123'
	})
}));

const WrappedComponent = () => (
	<ApolloProvider client={client}>
		<Provider store={mockStore()}>
			<StaticRouter>
				<EventAnalysisList />
			</StaticRouter>
		</Provider>
	</ApolloProvider>
);

describe('Event Analysis', () => {
	it('should render', async () => {
		const {container} = render(<WrappedComponent />);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});

	it('should render the button of Create Analysis', () => {
		const {getByText} = render(<WrappedComponent />);

		expect(getByText('Create Analysis')).toBeInTheDocument();
	});
});
