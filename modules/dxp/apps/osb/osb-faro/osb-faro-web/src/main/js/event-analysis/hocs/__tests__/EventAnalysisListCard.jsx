import client from 'shared/apollo/client';
import EventAnalysisListCard from '../EventAnalysisListCard';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {
	fireEvent,
	render,
	waitForElementToBeRemoved
} from '@testing-library/react';
import {Provider} from 'react-redux';
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
	<Provider store={mockStore()}>
		<ApolloProvider client={client}>
			<StaticRouter>
				<EventAnalysisListCard />
			</StaticRouter>
		</ApolloProvider>
	</Provider>
);

describe('EventAnalysisListCard', () => {
	it('should render', async () => {
		const {container} = render(<WrappedComponent />);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});

	it('should render a trash icon when item is checked', () => {
		const {container, getByTestId} = render(<WrappedComponent />);

		expect(
			container.getElementsByClassName('lexicon-icon-trash')
		).toHaveLength(3);

		const selectAllCheckbox = getByTestId('select-all-checkbox');

		jest.runAllTimers();

		fireEvent.click(selectAllCheckbox);

		jest.runAllTimers();

		expect(
			container.getElementsByClassName('lexicon-icon-trash')
		).toHaveLength(1);

		expect(
			container.getElementsByClassName('lexicon-icon-trash')[0].firstChild
		).toHaveAttribute(
			'xlink:href',
			'/o/osb-faro-web/dist/sprite.svg#trash'
		);
	});
});
