import Body from '../Body';
import React from 'react';
import {shallow} from 'enzyme';

describe('Modal Body', () => {
	it('should render', () => {
		const component = shallow(<Body />);
		expect(component).toMatchSnapshot();
	});
});
