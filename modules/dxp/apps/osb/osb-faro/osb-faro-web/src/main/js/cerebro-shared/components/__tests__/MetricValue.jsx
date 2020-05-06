import MetricValue from '../MetricValue';
import React from 'react';
import {shallow} from 'enzyme';

describe('MetricValue', () => {
	it('should render the component', () => {
		const component = shallow(<MetricValue value='100K' />);

		expect(component).toMatchSnapshot();
	});

	it('should render the component with number type', () => {
		const component = shallow(<MetricValue type='number' value='100K' />);

		expect(component).toMatchSnapshot();
	});

	it('should render the component with percentage ', () => {
		const component = shallow(
			<MetricValue type='percentage' value='100%' />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render the component with time ', () => {
		const component = shallow(<MetricValue type='time' value='12m 40s' />);

		expect(component).toMatchSnapshot();
	});

	it('should render the component with engagement ', () => {
		const component = shallow(
			<MetricValue type='engagement' value='5/10' />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render the component with ratings ', () => {
		const component = shallow(<MetricValue type='ratings' value='10/10' />);

		expect(component).toMatchSnapshot();
	});
});
