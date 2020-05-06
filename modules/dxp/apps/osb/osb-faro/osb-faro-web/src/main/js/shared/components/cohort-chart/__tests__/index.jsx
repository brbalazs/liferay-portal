import CohortChart from '../index';
import React from 'react';
import {shallow} from 'enzyme';

describe('CohortChart', () => {
	it('should render', () => {
		const aggregatedCounts = [
			{
				retention: 12.123123,
				value: 10
			},
			{
				retention: 42.123123,
				value: 56
			}
		];

		const data = [
			[
				{
					colorHex: '#000000',
					dateLabel: 'February',
					periodLabel: 'Month 0',
					retention: 12.123123,
					rowKey: '2019-02-01',
					value: 10
				},
				{
					colorHex: '#000000',
					dateLabel: 'March',
					periodLabel: 'Month 1',
					retention: 42.123123,
					rowKey: '2019-03-01',
					value: 56
				}
			],
			[
				{
					colorHex: '#000000',
					dateLabel: 'February',
					periodLabel: 'Month 0',
					retention: 22.113123,
					rowKey: '2019-02-01',
					value: 60
				}
			]
		];

		const component = shallow(
			<CohortChart
				aggregatedCounts={aggregatedCounts}
				data={data}
				dateLabels={['February', 'March']}
				periodLabels={['Month 0', 'Month 1']}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
