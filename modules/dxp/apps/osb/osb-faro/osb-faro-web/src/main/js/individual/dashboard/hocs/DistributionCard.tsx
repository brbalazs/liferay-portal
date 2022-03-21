import DistributionCard from 'contacts/components/distribution-card';
import React from 'react';
import {connect, ConnectedProps} from 'react-redux';
import {fetchIndividualsDistribution} from 'shared/actions/distributions';
import {Routes, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';

const connector = connect(null, {
	fetchDistribution: fetchIndividualsDistribution
});

type PropsFromRedux = ConnectedProps<typeof connector>;

interface IIndividualsDistributionCardProps
	extends React.HTMLAttributes<HTMLElement>,
		PropsFromRedux {
	id?: string;
	showAddDataSource?: boolean;
}

const IndividualsDistributionCard: React.FC<IIndividualsDistributionCardProps> = ({
	fetchDistribution,
	id,
	...otherProps
}) => {
	const {channelId, groupId} = useParams();

	return (
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
};

export default connector(IndividualsDistributionCard);
