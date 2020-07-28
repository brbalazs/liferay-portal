import * as data from 'test/data';
import React from 'react';
import {render} from '@testing-library/react';
import {
	SegmentGrowthChart,
	SegmentGrowthWithList,
	SelectedPointInfo
} from '../Growth';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

describe('SegmentGrowthWithList', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<SegmentGrowthWithList
					channelId='123'
					data={[]}
					groupId='23'
					id='3'
					onPointSelect={jest.fn()}
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});

describe('SegmentGrowthChart', () => {
	it('should render', () => {
		const {container} = render(
			<SegmentGrowthChart data={[]} onPointSelect={jest.fn()} />
		);

		expect(container).toMatchSnapshot();
	});
});

describe('SelectedPointInfo', () => {
	it('should render', () => {
		const {container} = render(
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
		expect(container).toMatchSnapshot();
	});
});
