import BasePage from 'settings/components/BasePage';
import Card from 'shared/components/Card';
import Nav from 'shared/components/Nav';
import React from 'react';
import {getDefinitions} from 'shared/util/breadcrumbs';
import {getMatchedRoute, Routes, toRoute} from 'shared/util/router';

interface IEventsProps {
	groupId: string;
}

const Events: React.FC<IEventsProps> = ({groupId}) => {
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

	const matchedRoute = getMatchedRoute(NAV_ITEMS);

	return (
		<BasePage
			breadcrumbItems={[
				getDefinitions({groupId}),
				{active: true, label: Liferay.Language.get('events')}
			]}
			groupId={groupId}
			pageDescription={Liferay.Language.get(
				'this-is-the-data-model-of-events-sent-to-analytics-cloud'
			)}
			pageTitle={Liferay.Language.get('events')}
		>
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
			</Card>
		</BasePage>
	);
};

export default Events;
