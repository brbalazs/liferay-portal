import ErrorPage from '../ErrorPage';
import React from 'react';
import {shallow} from 'enzyme';

describe('ErrorPage', () => {
	it('should render', () => {
		const component = shallow(<ErrorPage />);
		expect(component).toMatchSnapshot();
	});

	it('should render a custom message', () => {
		const component = shallow(<ErrorPage message='foo bar' />);
		expect(component).toMatchSnapshot();
	});
});
