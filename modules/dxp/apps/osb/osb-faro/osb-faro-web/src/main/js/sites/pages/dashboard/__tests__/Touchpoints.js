import React from 'react';
import Touchpoints from '../Touchpoints';
import {shallow} from 'enzyme';

describe('Sites Dashboard Touchpoints Page', () => {
	it('render', () => {
		const component = shallow(<Touchpoints />);

		expect(component).toMatchSnapshot();
	});
});
