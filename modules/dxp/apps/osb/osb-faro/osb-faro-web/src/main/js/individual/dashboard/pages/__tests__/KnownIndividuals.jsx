import KnownIndividuals from '../KnownIndividuals';
import mockStore from 'test/mock-store';
import React from 'react';
import {MemoryRouter, Route} from 'react-router-dom';
import {Provider} from 'react-redux';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {Routes} from 'shared/util/router';

jest.unmock('react-dom');

describe('Individuals Dashboard KnownIndividuals List', () => {
	it('renders', async () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<MemoryRouter
					initialEntries={[
						'/workspace/23/321321/contacts/individuals/known-individuals'
					]}
				>
					<Route path={Routes.CONTACTS_INDIVIDUALS_KNOWN_INDIVIDUALS}>
						<KnownIndividuals channelId='321321' groupId='23' />
					</Route>
				</MemoryRouter>
			</Provider>
		);

		await waitForElementToBeRemoved(
			container.querySelector('.spinner-root')
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
