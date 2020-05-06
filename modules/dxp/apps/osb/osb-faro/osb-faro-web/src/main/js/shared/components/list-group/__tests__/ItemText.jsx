import ItemText from '../ItemText';
import React from 'react';
import {shallow} from 'enzyme';

describe('ItemText', () => {
	it('should render', () => {
		const component = shallow(<ItemText />);
		expect(component).toMatchSnapshot();
	});
});
