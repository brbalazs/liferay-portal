import * as API from 'shared/api';
import Card from 'shared/components/Card';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {
	ACTIVITIES_COUNT,
	ENGAGEMENT_SCORE,
	JOB_TITLE,
	LAST_ACTIVITY_DATE,
	NAME,
	paginationConfig,
	paginationDefaults
} from 'shared/util/pagination';
import {buildOrderByFields} from 'shared/util/pagination';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {INDIVIDUALS} from 'shared/util/router';
import {individualsListColumns} from 'shared/util/table-columns';
import {PropTypes} from 'prop-types';

function fetchIndividuals({id, orderBy, orderByField, ...otherData}) {
	return API.individuals.search({
		...otherData,
		accountId: id,
		orderByFields: buildOrderByFields(
			{field: orderByField, sortOrder: orderBy},
			INDIVIDUALS
		)
	});
}

export class KnownIndividuals extends React.Component {
	static defaultProps = {
		...paginationDefaults,
		orderByField: NAME
	};

	static propTypes = {
		...paginationConfig,
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		orderByField: PropTypes.string,
		timeZoneId: PropTypes.string
	};

	render() {
		const {
			channelId,
			delta,
			filterBy,
			groupId,
			id,
			orderBy,
			orderByField,
			page,
			query,
			timeZoneId
		} = this.props;

		return (
			<Card pageDisplay>
				<SearchableEntityTable
					columns={[
						individualsListColumns.getNameEmail({
							channelId,
							groupId
						}),
						individualsListColumns.jobTitle,
						individualsListColumns.activitiesCount,
						individualsListColumns.engagementScore,
						individualsListColumns.getLastActivityDate(timeZoneId)
					]}
					dataSourceFn={fetchIndividuals}
					dataSourceParams={{channelId, groupId, id}}
					delta={delta}
					entityLabel={Liferay.Language.get('individuals')}
					filterBy={filterBy}
					orderBy={orderBy}
					orderByField={orderByField}
					orderByOptions={[
						{
							label: Liferay.Language.get('name'),
							value: NAME
						},
						{
							label: Liferay.Language.get('title'),
							value: JOB_TITLE
						},
						{
							label: Liferay.Language.get('activities'),
							value: ACTIVITIES_COUNT
						},
						{
							label: Liferay.Language.get('30-day-engagement'),
							value: ENGAGEMENT_SCORE
						},
						{
							label: Liferay.Language.get('last-activity'),
							value: LAST_ACTIVITY_DATE
						}
					]}
					page={page}
					query={query}
					rowIdentifier='id'
				/>
			</Card>
		);
	}
}

export default compose(
	connect((store, {groupId}) => ({
		timeZoneId: store.getIn([
			'projects',
			groupId,
			'data',
			'timeZone',
			'timeZoneId'
		])
	}))
)(KnownIndividuals);
