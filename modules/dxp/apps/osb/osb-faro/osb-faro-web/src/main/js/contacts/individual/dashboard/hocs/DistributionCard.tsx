import DistributionCard from 'contacts/components/distribution-card';
import React from 'react';
import {connect} from 'react-redux';
import {fetchIndividualsDistribution} from 'shared/actions/distributions';
import {Routes, toRoute} from 'shared/util/router';

const IndividualsDistributionCard = ({channelId, groupId, ...otherProps}) => (
	<DistributionCard
		channelId={channelId}
		distributionKey='individualsDashboard'
		groupId={groupId}
		viewAllLink={toRoute(Routes.CONTACTS_INDIVIDUALS_DISTRIBUTION, {
			channelId,
			groupId
		})}
		{...otherProps}
	/>
);

export default connect(
	null,
	{fetchDistribution: fetchIndividualsDistribution}
)(IndividualsDistributionCard);
