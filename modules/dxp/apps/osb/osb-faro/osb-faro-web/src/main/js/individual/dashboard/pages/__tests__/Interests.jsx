import Interests from '../Interests';
import React from 'react';
import {MemoryRouter, Route} from 'react-router-dom';
import {MockedProvider} from '@apollo/react-testing';
import {mockIndividualInterestsReq} from 'test/graphql-data';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {Routes} from 'shared/util/router';

jest.unmock('react-dom');

describe('Individuals Dashboard Individuals Interests', () => {
	it('renders', async () => {
		const {container} = render(
			<MockedProvider
				mocks={[
					mockIndividualInterestsReq(defaultVars => ({
						...defaultVars,
						channelId: '123',
						size: 2
					}))
				]}
			>
				<MemoryRouter
					initialEntries={[
						'/workspace/23/123/contacts/individuals/interests?delta=2&page=1&field=count&sortOrder=DESC'
					]}
				>
					<Route path={Routes.CONTACTS_INDIVIDUALS_INTERESTS}>
						<Interests />
					</Route>
				</MemoryRouter>
			</MockedProvider>
		);

		await waitForElementToBeRemoved(
			container.querySelector('.spinner-root')
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
