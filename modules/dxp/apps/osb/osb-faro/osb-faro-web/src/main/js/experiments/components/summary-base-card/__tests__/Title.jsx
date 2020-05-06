import React from 'react';
import Title from '../Title';
import {shallow} from 'enzyme';

describe('SummaryBaseCard Title', () => {
	it('should render component', () => {
		const component = shallow(<Title label='My Title' />);

		expect(component.length).toBe(1);
		expect(component.hasClass('font-weight-bold')).toBeTruthy();
		expect(component.render()).toMatchSnapshot();
	});
});
