import PlansList from '../PlansList';
import React from 'react';
import {shallow} from 'enzyme';

describe('PlansList', () => {
	it('should render', () => {
		const component = shallow(<PlansList />);
		expect(component).toMatchSnapshot();
	});

	it('should render with a label in the list of plans for the current plan', () => {
		const mockPlans = [
			{
				baseSubscriptionPlan: null,
				limits: {
					individuals: 0,
					pageViews: 0
				},
				name: 'Liferay Analytics Cloud Business',
				price: 0
			}
		];

		const component = shallow(
			<PlansList
				currentPlanName={'Liferay Analytics Cloud Business'}
				plans={mockPlans}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
