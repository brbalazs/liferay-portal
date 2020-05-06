import * as data from 'test/data';
import React from 'react';
import {
	SegmentGrowthChart,
	SegmentGrowthWithList,
	SelectedPointInfo
} from '../Growth';
import {shallow} from 'enzyme';

describe('SegmentGrowthWithList', () => {
	it('should render', () => {
		const component = shallow(
			<SegmentGrowthWithList
				channelId={'123'}
				data={[]}
				groupId={'23'}
				id={'3'}
				onPointSelect={jest.fn()}
			/>
		);
		expect(component).toMatchSnapshot();
	});
});

describe('SegmentGrowthChart', () => {
	it('should render', () => {
		const component = shallow(
			<SegmentGrowthChart data={[]} onPointSelect={jest.fn()} />
		);
		expect(component).toBeTruthy();
	});
});

describe('SelectedPointInfo', () => {
	it('should render', () => {
		const component = shallow(
			<SelectedPointInfo
				data={[
					{
						added: 1,
						modifiedDate: data.getTimestamp(),
						removed: 3
					}
				]}
				hasSelectedPoint
				onClearSelection={jest.fn()}
				selectedPoint={0}
			/>
		);
		expect(component.shallow()).toMatchSnapshot();
	});
});
