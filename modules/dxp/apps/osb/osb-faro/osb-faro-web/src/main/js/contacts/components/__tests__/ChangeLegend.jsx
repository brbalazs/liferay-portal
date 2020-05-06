import ChangeLegend from '../ChangeLegend';
import React from 'react';
import {CHART_ACTIVITY_ID} from 'shared/util/engagement-activity';
import {DEFAULT_ACTIVITY_MAX} from 'shared/api/activities';
import {shallow} from 'enzyme';
import {sub} from 'shared/util/lang';

describe('ChangeLegend', () => {
	it('should render', () => {
		const mockActivityCount = 50;

		const component = shallow(
			<ChangeLegend
				items={[
					{
						change: 2,
						id: CHART_ACTIVITY_ID,
						secondaryInfo: sub(
							Liferay.Language.get('x-day-total'),
							[DEFAULT_ACTIVITY_MAX]
						),
						title: sub(
							Liferay.Language.get('total-activity-count-x'),
							[mockActivityCount.toLocaleString()]
						)
					}
				]}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with a decrease if change is negative', () => {
		const mockActivityCount = 50;

		const component = shallow(
			<ChangeLegend
				items={[
					{
						change: -2,
						id: CHART_ACTIVITY_ID,
						secondaryInfo: sub(
							Liferay.Language.get('x-day-total'),
							[DEFAULT_ACTIVITY_MAX]
						),
						title: sub(
							Liferay.Language.get('total-activity-count-x'),
							[mockActivityCount.toLocaleString()]
						)
					}
				]}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with no icon if change is 0', () => {
		const mockActivityCount = 50;

		const component = shallow(
			<ChangeLegend
				items={[
					{
						change: 0,
						id: CHART_ACTIVITY_ID,
						secondaryInfo: sub(
							Liferay.Language.get('x-day-total'),
							[DEFAULT_ACTIVITY_MAX]
						),
						title: sub(
							Liferay.Language.get('total-activity-count-x'),
							[mockActivityCount.toLocaleString()]
						)
					}
				]}
			/>
		);

		expect(component).toMatchSnapshot();
	});

	it('should render with hypens if change is Infinite', () => {
		const mockActivityCount = 50;

		const component = shallow(
			<ChangeLegend
				items={[
					{
						change: Infinity,
						id: CHART_ACTIVITY_ID,
						secondaryInfo: sub(
							Liferay.Language.get('x-day-total'),
							[DEFAULT_ACTIVITY_MAX]
						),
						title: sub(
							Liferay.Language.get('total-activity-count-x'),
							[mockActivityCount.toLocaleString()]
						)
					}
				]}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
