import BundleRouter from 'route-middleware/BundleRouter';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense} from 'react';
import useModalNotifications from 'shared/hooks/useModalNotifications';
import {close, open} from 'shared/actions/modals';
import {connect} from 'react-redux';
import {matchPath} from 'react-router';
import {Modal} from 'shared/types';
import {Routes} from 'shared/util/router';
import {Switch} from 'react-router-dom';

// App Routes with Sidebar
const AppSidebarRoutes = lazy(() =>
	import(
		/* webpackChunkName: "AppSidebarRoutes" */ 'shared/pages/AppSidebarRoutes'
	)
);

// Settings
const Settings = lazy(() =>
	import(/* webpackChunkName: "Settings" */ 'settings/pages/Settings')
);

interface IWorkspaceLayerProps {
	close: Modal.close;
	location: {
		pathname: string;
	};
	open: Modal.open;
}

const WorkspaceLayer: React.FC<IWorkspaceLayerProps> = ({
	close,
	location,
	open
}) => {
	const {
		params: {groupId}
	} = matchPath(location.pathname, {
		path: Routes.WORKSPACE_WITH_ID
	});

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

export default connect(
	null,
	{close, open}
)(WorkspaceLayer);
