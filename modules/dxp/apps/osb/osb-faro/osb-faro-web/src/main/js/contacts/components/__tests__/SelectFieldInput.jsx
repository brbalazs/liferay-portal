import React from 'react';
import SelectFieldInput from '../SelectFieldInput';
import {shallow} from 'enzyme';

describe('SelectFieldInput', () => {
	it('should render', () => {
		const component = shallow(<SelectFieldInput groupId={'23'} />);

		expect(component).toMatchSnapshot();
	});
});
