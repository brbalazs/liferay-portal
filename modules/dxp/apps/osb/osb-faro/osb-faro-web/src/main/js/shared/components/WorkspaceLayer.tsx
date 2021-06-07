import BundleRouter from 'route-middleware/BundleRouter';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense, useEffect} from 'react';
import useModalNotifications from 'shared/hooks/useModalNotifications';
import {close, open} from 'shared/actions/modals';
import {compose} from 'redux';
import {connect} from 'react-redux';
import {matchPath} from 'react-router';
import {Modal} from 'shared/types';
import {Project} from 'shared/util/records';
import {Routes} from 'shared/util/router';
import {Switch} from 'react-router-dom';
import {withHelpWidget} from 'shared/hoc';

// App Routes with Sidebar
const AppSidebarRoutes = lazy(
	() =>
		import(
			/* webpackChunkName: "AppSidebarRoutes" */ 'shared/pages/AppSidebarRoutes'
		)
);

// Settings
const Settings = lazy(
	() => import(/* webpackChunkName: "Settings" */ 'settings/pages/Settings')
);

interface IWorkspaceLayerProps {
	close: Modal.close;
	currentUserId: string;
	faroSubscriptionIMap: Map<string, any>;
	groupId: string;
	open: Modal.open;
	serverLocation: string;
	workspaceName: string;
}

const WorkspaceLayer: React.FC<IWorkspaceLayerProps> = ({
	close,
	currentUserId,
	faroSubscriptionIMap,
	groupId,
	open,
	serverLocation,
	workspaceName
}) => {
	useEffect(() => {
		if (currentUserId && currentUserId !== '0' && workspaceName) {
			analytics.identify(currentUserId, null, {ip: '0'});

			pendo?.initialize({
				account: {
					groupId,
					id: groupId,
					name,
					serverLocation,
					subscriptionName: faroSubscriptionIMap.get('name'),
					workspaceName: name
				},
				visitor: {
					id: currentUserId
				}
			});

			analytics?.group(
				groupId,
				{
					groupId,
					serverLocation,
					subscriptionName: faroSubscriptionIMap.get('name'),
					workspaceName: name
				},
				{ip: '0'}
			);

			analytics?.track(
				'User accessed workspace',
				{
					groupId,
					serverLocation,
					subscriptionName: faroSubscriptionIMap.get('name'),
					userId: String(currentUserId),
					workspaceName
				},
				{ip: '0'}
			);
		}
	}, [currentUserId, workspaceName]);

	useModalNotifications(close, groupId, open);

	return (
		<Suspense fallback={<Loading />}>
			<Switch>
				<BundleRouter data={Settings} path={Routes.SETTINGS} />

				<BundleRouter data={AppSidebarRoutes} path={Routes.CHANNEL} />
			</Switch>
		</Suspense>
	);
};

export default compose(
	connect(
		(store, {location: {pathname}}) => {
			const {
				params: {groupId}
			} = matchPath(pathname, {
				path: Routes.WORKSPACE_WITH_ID
			});

			const project =
				store.getIn(['projects', groupId, 'data'], new Project()) ||
				new Project();

			return {
				currentUserId: store.getIn(['currentUser', 'data']),
				faroSubscriptionIMap: project.get('faroSubscription'),
				groupId,
				serverLocation: project.get('serverLocation'),
				workspaceName: project.get('name')
			};
		},
		{close, open}
	),
	withHelpWidget
)(WorkspaceLayer);
