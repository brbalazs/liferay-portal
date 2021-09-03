import * as data from 'test/data';
import client from 'shared/apollo/client';
import mockStore from 'test/mock-store';
import Overview from '../Overview';
import React from 'react';
import {Individual} from 'shared/util/records';
import {MockedProvider} from '@apollo/react-testing';
import {mockEventMetrics, mockSessions} from 'test/data';
import {mockTimeRangeReq} from 'test/graphql-data';
import {Provider} from 'react-redux';
import {render, waitForElementToBeRemoved} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

jest.mock('shared/apollo/client', () => ({
	query: jest.fn()
}));

client.query
	.mockResolvedValueOnce({
		data: mockEventMetrics()
	})
	.mockResolvedValue({
		data: mockSessions()
	});

describe('IndividualOverview', () => {
	it('should render', async () => {
		const {container} = render(
			<MockedProvider mocks={[mockTimeRangeReq()]}>
				<Provider store={mockStore()}>
					<StaticRouter>
						<Overview
							groupId='23'
							id='test'
							individual={data.getImmutableMock(
								Individual,
								data.mockIndividual
							)}
						/>
					</StaticRouter>
				</Provider>
			</MockedProvider>
		);

		jest.runAllTimers();

		await waitForElementToBeRemoved(() =>
			container.querySelector('.spinner-root')
		);

		expect(container).toMatchSnapshot();
	});
});
