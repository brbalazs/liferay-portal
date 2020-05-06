import ListGroup from '../index';
import React from 'react';
import {shallow} from 'enzyme';

describe('ListGroup', () => {
	it('should render', () => {
		const component = shallow(<ListGroup />);
		expect(component).toMatchSnapshot();
	});
});
