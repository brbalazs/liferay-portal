import React from 'react';
import VariantCard from '../index';
import {shallow} from 'enzyme';
import {StateProvider} from 'experiments/state';

const data = [
	{
		changes: 30,
		confidenceLevel: 0.0,
		control: true,
		dxpVariantId: '123',
		dxpVariantName: 'DXP Variant Name',
		improvementChance: 0.0,
		improvementLift: 0.0,
		metricRangeEnd: 0.0,
		metricRangeStart: 0.0,
		probabilityToWin: 0.0,
		trafficSplit: 0.0,
		uniqueVisitors: 0
	},
	{
		changes: 5,
		confidenceLevel: 0.0,
		control: false,
		dxpVariantId: '456',
		dxpVariantName: 'Another DXP Variant Name',
		improvementChance: 0.0,
		improvementLift: 0.0,
		metricRangeEnd: 0.0,
		metricRangeStart: 0.0,
		probabilityToWin: 0.0,
		trafficSplit: 0.0,
		uniqueVisitors: 0
	}
];

describe('VariantCard', () => {
	let component;

	afterEach(() => {
		if (component) {
			component.unmount();
		}
	});

	it('should render', () => {
		component = shallow(
			<StateProvider>
				<VariantCard
					bestVariant={data[0]}
					data={data}
					label={Liferay.Language.get('variant-report')}
					metric='CLICK_RATE'
					metricUnit='%'
					status='RUNNING'
					winnerDXPVariantId='DEFAULT'
				/>
			</StateProvider>
		);

		expect(component).toMatchSnapshot();
	});
});
