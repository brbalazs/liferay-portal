import {Record} from 'immutable';

export enum NotificationType {
	ALERT = 'ALERT',
	MODAL = 'MODAL'
}

export enum NotificationSubtype {
	TIME_ZONE_CHANGED = 'TIME_ZONE_CHANGED',
	TIME_ZONE_ADMIN = 'TIME_ZONE_ADMIN'
}
interface INotification {
	id: string;
	subtype: string;
	type: NotificationType;
}

export default class Notification
	extends Record({
		id: '',
		subtype: '',
		type: ''
	})
	implements INotification {
	id: string;
	subtype: string;
	type: NotificationType;

	constructor(props = {}) {
		super(props);
	}
}
