import Cell from '../Cell';
import React from 'react';
import {shallow} from 'enzyme';

describe('Cell', () => {
	it('should render', () => {
		const component = shallow(<Cell />);
		expect(component).toMatchSnapshot();
	});

	it('should render as a table title', () => {
		const component = shallow(<Cell title />);
		expect(component).toMatchSnapshot();
	});
});
