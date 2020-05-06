import PlanBreakdown from '../PlanBreakdown';
import React from 'react';
import {mockAddOns} from 'test/data';
import {shallow} from 'enzyme';

describe('PlanBreakdown', () => {
	it('should render', () => {
		const component = shallow(<PlanBreakdown addOns={mockAddOns()} />);
		expect(component).toMatchSnapshot();
	});
});
