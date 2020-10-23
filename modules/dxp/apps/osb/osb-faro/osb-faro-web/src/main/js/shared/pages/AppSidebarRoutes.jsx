import BundleRouter from '../../route-middleware/BundleRouter';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense} from 'react';
import RouteNotFound from 'shared/components/RouteNotFound';
import {ChannelContext} from 'shared/context/channel';
import {connect} from 'react-redux';
import {DEVELOPER_MODE} from 'shared/util/constants';
import {Routes} from 'shared/util/router';
import {Switch, withRouter} from 'react-router-dom';
import {withOnboarding, withUnassignedSegments} from 'shared/hoc';
import {withSidebar} from 'shared/hoc';

const UIKit = lazy(() =>
	import(/* webpackChunkName: "UIKit" */ '../../ui-kit/pages/index')
);

/* No Properties Available */
const NoPropertiesAvailable = lazy(() =>
	import(
		/* webpackChunkName: "NoPropertiesAvailable" */ './NoPropertiesAvailable'
	)
);

/* Segments */
const SegmentsList = lazy(() =>
	import(
		/* webpackChunkName: "SegmentsList" */ '../../contacts/pages/segment/List'
	)
);
const SegmentProfileRoutes = lazy(() =>
	import(
		/* webpackChunkName: "SegmentProfileRoutes" */ '../../contacts/pages/segment/ProfileRoutes'
	)
);
const SegmentEdit = lazy(() =>
	import(
		/* webpackChunkName: "SegmentEdit" */ '../../contacts/pages/segment/Edit'
	)
);

/* Accounts */

const AccountsList = lazy(() =>
	import(
		/* webpackChunkName: "AccountsList" */ '../../contacts/pages/account/List'
	)
);
const AccountProfileRoutes = lazy(() =>
	import(
		/* webpackChunkName: "AccountProfileRoutes" */ '../../contacts/pages/account/ProfileRoutes'
	)
);

/* Individuals */

const IndividualProfileRoutes = lazy(() =>
	import(
		/* webpackChunkName: "IndividualProfileRoutes" */ '../../contacts/individual/profile/pages/ProfileRoutes'
	)
);
const IndividualsDashboard = lazy(() =>
	import(
		/* webpackChunkName: "IndividualsDashboard" */ '../../contacts/individual/dashboard/pages'
	)
);

/* Sites */

const SitesDashboard = lazy(() =>
	import(
		/* webpackChunkName: "SitesDashboard" */ '../../sites/pages/dashboard'
	)
);

/* Experiments */

const ExperimentsList = lazy(() =>
	import(
		/* webpackChunkName: "ExperimentsList" */ '../../experiments/pages/ExperimentsListPage'
	)
);

const ExperimentOverview = lazy(() =>
	import(
		/* webpackChunkName: "ExperimentsList" */ '../../experiments/pages/ExperimentOverviewPage'
	)
);

const TouchpointRoutes = lazy(() =>
	import(
		/* webpackChunkName: "TouchpointRoutes" */ 'touchpoints/pages/TouchpointRoutes'
	)
);

/* Assets - Blogs */

const AssetsList = lazy(() =>
	import(/* webpackChunkName: "AssetsList" */ 'assets/index')
);

const BlogsRoutes = lazy(() =>
	import(
		/* webpackChunkName: "BlogsRoutes" */ 'assets/blogs/pages/BlogsRoutes'
	)
);

const CustomAssetsDashboard = lazy(() =>
	import(
		/* webpackChunkName: "CustomAssetsDashboard" */ 'assets/custom-assets/pages/CustomAssetsDashboardPage'
	)
);

const DocumentsAndMediaRoutes = lazy(() =>
	import(
		/* webpackChunkName: "DocumentsAndMediaRoutes" */ 'assets/documents-and-media/pages/DocumentsAndMediaRoutes'
	)
);

const FormsRoutes = lazy(() =>
	import(
		/* webpackChunkName: "FormsRoutes" */ 'assets/forms/pages/FormsRoutes'
	)
);

const WebContentRoutes = lazy(() =>
	import(
		/* webpackChunkName: "WebContentRoutes" */ 'assets/web-content/pages/WebContentRoutes'
	)
);

const ROUTES = [
	{
		data: AccountsList,
		path: Routes.CONTACTS_LIST_ACCOUNT
	},
	{
		data: AccountProfileRoutes,
		exact: false,
		path: Routes.CONTACTS_ACCOUNT
	},
	{
		data: IndividualProfileRoutes,
		exact: false,
		path: Routes.CONTACTS_INDIVIDUAL
	},
	{
		data: IndividualsDashboard,
		destructured: false,
		exact: false,
		path: Routes.CONTACTS_INDIVIDUALS
	},
	{
		data: SegmentsList,
		path: Routes.CONTACTS_LIST_SEGMENT
	},
	{
		data: SegmentEdit,
		path: Routes.CONTACTS_SEGMENT_EDIT
	},
	{
		data: SegmentEdit,
		path: Routes.CONTACTS_SEGMENT_CREATE
	},
	{
		data: SegmentProfileRoutes,
		exact: false,
		path: Routes.CONTACTS_SEGMENT
	},
	{
		data: BlogsRoutes,
		destructured: false,
		path: Routes.ASSETS_BLOGS_ROUTES
	},
	{
		data: CustomAssetsDashboard,
		destructured: false,
		path: Routes.ASSETS_CUSTOM_DASHBOARD
	},
	{
		data: DocumentsAndMediaRoutes,
		destructured: false,
		exact: false,
		path: Routes.ASSETS_DOCUMENTS_AND_MEDIA_ROUTES
	},
	{
		data: FormsRoutes,
		destructured: false,
		exact: false,
		path: Routes.ASSETS_FORMS_ROUTES
	},
	{
		data: WebContentRoutes,
		destructured: false,
		exact: false,
		path: Routes.ASSETS_WEB_CONTENT_ROUTES
	},
	{
		data: TouchpointRoutes,
		destructured: false,
		exact: false,
		path: Routes.SITES_TOUCHPOINTS_ROUTES
	},
	{
		data: ExperimentsList,
		destructured: false,
		path: Routes.TESTS
	},
	{
		data: ExperimentOverview,
		destructured: false,
		path: Routes.TESTS_OVERVIEW
	},
	{
		data: AssetsList,
		destructured: false,
		exact: false,
		path: Routes.ASSETS
	},
	{
		data: SitesDashboard,
		destructured: false,
		exact: false,
		path: Routes.SITES
	},
	{
		data: SitesDashboard,
		destructured: false,
		path: Routes.CHANNEL
	}
];

@withRouter
@withSidebar
@withOnboarding
@withUnassignedSegments
@connect((store, {groupId}) => ({
	project: store.getIn(['projects', groupId, 'data'])
}))
export default class AppSidebarRoutes extends React.PureComponent {
	static contextType = ChannelContext;

	componentDidMount() {
		const {
			currentUser,
			groupId,
			project: {
				faroSubscription: faroSubscriptionIMap,
				name,
				serverLocation
			}
		} = this.props;

		analytics.identify(currentUser.id);

		analytics.group(groupId, {
			groupId,
			serverLocation,
			subscriptionName: faroSubscriptionIMap.get('name'),
			workspaceName: name
		});

		analytics.track('User accessed workspace', {
			groupId,
			serverLocation,
			subscriptionName: faroSubscriptionIMap.get('name'),
			userId: String(currentUser.id),
			workspaceName: name
		});
	}

	render() {
		const {currentUser, groupId} = this.props;
		const {selectedChannel} = this.context;

		return (
			<Suspense fallback={<Loading />}>
				<Switch>
					{!selectedChannel && (
						<BundleRouter
							componentProps={{currentUser, groupId}}
							data={NoPropertiesAvailable}
							exact={false}
							path={Routes.WORKSPACE_WITH_ID}
						/>
					)}

					{ROUTES.map(({data, exact = true, path, ...otherProps}) => (
						<BundleRouter
							{...otherProps}
							data={data}
							exact={exact}
							key={path}
							path={path}
						/>
					))}

					{DEVELOPER_MODE && (
						<BundleRouter data={UIKit} exact path={Routes.UI_KIT} />
					)}

					<RouteNotFound />
				</Switch>
			</Suspense>
		);
	}
}
