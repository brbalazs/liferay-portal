import Description from '../Description';
import React from 'react';
import {shallow} from 'enzyme';

describe('SummarySection Description', () => {
	it('should render component', () => {
		const component = shallow(<Description value={100} />);

		expect(
			component.hasClass('analytics-summary-section-description')
		).toBeTruthy();
		expect(component).toMatchSnapshot();
	});
});
