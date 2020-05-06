import Body from '../Body';
import React from 'react';
import {shallow} from 'enzyme';

const MOCK_DATA = {
	steps: [
		{
			buttonProps: {
				label: 'btn label 1',
				symbol: 'home'
			},
			Description: () => 'Step 1 Description',
			title: 'Step 1 title'
		},
		{
			buttonProps: {
				label: 'btn label 2',
				symbol: 'home'
			},
			Description: () => 'Step 2 Description',
			title: 'Step 2 title'
		}
	],
	subtitle: 'Setup subtitle',
	title: 'Setup title'
};

describe('SummaryCardDraft Body', () => {
	it('should render component', () => {
		const component = shallow(<Body {...MOCK_DATA} current={0} />);

		expect(component.find('SummaryBaseCardTitle').length).toBe(1);
		expect(component.find('SummaryBaseCardSubtitle').length).toBe(1);
		expect(component.find('ClayMultiStep').length).toBe(1);
		expect(
			component
				.find('ClayMultiStep')
				.shallow()
				.find('ClayMultiStepItem').length
		).toBe(2);
		expect(component).toMatchSnapshot();
	});

	it('Current step should be 0 by default when there is no value', () => {
		const component = shallow(<Body {...MOCK_DATA} />);

		expect(component.find('ClayMultiStep').props().current).toBe(0);
	});
});
