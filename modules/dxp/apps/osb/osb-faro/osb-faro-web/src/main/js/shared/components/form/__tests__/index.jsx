import Form from '../index';
import React from 'react';
import {shallow} from 'enzyme';

describe('Form', () => {
	it('should render a Form', () => {
		const component = shallow(<Form />);
		expect(component).toMatchSnapshot();
	});

	it('should render a Form Group', () => {
		const component = shallow(<Form.Group />);
		expect(component).toMatchSnapshot();
	});

	it('should render an inline Form Group', () => {
		const component = shallow(<Form.Group inline />);
		expect(component).toMatchSnapshot();
	});

	it('should render an autofit Form Group', () => {
		const component = shallow(<Form.Group autoFit />);
		expect(component).toMatchSnapshot();
	});
});
