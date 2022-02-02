import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import BundleRouter from 'route-middleware/BundleRouter';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense} from 'react';
import RouteNotFound from 'shared/components/RouteNotFound';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import URLConstants from 'shared/util/url-constants';
import {Routes} from 'shared/util/router';
import {Switch} from 'react-router-dom';
import {useChannelContext} from 'shared/context/channel';
import {useDataSource} from 'shared/hooks/useDataSource';

const Distribution = lazy(
	() =>
		import(
			/* webpackChunkName: "IndividualsDashboardDistribution" */ './Distribution'
		)
);

const KnownIndividuals = lazy(
	() =>
		import(
			/* webpackChunkName: "IndividualsDashboardKnownIndividuals" */ './KnownIndividuals'
		)
);

const InterestDetails = lazy(
	() =>
		import(
			/* webpackChunkName: "IndividualsDashboardInterestDetails" */ './InterestDetails'
		)
);

const Interests = lazy(
	() =>
		import(
			/* webpackChunkName: "IndividualsDashboardInterests" */ './Interests'
		)
);

const Overview = lazy(
	() =>
		import(
			/* webpackChunkName: "IndividualsDashboardOverview" */ './Overview'
		)
);

const NAV_ITEMS = [
	{
		exact: true,
		label: Liferay.Language.get('overview'),
		route: Routes.CONTACTS_INDIVIDUALS
	},
	{
		exact: true,
		label: Liferay.Language.get('known-individuals'),
		route: Routes.CONTACTS_INDIVIDUALS_KNOWN_INDIVIDUALS
	},
	{
		exact: false,
		label: Liferay.Language.get('interests'),
		route: Routes.CONTACTS_INDIVIDUALS_INTERESTS
	},
	{
		exact: true,
		label: Liferay.Language.get('distribution'),
		route: Routes.CONTACTS_INDIVIDUALS_DISTRIBUTION
	}
];

interface IDashboardProps extends React.HTMLAttributes<HTMLDivElement> {
	router: {
		params: {
			channelId: string;
			groupId: string;
		};
		query: object;
	};
}

const Dashboard: React.FC<IDashboardProps> = ({
	router: {
		params: {channelId, groupId}
	}
}) => {
	const {selectedChannel} = useChannelContext();
	const dataSourceStates = useDataSource();

	return (
		<BasePage
			className='individuals-dashboard-root'
			documentTitle={Liferay.Language.get('individuals')}
		>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: selectedChannel && selectedChannel.name
					})
				]}
				groupId={groupId}
			>
				<BasePage.Header.TitleSection
					title={Liferay.Language.get('individuals')}
				/>

				<BasePage.Header.NavBar
					items={NAV_ITEMS}
					routeParams={{channelId, groupId}}
				/>
			</BasePage.Header>

			<BasePage.Body>
				<Suspense fallback={<Loading />}>
					<StatesRenderer {...dataSourceStates}>
						<StatesRenderer.Empty
							className='sites-dashboard bg-white mt-4 py-5'
							description={
								<>
									{Liferay.Language.get(
										'connect-a-data-source-with-individuals-data'
									)}

									<a
										className='pl-1'
										href={URLConstants.DataSourceConnection}
										key='DOCUMENTATION'
										target='_blank'
									>
										{Liferay.Language.get(
											'access-our-documentation-to-learn-more'
										)}
									</a>
								</>
							}
							title={Liferay.Language.get(
								'no-individuals-sycned-from-data-sources'
							)}
						/>

						<StatesRenderer.Success>
							<Switch>
								<BundleRouter
									data={Overview}
									destructured={false}
									exact
									path={Routes.CONTACTS_INDIVIDUALS}
								/>

								<BundleRouter
									data={KnownIndividuals}
									path={
										Routes.CONTACTS_INDIVIDUALS_KNOWN_INDIVIDUALS
									}
								/>

								<BundleRouter
									data={Distribution}
									exact
									path={
										Routes.CONTACTS_INDIVIDUALS_DISTRIBUTION
									}
								/>

								<BundleRouter
									data={InterestDetails}
									destructured={false}
									exact
									path={
										Routes.CONTACTS_INDIVIDUALS_INTEREST_DETAILS
									}
								/>

								<BundleRouter
									data={Interests}
									destructured={false}
									exact
									path={Routes.CONTACTS_INDIVIDUALS_INTERESTS}
								/>

								<RouteNotFound />
							</Switch>
						</StatesRenderer.Success>
					</StatesRenderer>
				</Suspense>
			</BasePage.Body>
		</BasePage>
	);
};

export default Dashboard;
