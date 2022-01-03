import * as Constants from 'shared/util/constants';
import BaseEventAnalysisPage from '../BaseEventAnalysisPage';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import React from 'react';
import {ApolloProvider} from '@apollo/react-components';
import {BrowserRouter} from 'react-router-dom';
import {Provider} from 'react-redux';
import {render, waitForElementToBeRemoved} from '@testing-library/react';

jest.unmock('react-dom');

jest.mock('react-router-dom', () => ({
	...jest.requireActual('react-router-dom'),
	useParams: () => ({
		channelId: '456',
		groupId: '123'
	})
}));

Constants.DEVELOPER_MODE = true;

const WrappedComponent = () => (
	<BrowserRouter>
		<ApolloProvider client={client}>
			<Provider store={mockStore()}>
				<BaseEventAnalysisPage />
			</Provider>
		</ApolloProvider>
	</BrowserRouter>
);

describe('BaseEventAnalysisPage', () => {
	it('should render', async () => {
		const {container} = render(<WrappedComponent />);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});

	it('the save analysis button must be disabled.', () => {
		const {getByText} = render(<WrappedComponent />);

		expect(getByText('Save Analysis')).toBeDisabled();
	});
});
