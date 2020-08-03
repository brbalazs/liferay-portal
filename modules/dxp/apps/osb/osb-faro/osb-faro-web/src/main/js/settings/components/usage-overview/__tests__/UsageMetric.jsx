import FaroConstants from 'shared/util/constants';
import React from 'react';
import UsageMetric from '../UsageMetric';
import {fromJS} from 'immutable';
import {mockPlan} from 'test/data';
import {Plan} from 'shared/util/records';
import {render} from '@testing-library/react';

jest.unmock('react-dom');
const {subscriptionStatuses} = FaroConstants;

const DefaultComponent = ({count, limit, status}) => (
	<UsageMetric
		currentPlan={
			new Plan(
				fromJS(
					mockPlan({
						pageViews: {
							count,
							limit,
							status
						}
					})
				)
			)
		}
		metricType='pageViews'
		planType='enterprise'
	/>
);

describe('UsageMetric', () => {
	it('should render', () => {
		const {container} = render(
			<UsageMetric
				currentPlan={new Plan(fromJS(mockPlan()))}
				metricType='pageViews'
				planType='enterprise'
			/>
		);

		expect(container).toMatchSnapshot();
	});

	it('should render as a warning usage level', () => {
		const props = {
			count: 6500000,
			limit: 7000000,
			status: subscriptionStatuses.approaching
		};

		const {container} = render(<DefaultComponent {...props} />);

		expect(container.querySelector('.bar-warning')).toBeTruthy();
	});

	it('should render as a danger usage level if metric status is 2', () => {
		const props = {
			count: 7500000,
			limit: 7000000,
			status: subscriptionStatuses.over
		};

		const {container} = render(<DefaultComponent {...props} />);

		expect(container.querySelector('.bar-danger')).toBeTruthy();
	});
});
