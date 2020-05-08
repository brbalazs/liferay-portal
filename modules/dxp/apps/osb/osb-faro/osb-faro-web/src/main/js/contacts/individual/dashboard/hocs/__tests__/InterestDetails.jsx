import InterestDetails from '../InterestDetails';
import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, render} from '@testing-library/react';
import {MockedProvider} from '@apollo/react-testing';
import {mockTouchpointsReq} from 'test/graphql-data';
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
		engagementMetric: {
			__typename: 'Metric',
			value: 0.25
		},
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

const defaultProps = {
	router: {
		params: {
			channelId: '321321',
			groupId: '23',
			interestId: 'test'
		},
		query: {
			delta: '5',
			page: '1'
		}
	}
};

const DefaultComponent = props => (
	<MockedProvider mocks={[mockTouchpointsReq(mockItems)]}>
		<BrowserRouter>
			<InterestDetails {...defaultProps} {...props} />
		</BrowserRouter>
	</MockedProvider>
);

describe('InterestDetails', () => {
	afterEach(cleanup);

	it('renders', async() => {
		const {container} = render(<DefaultComponent />);

		await waitForLoading(container);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
