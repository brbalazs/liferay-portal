import Button from 'shared/components/Button';
import Interests from '../hocs/Interests';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import URLConstants from 'shared/util/url-constants';
import {Routes, toRoute} from 'shared/util/router';
import {Sizes} from 'shared/util/constants';
import {useDataSource} from 'shared/hooks/useDataSource';
import {useParams} from 'react-router-dom';
import {User} from 'shared/util/records';
import {withCurrentUser} from 'shared/hoc';

interface IInterestsPageProps extends React.HTMLAttributes<HTMLElement> {
	currentUser: User;
}

const InterestsPage: React.FC<IInterestsPageProps> = ({currentUser}) => {
	const {groupId} = useParams();
	const authorized = currentUser.isAdmin();
	const dataSourceStates = useDataSource();

	const emptyState = {
		description: (
			<>
				{Liferay.Language.get('connect-a-data-source-with-sites-data')}

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
		),
		title: Liferay.Language.get('no-sites-synced-from-data-sources')
	};

	return (
		<StatesRenderer {...dataSourceStates}>
			<StatesRenderer.Empty {...emptyState} displayCard />

			<StatesRenderer.Success>
				<div className='individuals-dashboard-interests-root'>
					<div className='row'>
						<div className='col-xl-12'>
							<Interests
								noResultsRenderer={() => (
									<NoResultsDisplay
										{...emptyState}
										icon={{
											border: false,
											size: Sizes.XXXLarge,
											symbol: 'ac-satellite'
										}}
									/>
								)}
							/>
						</div>
					</div>
				</div>
			</StatesRenderer.Success>
		</StatesRenderer>
	);
};

export default withCurrentUser(InterestsPage);
