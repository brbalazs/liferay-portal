import * as API from 'shared/api';
import AssociatedSegmentsCard from 'contacts/components/AssociatedSegmentsCard';
import DetailsCard from 'contacts/individual/profile/components/DetailsCard';
import FaroConstants from 'shared/util/constants';
import IndividualProfileCard from 'contacts/individual/profile/hoc/ProfileCard';
import InterestsCard from 'contacts/individual/profile/components/InterestsCard';
import React from 'react';
import {Individual} from 'shared/util/records';
import {INDIVIDUAL_COUNT} from 'shared/util/pagination';
import {INDIVIDUALS} from 'shared/util/router';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {entityTypes, pagination} = FaroConstants;

const ITEMS_PER_CARD = 5;

function fetchAssociatedSegments({channelId, groupId, id, searchValue}) {
	return API.individualSegment.search({
		channelId,
		contactsEntityId: id,
		contactsEntityType: entityTypes.individual,
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

export default class Overview extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		individual: PropTypes.instanceOf(Individual).isRequired,
		tabId: PropTypes.string
	};

	render() {
		const {channelId, groupId, id, individual, tabId} = this.props;

		return (
			<div className='overview-layout'>
				<div className='overview-column-main'>
					<IndividualProfileCard
						channelId={channelId}
						entity={individual}
						groupId={groupId}
						tabId={tabId}
					/>
				</div>

				<div className='overview-column-side'>
					<DetailsCard
						channelId={channelId}
						entity={individual}
						groupId={groupId}
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
