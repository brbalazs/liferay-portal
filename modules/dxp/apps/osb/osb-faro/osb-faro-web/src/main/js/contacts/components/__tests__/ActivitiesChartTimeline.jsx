import * as data from 'test/data';
import faroConstants from 'shared/util/constants';
import React from 'react';
import {ActivitiesChartTimeline} from '../ActivitiesChartTimeline';
import {render} from '@testing-library/react';
import {StaticRouter} from 'react-router';

jest.unmock('react-dom');

const {entityTypes} = faroConstants;

const {activityAggregations} = data.mockActivityHistory();

describe('ActivitiesChartTimeline', () => {
	it('should render', () => {
		const {container} = render(
			<StaticRouter>
				<ActivitiesChartTimeline
					activitiesLabel={Liferay.Language.get(
						'accounts-activities-x'
					)}
					channelId='123123'
					entityType={entityTypes.account}
					groupId='23'
					history={activityAggregations}
					id='123'
					rangeSelectors={{
						rangeKey: '30'
					}}
				/>
			</StaticRouter>
		);

		jest.runAllTimers();

		expect(container).toMatchSnapshot();
	});
});
