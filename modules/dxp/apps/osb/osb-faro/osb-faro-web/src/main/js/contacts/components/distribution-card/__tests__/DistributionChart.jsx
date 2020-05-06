import DistributionChart from '../DistributionChart';
import FaroConstants from 'shared/util/constants';
import mockStore from 'test/mock-store';
import Promise from 'metal-promise';
import React from 'react';
import {BrowserRouter} from 'react-router-dom';
import {cleanup, render} from '@testing-library/react';
import {DistributionTab} from 'shared/util/records';
import {Provider} from 'react-redux';

const {
	fieldContexts: {demographics}
} = FaroConstants;

jest.unmock('react-dom');

describe('DistributionCard DistributionChart', () => {
	afterEach(cleanup);

	it('renders', () => {
		const {container} = render(
			<Provider store={mockStore()}>
				<BrowserRouter>
					<DistributionChart
						distributionKey='individualsDashboard'
						fetchDistribution={() => Promise.reject()}
						selectedTab={
							new DistributionTab({
								context: demographics,
								propertyType: 'number',
								title: 'Tab 1'
							})
						}
						viewAllLink='test/:id'
					/>
				</BrowserRouter>
			</Provider>
		);

		expect(container).toMatchSnapshot();
	});
});
