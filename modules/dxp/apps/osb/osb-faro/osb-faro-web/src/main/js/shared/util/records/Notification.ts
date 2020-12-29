import {Record} from 'immutable';

interface INotification {
	id: string;
	subtype: string;
}

export default class Notification
	extends Record({
		id: '',
		subtype: ''
	})
	implements INotification {
	id: string;
	subtype: string;

	constructor(props = {}) {
		super(props);
	}
}
