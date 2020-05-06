import Heading from '../Heading';
import React from 'react';
import {shallow} from 'enzyme';

describe('SummarySection Heading', () => {
	it('should render component', () => {
		const component = shallow(<Heading value={100} />);

		expect(
			component.hasClass('analytics-summary-section-heading')
		).toBeTruthy();
		expect(component).toMatchSnapshot();
	});
});
