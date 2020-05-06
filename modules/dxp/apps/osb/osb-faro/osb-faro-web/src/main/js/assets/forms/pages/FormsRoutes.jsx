import * as breadcrumbs from 'shared/util/breadcrumbs';
import BasePage from 'shared/components/base-page';
import BundleRouter from 'route-middleware/BundleRouter';
import Filter from '../hocs/Filter';
import getCN from 'classnames';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense, useState} from 'react';
import RouteNotFound from 'shared/components/RouteNotFound';
import WrappedPageComponent from 'cerebro-shared/hocs/WrappedPageComponent';
import {get} from 'lodash';
import {PropTypes} from 'prop-types';
import {Routes} from 'shared/util/router';
import {Switch} from 'react-router-dom';
import {useChannelContext} from 'shared/context/channel';

const FormsDashboardPage = lazy(() =>
	import(/* webpackChunkName: "FormsDashboardPage" */ './FormsDashboardPage')
);
const FormsKnownIndividualsPage = lazy(() =>
	import(
		/* webpackChunkName: "FormsKnownIndividualsPage" */ './FormsKnownIndividualsPage'
	)
);

function FormsRoutes({className, rangeKey, router}) {
	const {assetId, channelId, groupId, title, touchpoint} = router.params;

	const [filters, setFilters] = useState({});

	const decodedTitle = decodeURIComponent(title);

	const rangeKeyFromQuery = get(router, ['query', 'rangeKey']);

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
					}),
					breadcrumbs.getAssets({channelId, groupId}),
					breadcrumbs.getForms({channelId, groupId}),
					breadcrumbs.getEntityName({label: decodedTitle})
				]}
			>
				<BasePage.Header.TitleSection title={decodedTitle} />

				<BasePage.Header.NavBar
					items={[
						{
							exact: true,
							label: Liferay.Language.get('overview'),
							route: Routes.ASSETS_FORMS_DASHBOARD
						},
						{
							exact: true,
							label: Liferay.Language.get('known-individuals'),
							route: Routes.ASSETS_FORMS_KNOWN_INDIVIDUALS
						}
					]}
					routeParams={{
						assetId,
						channelId,
						groupId,
						title,
						touchpoint
					}}
					routeQueries={{rangeKey: rangeKeyFromQuery}}
				/>
			</BasePage.Header>

			<BasePage.Context.Provider value={{filters, rangeKey, router}}>
				<BasePage.SubHeader>
					<Filter onChange={setFilters} />
				</BasePage.SubHeader>

				<BasePage.Body>
					<Suspense fallback={<Loading />}>
						<Switch>
							<BundleRouter
								data={FormsDashboardPage}
								destructured={false}
								exact
								path={Routes.ASSETS_FORMS_DASHBOARD}
							/>

							<BundleRouter
								data={FormsKnownIndividualsPage}
								destructured={false}
								exact
								path={Routes.ASSETS_FORMS_KNOWN_INDIVIDUALS}
							/>

							<RouteNotFound />
						</Switch>
					</Suspense>
				</BasePage.Body>
			</BasePage.Context.Provider>
		</BasePage>
	);
}

FormsRoutes.propTypes = {
	rangeKey: PropTypes.shape({
		defaultValue: PropTypes.string,
		lastValue: PropTypes.string
	}),

	/**
	 * @type {object}
	 * @default undefined
	 */
	router: PropTypes.object,

	/**
	 * @type {string}
	 * @default undefined
	 */
	title: PropTypes.string
};

export default props => (
	<WrappedPageComponent {...props} Component={FormsRoutes} />
);
