import BundleRouter from 'route-middleware/BundleRouter';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense} from 'react';
import {DataSource} from 'shared/util/records';
import {PropTypes} from 'prop-types';
import {Routes} from 'shared/util/router';
import {Switch} from 'react-router-dom';

const TabRoutes = lazy(() =>
	import(/* webpackChunkName: "LiferayTabRoutes" */ './liferay-old/TabRoutes')
);
const ConfigureContacts = lazy(() =>
	import(
		/* webpackChunkName: "LiferayConfigureContacts" */ './liferay-old/ConfigureContacts'
	)
);
const SyncContacts = lazy(() =>
	import(
		/* webpackChunkName: "LiferaySyncContacts" */ './liferay-old/SyncContacts'
	)
);
const SyncSites = lazy(() =>
	import(/* webpackChunkName: "LiferaySyncSites" */ './liferay-old/SyncSites')
);

export default class LiferayDataSource extends React.Component {
	static propTypes = {
		dataSource: PropTypes.instanceOf(DataSource),
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string
	};

	render() {
		const {dataSource, groupId, id} = this.props;

		return (
			<Suspense fallback={<Loading />}>
				<Switch>
					<BundleRouter
						data={SyncContacts}
						exact
						path={Routes.SETTINGS_LIFERAY_CONTACTS}
					/>

					<BundleRouter
						data={ConfigureContacts}
						exact
						path={Routes.SETTINGS_LIFERAY_CONFIGURE_CONTACTS}
					/>

					<BundleRouter
						data={SyncSites}
						exact
						path={Routes.SETTINGS_LIFERAY_ANALYTICS}
					/>

					<BundleRouter
						componentProps={{dataSource, groupId, id}}
						data={TabRoutes}
						path={Routes.SETTINGS_DATA_SOURCE}
					/>
				</Switch>
			</Suspense>
		);
	}
}
