import * as API from 'shared/api';
import React from 'react';
import TimeZoneAlert from './TimeZoneAlert';
import {
	NotificationSubtype,
	NotificationType
} from 'shared/util/records/Notification';
import {useRequest} from 'shared/hooks';

const timeZoneAlertTransformer = (
	groupId: string,
	notificationId: string,
	stripe: boolean
) => (
	<TimeZoneAlert
		groupId={groupId}
		key={notificationId}
		notificationId={notificationId}
		stripe={stripe}
	/>
);

const notificationStrategies = new Map<string, Function>([
	[NotificationSubtype.TIME_ZONE_CHANGED, timeZoneAlertTransformer]
]);

interface INotificationAlertListProps {
	groupId: string;
	stripe: boolean;
}

const NotificationAlertList: React.FC<INotificationAlertListProps> = ({
	groupId,
	stripe = false
}) => {
	const {data, loading} = useRequest(API.notifications.fetchNotifications, {
		groupId,
		type: NotificationType.ALERT
	});

	const notifications =
		loading || !data
			? []
			: data.map(notification => {
					const transformer = notificationStrategies.get(
						notification.subtype
					);

					return transformer
						? transformer(groupId, notification.id, stripe)
						: null;
			  });

	return <>{...notifications}</>;
};

export default NotificationAlertList;
