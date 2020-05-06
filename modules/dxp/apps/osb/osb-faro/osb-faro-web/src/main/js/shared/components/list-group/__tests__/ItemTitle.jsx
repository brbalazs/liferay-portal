import ItemTitle from '../ItemTitle';
import React from 'react';
import {shallow} from 'enzyme';

describe('ItemTitle', () => {
	it('should render', () => {
		const component = shallow(<ItemTitle />);
		expect(component).toMatchSnapshot();
	});
});
