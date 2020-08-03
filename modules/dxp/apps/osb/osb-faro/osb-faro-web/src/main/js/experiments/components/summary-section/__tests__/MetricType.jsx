import MetricType from '../MetricType';
import React from 'react';
import {shallow} from 'enzyme';

describe('SummarySection MetricType', () => {
	it('should render component', () => {
		const component = shallow(<MetricType value='Click rate' />);

		expect(
			component.hasClass('analytics-summary-section-metric-type')
		).toBeTruthy();
		expect(component.find('ClayIcon').props().symbol).toEqual(
			'web-content'
		);
		expect(component).toMatchSnapshot();
	});
});
