import DatePicker from '../index';
import mockCurrentDate from 'test/mock-date';
import moment from 'moment';
import React from 'react';
import {shallow} from 'enzyme';

describe('DatePicker', () => {
	let currentDate;

	beforeEach(() => {
		currentDate = mockCurrentDate();
	});

	afterEach(() => {
		currentDate.mockReset();
		currentDate.mockRestore();
	});

	it('should render', () => {
		const component = shallow(
			<DatePicker date={moment(0)} minDate={moment(0)} />
		);
		jest.runAllTimers();
		expect(component).toMatchSnapshot();
	});

	it('should render the next month', () => {
		const component = shallow(
			<DatePicker date={moment(0)} minDate={moment(0)} />
		);
		jest.runAllTimers();
		component.instance().handleNextMonth();
		jest.runAllTimers();
		expect(component).toMatchSnapshot();
	});

	it('should call onSelect when a date is selected', () => {
		const onSelect = jest.fn();
		const component = shallow(
			<DatePicker
				date={moment(0)}
				minDate={moment(0)}
				onSelect={onSelect}
			/>
		);
		expect(onSelect).not.toHaveBeenCalled();
		component.instance().handleSelect();
		expect(onSelect).toHaveBeenCalled();
	});

	it('should render label when a range is passed', () => {
		const component = shallow(
			<DatePicker
				date={{
					end: null,
					start: moment(0)
				}}
				minDate={moment(0)}
			/>
		);

		jest.runAllTimers();
		expect(component).toMatchSnapshot();
	});
});
