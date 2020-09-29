import {Record} from 'immutable';

interface ITimeZone {
	displayTimeZone: string;
	timeZoneId: string;
}

export default class TimeZone
	extends Record({
		displayTimeZone: '(UTC) UTC',
		timeZoneId: 'UTC'
	})
	implements ITimeZone {
	displayTimeZone: string;
	timeZoneId: string;

	constructor() {
		super();
	}
}
