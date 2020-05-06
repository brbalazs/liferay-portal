import React from 'react';
import SegmentEngagementWithList, {SegmentEngagementChart} from '../Engagement';
import {getTimestamp} from 'test/data';
import {shallow} from 'enzyme';

const mockEngagementData = [
	{
		intervalInitDate: getTimestamp(-1),
		scoreAvg: 2
	},
	{
		intervalInitDate: getTimestamp(),
		scoreAvg: 7
	}
];

describe('SegmentEngagementWithList', () => {
	it('should render', () => {
		const component = shallow(
			<SegmentEngagementWithList
				data={mockEngagementData}
				groupId={'23'}
				id={'3'}
				previousScore={1}
				score={7}
			/>
		);

		expect(component).toBeTruthy();
	});
});

describe('SegmentEngagementChart', () => {
	it('should render', () => {
		const component = shallow(
			<SegmentEngagementChart
				data={mockEngagementData}
				groupId={'23'}
				id={'3'}
				previousScore={1}
				score={7}
			/>
		);

		expect(component).toBeTruthy();
	});
});
