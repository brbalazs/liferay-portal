import * as API from 'shared/api';
import React from 'react';
import TimeZoneAlert from './TimeZoneAlert';
import {useRequest} from 'shared/hooks';

enum NotificationSubtype {
	TIME_ZONE_CHANGED = 'TIME_ZONE_CHANGED'
}

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

const notificationStrategy = new Map<string, Function>([
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
		groupId
	});

	const notifications =
		loading || !data
			? []
			: data.map(notification => {
					const transformer = notificationStrategy.get(
						notification.subtype
					);

					return transformer
						? transformer(groupId, notification.id, stripe)
						: null;
			  });

	return <>{...notifications}</>;
};

export default NotificationAlertList;
