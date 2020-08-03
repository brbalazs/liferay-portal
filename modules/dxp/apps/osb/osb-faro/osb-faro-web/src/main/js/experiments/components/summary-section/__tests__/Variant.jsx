import React from 'react';
import Variant from '../Variant';
import {shallow} from 'enzyme';

describe('SummarySection Variant', () => {
	it('should render component', () => {
		const component = shallow(<Variant lift='50%' status='up' />);

		expect(
			component.hasClass('analytics-summary-section-variant')
		).toBeTruthy();
		expect(component.find('ClayIcon').props().symbol).toEqual('caret-top');
		expect(component).toMatchSnapshot();
	});

	it('should render component with status down', () => {
		const component = shallow(<Variant lift='50%' status='down' />);

		expect(component.find('ClayIcon').props().symbol).toEqual(
			'caret-bottom'
		);
	});
});
