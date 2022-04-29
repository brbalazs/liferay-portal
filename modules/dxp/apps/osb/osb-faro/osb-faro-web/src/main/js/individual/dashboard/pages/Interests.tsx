import Interests from '../hocs/Interests';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import URLConstants from 'shared/util/url-constants';
import {Sizes} from 'shared/util/constants';
import {useDataSource} from 'shared/hooks/useDataSource';
import {User} from 'shared/util/records';
import {withCurrentUser} from 'shared/hoc';

interface IInterestsPageProps extends React.HTMLAttributes<HTMLElement> {
	currentUser: User;
}

const InterestsPage: React.FC<IInterestsPageProps> = () => {
	const dataSourceStates = useDataSource();

	const emptyState = {
		description: (
			<>
				{Liferay.Language.get(
					'you-can-come-back-later-and-check-if-there-is-any-data-received-from-your-data-sources'
				)}

				<a
					className='d-block mb-3'
					href={
						URLConstants.IndividualsDashboardInterestsDocumentation
					}
					key='DOCUMENTATION'
					target='_blank'
				>
					{Liferay.Language.get('learn-more-about-interests')}
				</a>
			</>
		),
		title: Liferay.Language.get('there-are-no-interests-found')
	};

	return (
		<StatesRenderer {...dataSourceStates}>
			<StatesRenderer.Empty {...emptyState} displayCard />

			<StatesRenderer.Success>
				<div className='individuals-dashboard-interests-root'>
					<div className='row'>
						<div className='col-xl-12'>
							<Interests
								noResultsRenderer={
									<NoResultsDisplay
										{...emptyState}
										icon={{
											border: false,
											size: Sizes.XXXLarge,
											symbol: 'ac-satellite'
										}}
									/>
								}
							/>
						</div>
					</div>
				</div>
			</StatesRenderer.Success>
		</StatesRenderer>
	);
};

export default withCurrentUser(InterestsPage);
