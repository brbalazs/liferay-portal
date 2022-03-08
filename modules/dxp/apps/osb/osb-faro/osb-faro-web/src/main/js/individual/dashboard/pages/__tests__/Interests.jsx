import * as useDataSource from 'shared/hooks/useDataSource';
import Interests from '../Interests';
import mockStore from 'test/mock-store';
import React from 'react';
import {
	cleanup,
	render,
	waitForElementToBeRemoved
} from '@testing-library/react';
import {MemoryRouter, Route} from 'react-router-dom';
import {MockedProvider} from '@apollo/react-testing';
import {mockEmptyState, mockSuccessState} from 'test/__mocks__/mock-objects';
import {mockIndividualInterestsReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
import {Routes} from 'shared/util/router';

jest.unmock('react-dom');

const mockUseDataSource = useDataSource;

const WrappedComponent = () => (
	<MockedProvider
		mocks={[
			mockIndividualInterestsReq(defaultVars => ({
				...defaultVars,
				channelId: '123',
				size: 2
			}))
		]}
	>
		<Provider store={mockStore()}>
			<MemoryRouter
				initialEntries={[
					'/workspace/23/123/contacts/individuals/interests?delta=2&page=1&field=count&sortOrder=DESC'
				]}
			>
				<Route path={Routes.CONTACTS_INDIVIDUALS_INTERESTS}>
					<Interests />
				</Route>
			</MemoryRouter>
		</Provider>
	</MockedProvider>
);

describe('Individuals Dashboard Individuals Interests', () => {
	afterEach(cleanup);

	it('renders', async () => {
		mockUseDataSource.useDataSource = jest.fn(() => mockSuccessState);

		const {container} = render(<WrappedComponent />);

		await waitForElementToBeRemoved(
			container.querySelector('.spinner-root')
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});

	it('renders with data source empty state', async () => {
		mockUseDataSource.useDataSource = jest.fn(() => mockEmptyState);

		const {container} = render(<WrappedComponent />);

		expect(container).toMatchSnapshot();
	});
});
