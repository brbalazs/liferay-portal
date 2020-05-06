import React from 'react';
import Subtitle from '../Subtitle';
import {shallow} from 'enzyme';

describe('SummaryBaseCard Subtitle', () => {
	it('should render component', () => {
		const component = shallow(<Subtitle label='My Subtitle' />);

		expect(component.length).toBe(1);
		expect(
			component.hasClass('font-size-sm-1x mb-2 text-uppercase')
		).toBeTruthy();
		expect(component.render()).toMatchSnapshot();
	});
});
