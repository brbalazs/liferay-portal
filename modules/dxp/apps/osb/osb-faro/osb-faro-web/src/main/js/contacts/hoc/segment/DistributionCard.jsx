import DistributionCard from 'contacts/components/distribution-card';
import React from 'react';
import {connect} from 'react-redux';
import {fetchDistribution} from 'shared/actions/distributions';
import {Routes, toRoute} from 'shared/util/router';

const SegmentDistributionCard = ({channelId, groupId, id, ...otherProps}) => (
	<DistributionCard
		channelId={channelId}
		distributionKey={id}
		groupId={groupId}
		id={id}
		showContext
		viewAllLink={toRoute(Routes.CONTACTS_SEGMENT_DISTRIBUTION, {
			channelId,
			groupId,
			id
		})}
		{...otherProps}
	/>
);

export default connect(
	null,
	{fetchDistribution}
)(SegmentDistributionCard);
