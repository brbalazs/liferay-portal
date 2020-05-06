import React from 'react';
import SummaryCardDraft from '../index';
import {shallow} from 'enzyme';

const MOCK_DATA = {
	header: {
		cardModals: [
			{
				title: 'action 01'
			},
			{
				title: 'action 02'
			}
		],
		Description: () => 'Header description',
		modals: [
			{
				title: 'action 01'
			},
			{
				title: 'action 02'
			}
		],
		title: 'Header title'
	},
	setup: {
		current: 0,
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
	},
	status: 'draft',
	summary: {
		description: 'Summary description',
		subtitle: 'Summary subtitle',
		title: 'Summary title'
	}
};

describe('SummaryCardDraft', () => {
	it('should render component', () => {
		const component = shallow(<SummaryCardDraft {...MOCK_DATA} />);

		expect(component.find('SummaryBaseCardHeader').length).toBe(1);
		expect(component.find('SummaryCardDraftBody').length).toBe(1);
		expect(component.find('SummaryBaseCardTitle').length).toBe(1);
		expect(component.find('SummaryBaseCardSubtitle').length).toBe(1);
		expect(component.find('Header').length).toBe(1);
		expect(component.find('Body').length).toBe(1);
		expect(component.find('Footer').length).toBe(1);
		expect(component).toMatchSnapshot();
	});
});
