import BundleRouter from 'route-middleware/BundleRouter';
import Card from 'shared/components/Card';
import Loading from 'shared/pages/Loading';
import Nav from 'shared/components/Nav';
import React, {lazy, Suspense} from 'react';
import {getMatchedRoute, Routes, toRoute} from 'shared/util/router';
import {Switch} from 'react-router';

const AttributeList = lazy(() =>
	import(/* webpackChunkName: "AttributeList" */ './AttributeList')
);

const EventList = lazy(() =>
	import(/* webpackChunkName: "EventList" */ './EventList')
);

const NAV_ITEMS = [
	{
		exact: true,
		label: Liferay.Language.get('default-events'),
		route: Routes.SETTINGS_DEFINITIONS_EVENTS_DEFAULT
	},
	{
		exact: true,
		label: Liferay.Language.get('custom-events'),
		route: Routes.SETTINGS_DEFINITIONS_EVENTS_CUSTOM
	},
	{
		exact: true,
		label: Liferay.Language.get('attributes'),
		route: Routes.SETTINGS_DEFINITIONS_EVENTS_ATTRIBUTES
	}
];

interface ITabsCardProps {
	groupId: string;
}

const TabsCard: React.FC<ITabsCardProps> = ({groupId}) => {
	const matchedRoute = getMatchedRoute(NAV_ITEMS);

	return (
		<Card key='cardContainer' pageDisplay>
			<Nav className='page-subnav mx-4 my-3' display='underline'>
				{NAV_ITEMS.map(({label, route}) => (
					<Nav.Item
						active={matchedRoute === route}
						href={toRoute(route, {groupId})}
						key={route}
					>
						<div className='mb-2'>
							<b>{label}</b>
						</div>
					</Nav.Item>
				))}
			</Nav>

			<Suspense fallback={<Loading />}>
				<Switch>
					<BundleRouter
						data={EventList}
						exact
						path={Routes.SETTINGS_DEFINITIONS_EVENTS_DEFAULT}
					/>

					<BundleRouter
						componentProps={{customEvent: true}}
						data={EventList}
						exact
						path={Routes.SETTINGS_DEFINITIONS_EVENTS_CUSTOM}
					/>

					<BundleRouter
						data={AttributeList}
						exact
						path={Routes.SETTINGS_DEFINITIONS_EVENTS_ATTRIBUTES}
					/>
				</Switch>
			</Suspense>
		</Card>
	);
};

export default TabsCard;
