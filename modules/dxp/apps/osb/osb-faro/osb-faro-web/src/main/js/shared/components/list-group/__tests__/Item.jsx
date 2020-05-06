import Item from '../Item';
import React from 'react';
import {shallow} from 'enzyme';

describe('Item', () => {
	it('should render', () => {
		const component = shallow(<Item />);
		expect(component).toMatchSnapshot();
	});

	it('should render as active', () => {
		const component = shallow(<Item active />);
		expect(component).toMatchSnapshot();
	});
});
