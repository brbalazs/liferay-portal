import {Record} from 'immutable';

export enum NotificationTypes {
	Alert = 'ALERT',
	Modal = 'MODAL'
}

export enum NotificationSubtypes {
	CustomEventDefinitionLimitReached = 'CUSTOM_EVENT_DEFINITION_LIMIT_REACHED',
	TimeZoneAdmin = 'TIME_ZONE_ADMIN',
	TimeZoneChanged = 'TIME_ZONE_CHANGED'
}

interface INotification {
	id: string;
	subtype: NotificationSubtypes;
	type: NotificationTypes;
}

export default class Notification
	extends Record({
		id: '',
		subtype: null,
		type: null
	})
	implements INotification {
	id: string;
	subtype: NotificationSubtypes;
	type: NotificationTypes;

	constructor(props = {}) {
		super(props);
	}
}
