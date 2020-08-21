import * as API from 'shared/api';
import * as data from 'test/data';
import ActivitiesChartTimeline from 'contacts/components/ActivitiesChartTimeline';
import faroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import {LAST_30_DAYS} from 'shared/util/constants';

const {entityTypes} = faroConstants;

Object.assign(API.activities.fetchGroup, () =>
	Promise.resolve({
		items: [data.mockActivity(2)],
		total: 1
	})
);

const activityHistory = Array.from({length: 30}, (_, i) => ({
	intervalInitDate: new Date(2019, 0, i + 1).getTime(),
	totalElements: Math.round(Math.random() * 100)
}));

export default class ActivitiesChartTimelineKit extends React.Component {
	render() {
		return (
			<div
				className={
					this.props.className ? ` ${this.props.className}` : ''
				}
			>
				<ActivitiesChartTimeline
					activitiesLabel='Test label'
					entityType={entityTypes.account}
					groupId='23'
					history={activityHistory}
					id='1'
					rangeSelectors={{
						rangeKey: LAST_30_DAYS
					}}
				/>
			</div>
		);
	}
}
