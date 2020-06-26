import PlansList from '../PlansList';
import React from 'react';
import {render} from '@testing-library/react';

jest.unmock('react-dom');

describe('PlansList', () => {
	
	it('should render', () => {
		const {container} = render(<PlansList />);

		expect(container).toMatchSnapshot();
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

		const {container} = render(
			<PlansList
				currentPlanName={'Liferay Analytics Cloud Business'}
				plans={mockPlans}
			/>
		);

		expect(container).toMatchSnapshot();
	});
});
