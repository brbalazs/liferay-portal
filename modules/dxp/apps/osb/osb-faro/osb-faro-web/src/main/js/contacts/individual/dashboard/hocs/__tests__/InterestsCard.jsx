import InterestsCard from '../InterestsCard';
import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, render} from '@testing-library/react';
import {MockedProvider} from '@apollo/react-testing';
import {mockIndividualInterestsReq} from 'test/graphql-data';
import {omit} from 'lodash';

jest.unmock('react-dom');

describe('InterestsCard', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<MockedProvider
				mocks={[
					mockIndividualInterestsReq(variables =>
						omit(variables, 'keywords')
					)
				]}
			>
				<BrowserRouter>
					<InterestsCard groupId='123' />
				</BrowserRouter>
			</MockedProvider>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
