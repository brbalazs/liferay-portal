import Interests from '../Interests';
import React from 'react';
import {shallow} from 'enzyme';

describe('Sites Dashboard Interests', () => {
	it('render', () => {
		const component = shallow(<Interests />);

		expect(component).toMatchSnapshot();
	});
});
