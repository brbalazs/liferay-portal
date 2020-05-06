import React from 'react';
import StepBody from '../StepBody';
import {shallow} from 'enzyme';

const MOCK_STEP = {
	buttonProps: {
		label: 'button label',
		symbol: 'home'
	},
	Description: () => 'StepBody description',
	modal: [
		{
			title: 'modal 01'
		}
	],
	subtitle: 'StepBody subtitle',
	title: 'StepBody title'
};

describe('SummaryCardDraft StepBody', () => {
	it('should render component', () => {
		const component = shallow(
			<StepBody status='wait' step={MOCK_STEP} />
		).find('Card');

		expect(
			component.hasClass('analytics-summary-card-step-content')
		).toBeTruthy();
		expect(component).toMatchSnapshot();
	});

	it('should render "wait card" when status is wait', () => {
		const component = shallow(
			<StepBody status='wait' step={MOCK_STEP} />
		).find('Card');

		expect(
			component.hasClass('analytics-summary-card-step-content-wait')
		).toBeTruthy();
	});

	it('should render "active card" when status is active', () => {
		const component = shallow(
			<StepBody status='active' step={MOCK_STEP} />
		).find('Card');

		expect(
			component.hasClass('analytics-summary-card-step-content-active')
		).toBeTruthy();
	});
});
