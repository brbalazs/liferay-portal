import Modal from '../index';
import React from 'react';
import {shallow} from 'enzyme';

describe('Modal', () => {
	it('should render', () => {
		const component = shallow(<Modal />);
		expect(component).toMatchSnapshot();
	});
});
