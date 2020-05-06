import FaroConstants from 'shared/util/constants';
import moment from 'moment';
import sendRequest from 'shared/util/request';
import {TITLE} from 'shared/util/pagination';

const {
	pagination: {cur: DEFAULT_PAGE, delta: DEFAULT_DELTA, orderDescending},
	timeIntervals
} = FaroConstants;

export const INTERVALS_MAP = {
	[timeIntervals.day]: 1,
	[timeIntervals.month]: 30,
	[timeIntervals.week]: 7
};

export function search(params) {
	const {
		active = true,
		contactsEntityId,
		contactsEntityType,
		delta = DEFAULT_DELTA,
		groupId,
		interestName,
		interval,
		intervalInitDate,
		orderByFields = [
			{fieldName: TITLE, orderBy: orderDescending, system: true}
		],
		page = DEFAULT_PAGE,
		query = ''
	} = params;

	return sendRequest({
		data: {
			active,
			contactsEntityId,
			contactsEntityType,
			cur: page,
			delta,
			endDate: moment(intervalInitDate)
				.add(INTERVALS_MAP[interval] - 1, 'days')
				.format('x'),
			interestName,
			orderByFields,
			query,
			startDate: intervalInitDate
		},
		method: 'GET',
		path: `contacts/${groupId}/pages_visited`
	});
}
