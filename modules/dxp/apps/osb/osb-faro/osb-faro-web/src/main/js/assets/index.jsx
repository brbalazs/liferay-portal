import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import BundleRouter from 'route-middleware/BundleRouter';
import getCN from 'classnames';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense} from 'react';
import RouteNotFound from 'shared/components/RouteNotFound';
import WrappedPageComponent from 'cerebro-shared/hocs/WrappedPageComponent';
import {PropTypes} from 'prop-types';
import {Routes} from 'shared/util/router';
import {Switch} from 'react-router-dom';
import {useChannelContext} from 'shared/context/channel';

/**
 * Forms List Page
 * @function
 */

const BlogsList = lazy(() =>
	import(/* webpackChunkName: "BlogsList" */ './blogs/pages/BlogsListPage')
);

const CustomList = lazy(() =>
	import(
		/* webpackChunkName: "CustomList" */ './custom-assets/pages/CustomAssetsListPage'
	)
);

const DocumentsAndMediaList = lazy(() =>
	import(
		/* webpackChunkName: "DocumentsAndMediaList" */ './documents-and-media/pages/DocumentsAndMediaListPage'
	)
);

const FormsList = lazy(() =>
	import(/* webpackChunkName: "FormsList" */ './forms/pages/FormsListPage')
);

const WebContentList = lazy(() =>
	import(
		/* webpackChunkName: "WebContentList" */ './web-content/pages/WebContentListPage'
	)
);

const NAV_ITEMS = [
	{
		exact: true,
		label: Liferay.Language.get('blogs'),
		route: Routes.ASSETS_BLOGS
	},
	{
		exact: true,
		label: Liferay.Language.get('documents-and-media'),
		route: Routes.ASSETS_DOCUMENTS_AND_MEDIA
	},
	{
		exact: true,
		label: Liferay.Language.get('forms'),
		route: Routes.ASSETS_FORMS
	},
	{
		exact: true,
		label: Liferay.Language.get('web-content'),
		route: Routes.ASSETS_WEB_CONTENT
	},
	{
		exact: true,
		label: Liferay.Language.get('custom'),
		route: Routes.ASSETS_CUSTOM
	}
];

function AssetsRoutes({className, router}) {
	const {channelId, groupId} = router.params;

	const {selectedChannel} = useChannelContext();

	return (
		<BasePage
			className={getCN(className)}
			documentTitle={Liferay.Language.get('assets')}
		>
			<BasePage.Header
				breadcrumbs={[
					breadcrumbs.getHome({
						channelId,
						groupId,
						label: selectedChannel && selectedChannel.name
					})
				]}
			>
				<BasePage.Header.TitleSection
					title={Liferay.Language.get('assets')}
				/>

				<BasePage.Header.NavBar
					items={NAV_ITEMS}
					routeParams={{channelId, groupId}}
				/>
			</BasePage.Header>

			<BasePage.Body>
				<BasePage.Context.Provider
					value={{
						filters: {},
						router
					}}
				>
					<Suspense fallback={<Loading />}>
						<Switch>
							<BundleRouter
								data={BlogsList}
								destructured={false}
								exact
								path={Routes.ASSETS_BLOGS}
							/>

							<BundleRouter
								data={CustomList}
								destructured={false}
								exact
								path={Routes.ASSETS_CUSTOM}
							/>

							<BundleRouter
								data={DocumentsAndMediaList}
								destructured={false}
								exact
								path={Routes.ASSETS_DOCUMENTS_AND_MEDIA}
							/>

							<BundleRouter
								data={FormsList}
								destructured={false}
								exact
								path={Routes.ASSETS_FORMS}
							/>

							<BundleRouter
								data={WebContentList}
								destructured={false}
								exact
								path={Routes.ASSETS_WEB_CONTENT}
							/>

							<RouteNotFound />
						</Switch>
					</Suspense>
				</BasePage.Context.Provider>
			</BasePage.Body>
		</BasePage>
	);
}

AssetsRoutes.propTypes = {
	/**
	 * @type {object}
	 * @default undefined
	 */
	router: PropTypes.object
};

export default props => (
	<WrappedPageComponent {...props} Component={AssetsRoutes} />
);
