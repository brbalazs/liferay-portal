import DateInput from '../DateInput';
import moment from 'moment';
import React from 'react';
import {shallow} from 'enzyme';

describe('DateInput', () => {
	it('should render', () => {
		const component = shallow(<DateInput />);
		expect(component.render()).toMatchSnapshot();
	});

	it('should call onChange', () => {
		const onChange = jest.fn();
		const component = shallow(<DateInput onChange={onChange} />);
		expect(onChange).not.toHaveBeenCalled();
		component.instance().handleDateSelect(moment(0));
		expect(onChange).toHaveBeenCalled();
	});

	it('should use the displayFormat prop for displaying the date', () => {
		const onChange = jest.fn();
		const displayFormat = 'YYYY MM DD HH:mm';
		const updatedDate = '2019-06-13';

		const component = shallow(
			<DateInput
				displayFormat={displayFormat}
				onChange={onChange}
				value='1970-01-01'
			/>
		);

		expect(component.find('MaskedInput').props().value).toEqual(
			'1970 01 01 00:00'
		);

		component
			.find('MaskedInput')
			.props()
			.onChange({target: {value: updatedDate}});

		expect(onChange).toHaveBeenCalledWith(updatedDate);
	});
});
