import FormNavigation from '../FormNavigation';
import React from 'react';
import {shallow} from 'enzyme';

describe('FormNavigation', () => {
	it('should render', () => {
		const component = shallow(<FormNavigation cancelHref='' />);

		expect(component).toMatchSnapshot();
	});
});
