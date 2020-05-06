import React from 'react';
import SummaryCardRun from '../index';
import {shallow} from 'enzyme';

const MOCK_ALERT = {
	description: 'Alert description',
	symbol: 'home',
	title: 'Alert title'
};

const MOCK_DATA = {
	header: {
		cardModals: [
			{
				buttonTitle: 'terminate test',
				message: 'content to terminate test',
				modalType: 'CONFIRMATION_MODAL',
				onSubmit: () => {},
				submitMessage: 'terminate',
				title: 'dropdown terminate test'
			},
			{
				buttonTitle: 'other test',
				message: 'content to other test',
				modalType: 'CONFIRMATION_MODAL',
				onSubmit: () => {},
				submitMessage: 'other',
				title: 'dropdown other test'
			}
		],
		Description: () => 'Header description',
		title: 'Header title'
	},
	modals: [
		{
			buttonTitle: 'run test',
			message: 'content to run test',
			modalType: 'CONFIRMATION_MODAL',
			onSubmit: () => {},
			submitMessage: 'run',
			title: 'dropdown run test'
		},
		{
			buttonTitle: 'delete test',
			message: 'content to delete test',
			modalType: 'CONFIRMATION_MODAL',
			onSubmit: () => {},
			submitMessage: 'delete',
			title: 'dropdown delete test'
		}
	],
	summary: {
		description: 'Summary description',
		subtitle: 'Summary subtitle',
		title: 'Summary title'
	}
};

const MOCK_SECTIONS = [
	{
		Body: () => <div>{'Section 01'}</div>
	},
	{
		Body: () => <div>{'Section 02'}</div>
	},
	{
		Body: () => <div>{'Section 03'}</div>
	}
];

describe('SummaryCardRun', () => {
	it('should render component', () => {
		const component = shallow(<SummaryCardRun {...MOCK_DATA} />);

		expect(component.find('SummaryBaseCardHeader').length).toBe(1);
		expect(component.find('Header').length).toBe(1);
		expect(component.find('Body').length).toBe(1);
		expect(component).toMatchSnapshot();
	});

	it('should render component with status "running"', () => {
		const component = shallow(
			<SummaryCardRun {...MOCK_DATA} status='running' />
		);

		expect(component.props().status).toEqual('running');
	});

	it('should render component with status "terminated"', () => {
		const component = shallow(
			<SummaryCardRun {...MOCK_DATA} status='terminated' />
		);

		expect(component.props().status).toEqual('terminated');
	});

	it('should render component with status "finished"', () => {
		const component = shallow(
			<SummaryCardRun {...MOCK_DATA} status='finished' />
		);

		expect(component.props().status).toEqual('finished');
	});

	it('should render component with status "completed"', () => {
		const component = shallow(
			<SummaryCardRun {...MOCK_DATA} status='completed' />
		);

		expect(component.props().status).toEqual('completed');
	});
});

describe('SummaryCardRun Paragraph', () => {
	it('should render component', () => {
		const component = shallow(<SummaryCardRun {...MOCK_DATA} />);

		expect(component.find('SummaryCardRunParagraph').length).toBe(1);
		expect(
			component.find('SummaryCardRunParagraph').render()
		).toMatchSnapshot();
	});
});

describe('SummaryCardRun Alert', () => {
	it('should render component with Alert', () => {
		const component = shallow(
			<SummaryCardRun {...MOCK_DATA} alert={MOCK_ALERT} />
		);

		expect(
			component
				.find('SummaryBaseCardAlert')
				.shallow()
				.find('SummaryBaseCardTitle').length
		).toBe(1);
		expect(
			component
				.find('SummaryBaseCardAlert')
				.shallow()
				.find('strong').length
		).toBe(1);
		expect(component.find('SummaryBaseCardAlert')).toMatchSnapshot();
	});
});

describe('SummaryCardRun Sections', () => {
	it('should render component with Alert', () => {
		const component = shallow(
			<SummaryCardRun {...MOCK_DATA} sections={MOCK_SECTIONS} />
		);

		expect(
			component
				.find('.analytics-summary-card-sections')
				.shallow()
				.find('Body').length
		).toBe(3);
		expect(
			component.find('.analytics-summary-card-sections')
		).toMatchSnapshot();
	});
});
