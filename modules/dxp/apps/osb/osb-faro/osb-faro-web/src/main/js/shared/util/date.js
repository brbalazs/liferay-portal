import moment from 'moment';
import {flow, get, head, last, rangeRight} from 'lodash/fp';

export const FORMAT = 'YYYY-MM-DD';

export const DATE_MASK = [
	/\d/,
	/\d/,
	/\d/,
	/\d/,
	'-',
	/\d/,
	/\d/,
	'-',
	/\d/,
	/\d/
];

export const DATE_TIME_MASK = [
	/\d/,
	/\d/,
	/\d/,
	/\d/,
	'-',
	/\d/,
	/\d/,
	'-',
	/\d/,
	/\d/,
	' ',
	/\d/,
	/\d/,
	':',
	/\d/,
	/\d/
];

export const WEEKDAYS = [
	Liferay.Language.get('sunday'),
	Liferay.Language.get('monday'),
	Liferay.Language.get('tuesday'),
	Liferay.Language.get('wednesday'),
	Liferay.Language.get('thursday'),
	Liferay.Language.get('friday'),
	Liferay.Language.get('saturday')
];

/**
 * Formats unix timestamp to specified moment format
 * @param {number|string|Date} date
 * @param {string|moment.MomentBuiltinFormat} format
 * @param {string|moment.MomentBuiltinFormat} [inputFormatter]
 * @return {string} formatted date
 */
export function formatUTCDate(date, format = 'LL', inputFormatter) {
	return moment.utc(date, inputFormatter).format(format);
}

export const formatUTCDateFromUnix = (date, format = 'LL') =>
	formatUTCDate(date, format, 'x');

export function generateDateRange(period = 30, interval = 'days') {
	return rangeRight(0, period).map(cur =>
		moment
			.utc()
			.startOf(interval)
			.subtract(cur, interval)
			.valueOf()
	);
}

/**
 * Get Date
 * @param {string | number} [date]
 */
export const getDate = date => moment.utc(date).toDate();

/**
 * Get ISO Date
 * @param {string} date
 */
export const getISODate = date => moment.utc(date).toISOString();

/**
 * Get Date now.
 * @returns {Moment} Date at time of calling.
 */
export const getDateNow = () => moment.utc();

export function getDateRangeLabel(dates, key) {
	const firstDate = flow(
		head,
		get(key),
		formatUTCDate
	)(dates);
	const lastDate = flow(
		last,
		get(key),
		formatUTCDate
	)(dates);

	return `${firstDate} - ${lastDate}`;
}

/**
 *  Gets the first date of the array.
 *  @param {Array.<Aggregation>} aggregations - Array of objects.
 *  @returns {number} Date in unix time.
 */
export function getFirstDate(dates, key) {
	return flow(
		head,
		get(key)
	)(dates);
}

/**
 *  Gets the last date of the array.
 *  @param {Array.<Aggregation>} aggregations - Array of objects.
 *  @returns {number} Date in unix time.
 */
export function getLastDate(dates, key) {
	return flow(
		last,
		get(key)
	)(dates);
}

/**
 * Get total days to date
 * @param {object} date
 */
export const getTotalDaysToDate = createDate => {
	const duration = moment.duration({
		from: moment(createDate).clone(),
		to: new Date()
	});

	return Math.floor(duration.asDays());
};

export function toUnix(stringOrMoment) {
	return moment(stringOrMoment, FORMAT).unix() * 1000 || null;
}
