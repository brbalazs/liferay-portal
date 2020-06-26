import Constants from 'shared/util/constants';
import React from 'react';
import SegmentEngagementWithList, {SegmentEngagementChart} from '../Engagement';
import {getTimestamp} from 'test/data';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const {entityTypes} = Constants;

const MOCK_DATA = [
	{
		contributors: 1,
		intervalInitDate: getTimestamp(-1),
		scoreAvg: 2
	},
	{
		contributors: 3,
		intervalInitDate: getTimestamp(),
		scoreAvg: 7
	}
];

const MOCK_COLUMNS = [
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

jest.unmock('react-dom');

describe('SegmentEngagementWithList', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<SegmentEngagementWithList
					columns={MOCK_COLUMNS}
					data={MOCK_DATA}
					entityType={entityTypes.individualsSegment}
					groupId={'23'}
					id={'3'}
					previousScore={1}
					score={7}
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});

describe('SegmentEngagementChart', () => {
	it('should render', () => {
		const {container} = render(<SegmentEngagementChart data={MOCK_DATA} />);

		expect(container).toMatchSnapshot();
	});
});
