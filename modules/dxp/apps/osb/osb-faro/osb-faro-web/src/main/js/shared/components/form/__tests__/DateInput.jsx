import DateInput from '../DateInput';
import React from 'react';
import {mockForm} from 'test/data';
import {shallow} from 'enzyme';

describe('DateInput', () => {
	it('should render', () => {
		const component = shallow(
			<DateInput field={{name: 'foo'}} form={mockForm()} />
		);
		expect(component).toMatchSnapshot();
	});
});
