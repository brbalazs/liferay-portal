import * as API from 'shared/api';
import AssociatedSegmentsCard from 'contacts/components/AssociatedSegmentsCard';
import Constants, {EntityTypes} from 'shared/util/constants';
import DetailsCard from 'contacts/individual/profile/components/DetailsCard';
import IndividualProfileCard from 'contacts/individual/profile/hoc/ProfileCard';
import InterestsCard from 'contacts/individual/profile/components/InterestsCard';
import React from 'react';
import {connect} from 'react-redux';
import {Individual} from 'shared/util/records';
import {INDIVIDUAL_COUNT} from 'shared/util/pagination';
import {INDIVIDUALS} from 'shared/util/router';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {pagination} = Constants;

const ITEMS_PER_CARD = 5;

function fetchAssociatedSegments({channelId, groupId, id, searchValue}) {
	return API.individualSegment.search({
		channelId,
		contactsEntityId: id,
		contactsEntityType: EntityTypes.Individual,
		delta: ITEMS_PER_CARD,
		groupId,
		orderByFields: [
			{
				fieldName: INDIVIDUAL_COUNT,
				orderBy: pagination.orderDescending,
				system: true
			}
		],
		query: searchValue
	});
}

export class Overview extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		individual: PropTypes.instanceOf(Individual).isRequired,
		tabId: PropTypes.string,
		timeZoneId: PropTypes.string
	};

	render() {
		const {
			channelId,
			groupId,
			id,
			individual,
			tabId,
			timeZoneId
		} = this.props;

		return (
			<div className='overview-layout'>
				<div className='overview-column-main'>
					<IndividualProfileCard
						channelId={channelId}
						entity={individual}
						groupId={groupId}
						tabId={tabId}
						timeZoneId={timeZoneId}
					/>
				</div>

				<div className='overview-column-side'>
					<DetailsCard
						channelId={channelId}
						entity={individual}
						groupId={groupId}
						timeZoneId={timeZoneId}
					/>

					<InterestsCard
						channelId={channelId}
						compact
						entity={individual}
						groupId={groupId}
						showFilter
						type={INDIVIDUALS}
					/>

					<AssociatedSegmentsCard
						channelId={channelId}
						dataSourceFn={fetchAssociatedSegments}
						groupId={groupId}
						id={id}
						pageUrl={toRoute(Routes.CONTACTS_INDIVIDUAL_SEGMENTS, {
							channelId,
							groupId,
							id
						})}
					/>
				</div>
			</div>
		);
	}
}

export default connect((store, {groupId}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))(Overview);
