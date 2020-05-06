import AddReport from '../AddReport';
import React from 'react';
import {shallow} from 'enzyme';

describe('AddReport', () => {
	it('should render without analytics-add-report-empty-dashboard class', () => {
		const component = shallow(<AddReport />);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render with analytics-add-report-empty-dashboard class', () => {
		const component = shallow(<AddReport isEmptyDashboard />);

		expect(component.render()).toMatchSnapshot();
	});

	it('should render a form AddReport', () => {
		const component = shallow(<AddReport />);

		component.instance().openReport();

		expect(component.render()).toMatchSnapshot();
	});

	it('should render an empty state when closeReport method is called', () => {
		const component = shallow(<AddReport />);

		component.instance().closeReport();

		expect(component.render()).toMatchSnapshot();
	});

	it('should be false when some form field has not filled in', () => {
		const component = shallow(<AddReport />);

		component.setState({
			report: {
				chartType: 'line',
				metric: 'viewsMetric',
				title: null
			}
		});

		component.instance().enableButtonSave();

		expect(component.state('isEnableToSave')).toBe(false);
	});

	it('should be true when all form field has filled in', () => {
		const component = shallow(<AddReport />);

		component.setState({
			report: {
				chartType: 'line',
				metric: 'viewsMetric',
				title: 'My title'
			}
		});

		component.instance().enableButtonSave();

		expect(component.state('isEnableToSave')).toBe(true);
	});

	it('should return a report with the title when the handleChangeReportTitle is called', () => {
		const component = shallow(<AddReport />);

		component
			.instance()
			.handleChangeReportTitle({target: {value: 'My title 2'}});

		expect(component.state('report')).toEqual({
			chartType: '',
			metric: '',
			title: 'My title 2'
		});
	});

	it('should return a report with the title when the handleChangeSelectMetric is called', () => {
		const component = shallow(<AddReport />);

		component
			.instance()
			.handleChangeSelectMetric({target: {value: 'viewsMetric'}});

		expect(component.state('report')).toEqual({
			chartType: '',
			metric: 'viewsMetric',
			title: ''
		});
	});

	it('should return a report with the title when the getSelectedChartType is called', () => {
		const component = shallow(<AddReport />);

		component.instance().handleGetSelectedChartType({value: 'line'});

		expect(component.state('report')).toEqual({
			chartType: 'line',
			metric: '',
			title: ''
		});
	});

	it('should open form when handleClickAddReport is called', () => {
		const component = shallow(<AddReport />);

		component.instance().handleClickAddReport();

		expect(component.state('showFormAddReport')).toBeTruthy();
	});

	it('should close form when handleClickSaveReport is called', () => {
		const component = shallow(<AddReport onGetReport={jest.fn()} />);

		component.instance().handleClickSaveReport();

		expect(component.state('showFormAddReport')).toBeFalsy();
	});

	it('should close form when handleClickCancelReport is called', () => {
		const component = shallow(<AddReport onGetReport={jest.fn()} />);

		component.instance().handleClickCancelReport();

		expect(component.state('showFormAddReport')).toBeFalsy();
	});
});
