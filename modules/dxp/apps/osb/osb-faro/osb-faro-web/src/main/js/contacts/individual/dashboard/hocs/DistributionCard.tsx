import DistributionCard from 'contacts/components/distribution-card';
import React from 'react';
import {connect, ConnectedProps} from 'react-redux';
import {fetchIndividualsDistribution} from 'shared/actions/distributions';
import {Routes, toRoute} from 'shared/util/router';

const connector = connect(null, {
	fetchDistribution: fetchIndividualsDistribution
});

type PropsFromRedux = ConnectedProps<typeof connector>;

const IndividualsDistributionCard: React.FC<
	{
		channelId: string;
		groupId: string;
		id?: string;
		showAddDataSource?: boolean;
	} & PropsFromRedux
> = ({channelId, fetchDistribution, groupId, id, ...otherProps}) => (
	<DistributionCard
		channelId={channelId}
		distributionKey='individualsDashboard'
		fetchDistribution={fetchDistribution}
		groupId={groupId}
		id={id}
		viewAllLink={toRoute(Routes.CONTACTS_INDIVIDUALS_DISTRIBUTION, {
			channelId,
			groupId
		})}
		{...otherProps}
	/>
);

export default connector(IndividualsDistributionCard);
