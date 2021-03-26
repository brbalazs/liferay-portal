import * as API from 'shared/api';
import Button from 'shared/components/Button';
import EmbeddedAlertList from 'shared/components/EmbeddedAlertList';
import React from 'react';
import TimeZoneAlert from './TimeZoneAlert';
import {
	NotificationSubtype,
	NotificationType
} from 'shared/util/records/Notification';
import {Routes, toRoute} from 'shared/util/router';
import {useRequest} from 'shared/hooks';

const notificationStrategies = new Map<string, Function>([
	[
		NotificationSubtype.TIME_ZONE_CHANGED,
		(
			groupId: string,
			notificationId: string,
			stripe: boolean,
			onClose
		) => ({
			customComponent: () => (
				<TimeZoneAlert
					groupId={groupId}
					key={notificationId}
					onClose={() => onClose(notificationId)}
					stripe={stripe}
				/>
			)
		})
	],
	[
		NotificationSubtype.CUSTOM_EVENT_LIMIT_REACHED,
		(groupId, notificationId, stripe, onClose) => ({
			alertType: 'warning',
			className: 'd-flex align-items-center',
			id: notificationId,
			key: notificationId,
			message: (
				<>
					<span>
						{Liferay.Language.get(
							'100-event-limit-reached,-resulting-in-blocked-events'
						)}
					</span>

					<Button
						className='py-0'
						display='link'
						href={toRoute(
							Routes.SETTINGS_DEFINITIONS_EVENTS_BLOCK_LIST,
							{groupId}
						)}
						size='sm'
					>
						{Liferay.Language.get('view-block-list')}
					</Button>
				</>
			),
			onClose,
			stripe,
			title: Liferay.Language.get('limit-reached')
		})
	]
]);

interface INotificationAlertListProps {
	groupId: string;
	stripe?: boolean;
}

const NotificationAlertList: React.FC<INotificationAlertListProps> = ({
	groupId,
	stripe = false
}) => {
	const {data, loading, refetch} = useRequest(
		API.notifications.fetchNotifications,
		{
			groupId,
			type: NotificationType.ALERT
		}
	);

	const removeNotification = notificationId => {
		API.notifications.readNotification(groupId, notificationId).then(() => {
			refetch();
		});
	};

	const notifications =
		loading || !data
			? []
			: data.map(({id, subtype}) => {
					const transformer = notificationStrategies.get(subtype);

					if (transformer) {
						return transformer(
							groupId,
							id,
							stripe,
							removeNotification
						);
					}
			  });

	return <EmbeddedAlertList alerts={notifications} />;
};

export default NotificationAlertList;
