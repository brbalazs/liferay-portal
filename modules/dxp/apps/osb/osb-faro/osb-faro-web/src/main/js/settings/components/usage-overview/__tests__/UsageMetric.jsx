import FaroConstants from 'shared/util/constants';
import React from 'react';
import UsageMetric from '../UsageMetric';
import {fromJS} from 'immutable';
import {mockPlan} from 'test/data';
import {Plan} from 'shared/util/records';
import {shallow} from 'enzyme';
const {subscriptionStatuses} = FaroConstants;

describe('UsageMetric', () => {
	it('should render', () => {
		const component = shallow(
			<UsageMetric
				currentPlan={new Plan(fromJS(mockPlan()))}
				metricType={'pageViews'}
				planType={'enterprise'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as a warning usage level', () => {
		const component = shallow(
			<UsageMetric
				currentPlan={
					new Plan(
						fromJS(
							mockPlan({
								pageViews: {
									count: 6500000,
									limit: 7000000,
									status: subscriptionStatuses.approaching
								}
							})
						)
					)
				}
				metricType={'pageViews'}
				planType={'enterprise'}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render as a danger usage level if metric status is 2', () => {
		const component = shallow(
			<UsageMetric
				currentPlan={
					new Plan(
						fromJS(
							mockPlan({
								pageViews: {
									count: 7500000,
									limit: 7000000,
									status: subscriptionStatuses.over
								}
							})
						)
					)
				}
				metricType={'pageViews'}
				planType={'enterprise'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
