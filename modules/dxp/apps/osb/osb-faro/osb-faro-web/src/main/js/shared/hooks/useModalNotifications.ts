import * as API from 'shared/api';
import Notification, {
	NotificationSubtype,
	NotificationType
} from 'shared/util/records/Notification';
import {Modal} from 'shared/types';
import {modalTypes} from 'shared/actions/modals';
import {useEffect} from 'react';

interface ITimeZoneAdminModalRenderProps {
	close: Modal.close;
	groupId: string;
	notificationId: string;
	open: Modal.open;
}

const renderTimeZoneAdminModal = (
	modalType: Modal.modalTypes,
	{close, groupId, notificationId, open}
) =>
	open(modalType, {
		groupId,
		notificationId,
		onClose: close
	});

const modalNotificationStrategies = new Map<string, Function>([
	[
		NotificationSubtype.TIME_ZONE_ADMIN,
		(params: ITimeZoneAdminModalRenderProps) =>
			renderTimeZoneAdminModal(
				modalTypes.TIME_ZONE_SELECTION_MODAL,
				params
			)
	]
]);

function useModalNotifications(
	close: Modal.close,
	groupId: string,
	open: Modal.open
): void {
	const handleRender = (notificationList: Array<Notification>): void => {
		const notificationToRender = notificationList.pop();

		if (notificationToRender) {
			const renderFn = modalNotificationStrategies.get(
				notificationToRender.subtype
			);

			const onClose = (notificationList: Array<Notification>) => {
				handleRender(notificationList);
				close();
			};

			renderFn({
				close: () => onClose(notificationList),
				groupId,
				notificationId: notificationToRender.id,
				open
			});
		}
	};

	useEffect(() => {
		API.notifications
			.fetchNotifications({
				groupId,
				type: NotificationType.MODAL
			})
			.then(handleRender);
	}, []);
}

export default useModalNotifications;
