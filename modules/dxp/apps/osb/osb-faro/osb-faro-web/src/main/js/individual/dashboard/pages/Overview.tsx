import * as API from 'shared/api';
import ActiveIndividualsCard from '../hocs/ActiveIndividualsCard';
import Button from 'shared/components/Button';
import Constants from 'shared/util/constants';
import DistributionCard from '../hocs/DistributionCard';
import EnrichedProfilesCard from '../hocs/EnrichedProfilesCard';
import InterestsCard from '../hocs/InterestsCard';
import React, {useEffect, useState} from 'react';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import TypeTrendCard from '../hocs/TypeTrendCard';
import URLConstants from 'shared/util/url-constants';
import {DataSource, User} from 'shared/util/records';
import {fromJS} from 'immutable';
import {Routes, toRoute} from 'shared/util/router';
import {useDataSource} from 'shared/hooks/useDataSource';
import {useParams} from 'react-router-dom';
import {withCurrentUser} from 'shared/hoc';

const {
	pagination: {cur}
} = Constants;

const MAX_DELTA = 500;

interface IOverviewProps extends React.HTMLAttributes<HTMLElement> {
	currentUser: User;
}

const Overview: React.FC<IOverviewProps> = ({currentUser}) => {
	const [dataSources, setDataSources] = useState(null);
	const {channelId, groupId} = useParams();
	const authorized = currentUser.isAdmin();
	const dataSourceStates = useDataSource();

	useEffect(() => {
		API.dataSource
			.search({
				channelId,
				delta: MAX_DELTA,
				groupId,
				page: cur,
				query: ''
			})
			.then(({items}) => {
				setDataSources(items.map(item => new DataSource(fromJS(item))));
			});
	}, []);

	return (
		<StatesRenderer {...dataSourceStates}>
			<StatesRenderer.Empty
				className='bg-white mt-4 py-5 rounded sites-dashboard'
				description={
					<>
						{Liferay.Language.get(
							'connect-a-data-source-with-sites-data'
						)}

						<a
							className='d-block mb-3'
							href={URLConstants.DataSourceConnection}
							key='DOCUMENTATION'
							target='_blank'
						>
							{Liferay.Language.get(
								'access-our-documentation-to-learn-more'
							)}
						</a>

						{authorized && (
							<Button
								display='primary'
								href={toRoute(Routes.SETTINGS_ADD_DATA_SOURCE, {
									groupId
								})}
							>
								{Liferay.Language.get('connect-data-source')}
							</Button>
						)}
					</>
				}
				title={Liferay.Language.get(
					'no-sites-synced-from-data-sources'
				)}
			/>

			<StatesRenderer.Success>
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
							<InterestsCard
								channelId={channelId}
								groupId={groupId}
							/>
						</div>
					</div>

					<div className='row'>
						<div className='col-xl-12'>
							<DistributionCard
								channelId={channelId}
								groupId={groupId}
								showAddDataSource={
									!!dataSources && !dataSources.length
								}
							/>
						</div>
					</div>
				</div>
			</StatesRenderer.Success>
		</StatesRenderer>
	);
};

export default withCurrentUser(Overview);
