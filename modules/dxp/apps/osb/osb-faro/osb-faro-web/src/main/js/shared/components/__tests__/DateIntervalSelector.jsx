import DateIntervalSelector, {INTERVAL, RANGE} from '../DateIntervalSelector';
import moment from 'moment';
import React from 'react';
import {shallow} from 'enzyme';

describe('DateIntervalSelector', () => {
	it('should render', () => {
		const component = shallow(
			<DateIntervalSelector minDate={moment(0)} range={moment(0)} />
		);
		expect(component.find('Button')).toMatchSnapshot();
	});

	it('should render content in overlay', () => {
		const component = shallow(
			<DateIntervalSelector minDate={moment(0)} range={moment(0)} />
		);

		component.instance().handleToggleActive();

		jest.runAllTimers();

		expect(component).toMatchSnapshot();
	});

	it('should render button label with custom range', () => {
		const component = shallow(
			<DateIntervalSelector
				minDate={moment(0)}
				range={{
					end: moment(1000000000),
					start: moment(0)
				}}
				rangeType={RANGE.CUSTOM}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render button label with different interval', () => {
		const component = shallow(
			<DateIntervalSelector
				interval={INTERVAL.YEARLY}
				minDate={moment(0)}
				range={moment(0)}
			/>
		);
		expect(component).toMatchSnapshot();
	});

	it('should call onRangeTypeChange', () => {
		const onRangeTypeChange = jest.fn();

		const component = shallow(
			<DateIntervalSelector
				minDate={moment(0)}
				onRangeTypeChange={onRangeTypeChange}
				range={moment(0)}
			/>
		);

		expect(onRangeTypeChange).not.toHaveBeenCalled();
		component.instance().handleRangeTypeSelect({value: 0});
		expect(onRangeTypeChange).toHaveBeenCalledWith(0);
	});
});
