import * as API from 'shared/api';
import ActiveIndividualsCard from '../hocs/ActiveIndividualsCard';
import Constants from 'shared/util/constants';
import DistributionCard from '../hocs/DistributionCard';
import EnrichedProfilesCard from '../hocs/EnrichedProfilesCard';
import InterestsCard from '../hocs/InterestsCard';
import React, {useEffect, useState} from 'react';
import TypeTrendCard from '../hocs/TypeTrendCard';
import {DataSource} from 'shared/util/records';
import {fromJS} from 'immutable';

const {
	pagination: {cur}
} = Constants;

const MAX_DELTA = 500;

interface IOverviewProps {
	router: {
		params: {
			channelId: string;
			groupId: string;
		};
		query: object;
	};
}

const Overview: React.FC<IOverviewProps> = ({
	router: {
		params: {channelId, groupId}
	}
}) => {
	const [dataSources, setDataSources] = useState(null);

	useEffect(() => {
		API.dataSource
			.search({channelId, cur, delta: MAX_DELTA, groupId})
			.then(({items}) => {
				setDataSources(items.map(item => new DataSource(fromJS(item))));
			});
	}, []);

	return (
		<div className='individuals-dashboard-overview-root overview-root'>
			<div className='row'>
				<div className='col-xl-8'>
					<TypeTrendCard channelId={channelId} />
				</div>

				<div className='col-xl-4'>
					<EnrichedProfilesCard
						channelId={channelId}
						dataSources={dataSources}
						groupId={groupId}
					/>
				</div>
			</div>

			<div className='row'>
				<div className='col-xl-12'>
					<ActiveIndividualsCard channelId={channelId} />
				</div>
			</div>

			<div className='row'>
				<div className='col-xl-12'>
					<InterestsCard channelId={channelId} groupId={groupId} />
				</div>
			</div>

			<div className='row'>
				<div className='col-xl-12'>
					<DistributionCard
						channelId={channelId}
						groupId={groupId}
						showAddDataSource={!!dataSources && !dataSources.length}
					/>
				</div>
			</div>
		</div>
	);
};

export default Overview;
