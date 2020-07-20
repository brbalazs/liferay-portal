import * as data from 'test/data';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {EngagementWithList, SelectedPointInfo} from '../Engagement';
import {shallow} from 'enzyme';

const {entityTypes} = FaroConstants;

const columns = [
	{
		accessor: 'name',
		label: Liferay.Language.get('name'),
		sortable: false,
		title: true
	},
	{
		accessor: 'emailAddress',
		label: Liferay.Language.get('email'),
		sortable: false
	},
	{
		accessor: 'score',
		label: Liferay.Language.get('engagement')
	}
];

const mockEngagementData = [
	{
		intervalInitDate: data.getTimestamp(-1),
		scoreAvg: 2
	},
	{
		intervalInitDate: data.getTimestamp(),
		scoreAvg: 7
	}
];

const tooltipLabels = {
	scoreLabel: 'Segment Engagement',
	subtitleLabel: 'subtitle label'
};

describe('EngagementWithList', () => {
	const defaultProps = {
		columns,
		entityType: entityTypes.individualsSegment,
		groupId: '23',
		id: '3',
		onPointSelect: jest.fn(),
		previousScore: 1,
		score: 7,
		tooltipLabels
	};

	it('should render', () => {
		const component = shallow(
			<EngagementWithList {...defaultProps} data={mockEngagementData} />
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with empty engagement', () => {
		const component = shallow(
			<EngagementWithList {...defaultProps} data={[]} hasSelectedPoint />
		);

		expect(component).toBeTruthy();
	});
});

describe('SelectedPointInfo', () => {
	it('should render', () => {
		const component = shallow(
			<SelectedPointInfo
				data={[
					...mockEngagementData,
					{
						intervalInitDate: data.getTimestamp(1),
						scoreAvg: 1.2
					},
					{
						intervalInitDate: data.getTimestamp(2),
						scoreAvg: null
					}
				]}
				hasSelectedPoint
				previousScore={2.0}
				scoreLabel='Segment Engagement'
				selectedPoint={0}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
