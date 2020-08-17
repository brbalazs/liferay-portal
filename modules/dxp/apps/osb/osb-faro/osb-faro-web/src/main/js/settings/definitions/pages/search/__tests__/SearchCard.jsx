import mockStore from 'test/mock-store';
import React from 'react';
import SearchCard from '../SearchCard';
import {MockedProvider} from '@apollo/react-testing';
import {mockSearchStringListReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

jest.useRealTimers();

describe('SearchCard', () => {
	it('should render', async() => {
		const {container} = render(
			<StaticRouter>
				<MockedProvider mocks={[mockSearchStringListReq()]}>
					<Provider store={mockStore()}>
						<SearchCard groupId='23' />
					</Provider>
				</MockedProvider>
			</StaticRouter>
		);

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});
});
