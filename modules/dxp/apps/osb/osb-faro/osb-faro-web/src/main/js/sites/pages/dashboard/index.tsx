import * as breadcrumbs from 'shared/util/breadcrumbs';
import Alert from 'shared/components/Alert';
import BasePage from 'shared/components/base-page';
import BundleRouter from 'route-middleware/BundleRouter';
import Button from 'shared/components/Button';
import DataSourceQuery from 'shared/queries/DataSourceQuery';
import EmbeddedAlertList from 'shared/components/EmbeddedAlertList';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense, useContext, useEffect} from 'react';
import RouteNotFound from 'shared/components/RouteNotFound';
import {OAuthUpgradeWarningContext} from 'shared/context/oAuthUpgradeWarning';
import {Routes, toRoute} from 'shared/util/router';
import {Switch} from 'react-router-dom';
import {useChannelContext} from 'shared/context/channel';
import {useQuery} from '@apollo/react-hooks';
import {User} from 'shared/util/records';
import {withCurrentUser} from 'shared/hoc';

const {
	credentialTypes: {oAuth1, oAuth2}
} = FaroConstants;

const InterestDetails = lazy(() =>
	import(
		/* webpackChunkName: "SitesDashboardInterestDetails" */ './InterestDetails'
	)
);
const Interests = lazy(() =>
	import(/* webpackChunkName: "SitesDashboardInterests" */ './Interests')
);
const Overview = lazy(() =>
	import(/* webpackChunkName: "SitesDashboardOverview" */ './Overview')
);
const Touchpoints = lazy(() =>
	import(/* webpackChunkName: "SitesDashboardTouchpoints" */ './Touchpoints')
);

const getAlert = (
	channelId: string,
	groupId: string,
	setShowWarning: (value: boolean) => void
) => [
	{
		iconSymbol: 'warning',
		message: (
			<>
				{Liferay.Language.get(
					'one-or-more-of-your-data-sources-needs-to-upgrade-from-oauth-to-the-new-token-based-connection'
				)}
				<div className='mt-3 pl-4'>
					<Button
						display='warning'
						href={toRoute(Routes.SETTINGS_DATA_SOURCE_LIST, {
							channelId,
							groupId
						})}
						size='sm'
					>
						{Liferay.Language.get('go-to-data-sources')}
					</Button>
				</div>
			</>
		),
		onClose: () => {
			setShowWarning(false);
		},
		stripe: true,
		title: 'Warning',
		type: Alert.TYPES.warning
	}
];

const NAV_ITEMS = [
	{
		exact: true,
		label: Liferay.Language.get('overview'),
		route: Routes.SITES
	},
	{
		exact: true,
		label: Liferay.Language.get('pages'),
		route: Routes.SITES_TOUCHPOINTS
	},
	{
		exact: false,
		label: Liferay.Language.get('interests'),
		route: Routes.SITES_INTERESTS
	}
];

type RouterParams = {
	channelId: string;
	groupId: string;
};

type Router = {
	params: RouterParams;
	query: object;
};

interface IDashboardProps extends React.HTMLAttributes<HTMLDivElement> {
	currentUser: User;
	router: Router;
}

export const Dashboard: React.FC<IDashboardProps> = ({currentUser, router}) => {
	const {channelId, groupId} = router.params;
	const {selectedChannel} = useChannelContext();

	const selectedChannelName = selectedChannel && selectedChannel.name;

	const {setShowOAuthUpgradeWarning, showOAuthUpgradeWarning} = useContext(
		OAuthUpgradeWarningContext
	);

	const {data: oAuth1Data} = useQuery(DataSourceQuery, {
		variables: {credentialsType: oAuth1}
	});
	const {data: oAuth2Data} = useQuery(DataSourceQuery, {
		variables: {credentialsType: oAuth2}
	});

	useEffect(() => {
		if (
			showOAuthUpgradeWarning === null &&
			oAuth1Data &&
			oAuth2Data &&
			oAuth1Data.dataSources.length + oAuth2Data.dataSources.length > 0
		) {
			setShowOAuthUpgradeWarning(true);
		}
	}, [oAuth1Data, oAuth2Data]);

	return (
		<BasePage
			className='sites-dashboard-root'
			documentTitle={Liferay.Language.get('sites')}
		>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: selectedChannelName
					})
				]}
			>
				<BasePage.Header.TitleSection
					className={getCN({'no-sites-connected': !selectedChannel})}
					title={
						selectedChannel
							? Liferay.Language.get('sites')
							: Liferay.Language.get('no-sites-connected')
					}
				/>

				<BasePage.Header.NavBar
					items={NAV_ITEMS}
					routeParams={{channelId, groupId}}
				/>
			</BasePage.Header>
			{showOAuthUpgradeWarning && currentUser.isAdmin() && (
				<EmbeddedAlertList
					alerts={getAlert(
						channelId,
						groupId,
						setShowOAuthUpgradeWarning
					)}
				/>
			)}

			<BasePage.Context.Provider
				value={{
					filters: {},
					router
				}}
			>
				<BasePage.Body>
					<Suspense fallback={<Loading />}>
						<Switch>
							<BundleRouter
								data={InterestDetails}
								destructured={false}
								exact
								path={Routes.SITES_INTEREST_DETAILS}
							/>

							<BundleRouter
								data={Interests}
								destructured={false}
								exact
								path={Routes.SITES_INTERESTS}
							/>

							<BundleRouter
								data={Touchpoints}
								destructured={false}
								exact
								path={Routes.SITES_TOUCHPOINTS}
							/>

							<BundleRouter
								componentProps={{
									channelName: selectedChannelName
								}}
								data={Overview}
								destructured={false}
								exact
								path={Routes.SITES}
							/>

							<RouteNotFound />
						</Switch>
					</Suspense>
				</BasePage.Body>
			</BasePage.Context.Provider>
		</BasePage>
	);
};

export default withCurrentUser(Dashboard);
