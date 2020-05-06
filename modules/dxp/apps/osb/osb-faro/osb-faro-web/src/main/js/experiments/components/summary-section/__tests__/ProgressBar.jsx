import ProgressBar from '../ProgressBar';
import React from 'react';
import {shallow} from 'enzyme';

describe('SummarySection ProgressBar', () => {
	it('should render component', () => {
		const component = shallow(<ProgressBar value={50} />);

		expect(
			component.hasClass('analytics-summary-section-progress')
		).toBeTruthy();
		expect(component).toMatchSnapshot();
	});

	it('should render component with completed progress', () => {
		const component = shallow(<ProgressBar value={100} />);

		expect(component.hasClass('complete')).toBeTruthy();
	});
});
