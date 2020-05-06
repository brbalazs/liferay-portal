import * as data from 'test/data';
import faroConstants from 'shared/util/constants';
import React from 'react';
import {ActivitiesChartTimeline} from '../ActivitiesChartTimeline';
import {shallow} from 'enzyme';

const {entityTypes} = faroConstants;

const {activityAggregations} = data.mockActivityHistory();

describe('ActivitiesChartTimeline', () => {
	it('should render', () => {
		const component = shallow(
			<ActivitiesChartTimeline
				activitiesLabel={Liferay.Language.get('accounts-activities-x')}
				channelId='123123'
				entityType={entityTypes.account}
				groupId={'23'}
				history={activityAggregations}
				id={'123'}
			/>
		);

		expect(component).toMatchSnapshot();
	});
});
