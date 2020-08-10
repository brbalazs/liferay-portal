import BundleRouter from 'route-middleware/BundleRouter';
import Loading from 'shared/pages/Loading';
import React, {lazy, Suspense} from 'react';
import RouteNotFound from 'shared/components/RouteNotFound';
import {DEVELOPER_MODE} from 'shared/util/constants';
import {Routes} from 'shared/util/router';
import {Switch} from 'react-router-dom';

const Overview = lazy(() =>
	import(/* webpackChunkName: "DefinitionsOverview" */ './Overview')
);

const IndividualAttributes = lazy(() =>
	import(
		/* webpackChunkName: "DefinitionsIndividualAttributes" */ './IndividualAttributes'
	)
);

const InterestTopics = lazy(() =>
	import(
		/* webpackChunkName: "DefinitionsInterestTopics" */ './InterestTopics'
	)
);

const TrackedBehaviors = lazy(() =>
	import(/* webpackChunkName: "TrackedBehaviors" */ './TrackedBehaviors')
);

const Search = lazy(() => import(/* webpackChunkName: "Search" */ './Search'));

interface IDefinitionsProps extends React.HTMLAttributes<HTMLDivElement> {}

const Definitions: React.FC<IDefinitionsProps> = () => (
	<Suspense fallback={<Loading />}>
		<Switch>
			<BundleRouter
				data={Overview}
				exact
				path={Routes.SETTINGS_DEFINITIONS}
			/>

			<BundleRouter
				data={InterestTopics}
				exact
				path={Routes.SETTINGS_DEFINITIONS_INTEREST_TOPICS}
			/>

			<BundleRouter
				data={IndividualAttributes}
				exact
				path={Routes.SETTINGS_DEFINITIONS_INDIVIDUAL_ATTRIBUTES}
			/>

			{DEVELOPER_MODE && (
				<Switch>
					<BundleRouter
						data={TrackedBehaviors}
						path={Routes.SETTINGS_DEFINITIONS_BEHAVIORS}
					/>
					<BundleRouter
						data={Search}
						path={Routes.SETTINGS_DEFINITIONS_SEARCH}
					/>
				</Switch>
			)}
			<RouteNotFound />
		</Switch>
	</Suspense>
);

export default Definitions;
