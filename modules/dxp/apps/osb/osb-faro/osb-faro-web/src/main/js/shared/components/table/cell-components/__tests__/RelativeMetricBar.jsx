import React from 'react';
import RelativeMetricBar from '../RelativeMetricBar';
import {shallow} from 'enzyme';

describe('RelativeMetricBar', () => {
	it('should render', () => {
		const component = shallow(
			<RelativeMetricBar
				data={{
					count: 6,
					name: 'Test Test'
				}}
				maxCount={10}
				showTitle={false}
				totalCount={12}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render w/ name', () => {
		const component = shallow(
			<RelativeMetricBar
				data={{
					count: 6,
					name: 'Test Test'
				}}
				maxCount={10}
				showName
				totalCount={12}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
