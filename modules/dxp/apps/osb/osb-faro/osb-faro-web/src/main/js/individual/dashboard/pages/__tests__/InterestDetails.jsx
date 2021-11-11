import InterestDetails from '../InterestDetails';
import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, render} from '@testing-library/react';
import {MockedProvider} from '@apollo/react-testing';
import {mockTimeRangeReq, mockTouchpointsReq} from 'test/graphql-data';
import {waitForLoading} from 'test/helpers';

jest.unmock('react-dom');

const mockItems = [
	{
		__typename: 'PageMetric',
		assetId: 'https://www.liferay.com',
		assetTitle: 'Dashboard - Retail',
		avgTimeOnPageMetric: {
			__typename: 'Metric',
			value: 23
		},
		bounceRateMetric: {
			__typename: 'Metric',
			value: 0.23
		},
		dataSourceId: '123123',
		entrancesMetric: {
			__typename: 'Metric',
			value: 56
		},
		exitRateMetric: {
			__typename: 'Metric',
			value: 0.53
		},
		viewsMetric: {__typename: 'Metric', value: 243.0},
		visitorsMetric: {
			__typename: 'Metric',
			value: 45.0
		}
	}
];

describe('Individuals Dashboard Individuals Interest Details', () => {
	afterEach(cleanup);

	it('renders', async () => {
		const {container} = render(
			<MockedProvider
				mocks={[mockTouchpointsReq(mockItems), mockTimeRangeReq()]}
			>
				<BrowserRouter>
					<InterestDetails
						router={{
							params: {
								channelId: '321321',
								groupId: '23',
								interestId: 'test'
							},
							query: {
								delta: '5',
								page: '1',
								rangeEnd: null,
								rangeKey: '30',
								rangeStart: null
							}
						}}
					/>
				</BrowserRouter>
			</MockedProvider>
		);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
