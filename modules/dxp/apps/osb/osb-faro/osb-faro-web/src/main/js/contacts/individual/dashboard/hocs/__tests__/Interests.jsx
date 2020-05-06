import Interests from '../Interests';
import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, render} from '@testing-library/react';
import {MockedProvider} from '@apollo/react-testing';
import {mockIndividualInterestsReq} from 'test/graphql-data';

jest.unmock('react-dom');

describe('Interests', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<MockedProvider mocks={[mockIndividualInterestsReq()]}>
				<BrowserRouter>
					<Interests
						router={{
							params: {groupId: '123'},
							query: {delta: '5', page: '1'}
						}}
					/>
				</BrowserRouter>
			</MockedProvider>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
