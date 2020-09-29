import {Record} from 'immutable';

interface ITimezone {
	displayTimeZone: string;
	timeZoneId: string;
}

export default class TimeZone
	extends Record({
		displayTimeZone: '(UTC) UTC',
		timeZoneId: 'UTC'
	})
	implements ITimezone {
	displayTimeZone: string;
	timeZoneId: string;

	constructor() {
		super();
	}
}
