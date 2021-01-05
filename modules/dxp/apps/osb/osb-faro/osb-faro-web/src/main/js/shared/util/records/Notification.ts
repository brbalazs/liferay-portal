import {Record} from 'immutable';

export enum NotificationType {
	ALERT = 'ALERT',
	MODAL = 'MODAL'
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
