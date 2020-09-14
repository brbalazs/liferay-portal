import * as data from 'test/data';
import moment from 'moment';
import {
	createDateKeysIMap,
	getByCustomRangeKey,
	getByEvenOrOddIndexes,
	getByIndexesMultipleOfFour,
	getByIndexesMultipleOfSix,
	getDayIntervalsMap,
	getFirstAndFifteenthsDays,
	getFirstDays,
	getIntervalHandle,
	getIntervalsFromMap,
	getNextFirst,
	getNextFirstOrFifteenth,
	getNextSunday,
	getSundays,
	getWeekIntervalsMap,
	handleDayInterval
} from '../intervals';
import {
	CUSTOM_RANGE,
	LAST_180_DAYS,
	LAST_24_HOURS,
	LAST_28_DAYS,
	LAST_30_DAYS,
	LAST_7_DAYS,
	LAST_90_DAYS,
	LAST_YEAR,
	YESTERDAY
} from 'shared/util/constants';
import {getDate} from 'shared/util/date';
import {INTERVAL_KEY_MAP} from 'shared/util/time';
import {Map} from 'immutable';

const currentDate = getDate();
const mockDate = getDate('2020-06-12'); // Friday

const mockData = [
	{
		intervalInitDate: data.getTimestamp(0),
		scoreAvg: 2,
		totalElements: 5
	},
	{
		intervalInitDate: data.getTimestamp(1),
		scoreAvg: 4,
		totalElements: 10
	}
];

describe('createDateKeysIMap', () => {
	it('should create an dateKeysIMap', () => {
		const dateKeysIMap = createDateKeysIMap('D', mockData);

		expect(dateKeysIMap).toBeInstanceOf(Map);
	});

	it('should create an dateKeysIMap with two date when interval is week', () => {
		const dateKeysIMap = createDateKeysIMap(
			'W',
			mockData,
			'intervalInitDate'
		);

		const dates = dateKeysIMap.get(data.getTimestamp(0));

		expect(dates[0]).toBeNumber();
		expect(dates[1]).toBeNumber();
	});
});

describe('getNext Functions', () => {
	it('should get next sunday from a given date', () => {
		const nextSunday = getNextSunday(currentDate);

		expect(nextSunday.getUTCDay()).toEqual(0);
	});

	it('should get next first day of a month from a given date', () => {
		const nextFirst = getNextFirst(currentDate);

		expect(nextFirst.getUTCDate()).toEqual(1);
	});

	it('should get next first or fifteenth day of a month from a given date', () => {
		const mockToFifteenthDate = getDate('2020-06-12');
		const mockToFirstDate = getDate('2020-06-16');

		const nextFirst = getNextFirstOrFifteenth(mockToFirstDate);
		const nextFifteenth = getNextFirstOrFifteenth(mockToFifteenthDate);

		expect(nextFifteenth.getUTCDate()).toEqual(15);
		expect(nextFirst.getUTCDate()).toEqual(1);
	});
});

describe('getDates functions', () => {
	const dates = [];

	for (let i = 1; i < 30; i++) {
		const date = getDate(mockDate.getTime() - i * 8.64e7);

		dates.push(date);
	}

	dates.reverse();

	it('get Sundays from a given date array', () => {
		const sundays = getSundays(dates);

		expect(sundays[0].getUTCDay()).toEqual(0);
		expect(sundays[1].getUTCDay()).toEqual(0);
		expect(sundays[2].getUTCDay()).toEqual(0);
	});

	it('get first days of each month from a given date array', () => {
		const firstDays = getFirstDays(dates);

		expect(firstDays[0].getUTCDate()).toEqual(1);
	});

	it('get first or fifteenth days of each month from a given date array', () => {
		const firstOrFifteenthDays = getFirstAndFifteenthsDays(dates);

		expect(firstOrFifteenthDays[0].getUTCDate()).toEqual(15);
		expect(firstOrFifteenthDays[1].getUTCDate()).toEqual(1);
	});

	it('get by even or odd indexes', () => {
		// even array
		const evenIndexes = getByEvenOrOddIndexes(dates.slice(0, 9));

		expect(evenIndexes).toEqual([
			dates[0],
			dates[2],
			dates[4],
			dates[6],
			dates[8]
		]);

		// odd array should include first item
		const oddIndexes = getByEvenOrOddIndexes(dates.slice(0, 8));

		expect(oddIndexes).toEqual([
			dates[0],
			dates[1],
			dates[3],
			dates[5],
			dates[7]
		]);
	});

	it('get by indexes multiple of four', () => {
		const multiplesOfFourIndexes = getByIndexesMultipleOfFour(
			dates.slice(0, 13)
		);

		expect(multiplesOfFourIndexes).toEqual([
			dates[0],
			dates[4],
			dates[8],
			dates[12]
		]);
	});

	it('get by indexes multiple of six', () => {
		const multiplesOfSixIndexes = getByIndexesMultipleOfSix(
			dates.slice(0, 24)
		);

		expect(multiplesOfSixIndexes).toEqual([
			dates[0],
			dates[6],
			dates[12],
			dates[18],
			dates[23]
		]);
	});
});

describe('get functions to handle custom range key', () => {
	it('should return undefined if the duration is less than 14 days', () => {
		const handleFn = getByCustomRangeKey(13, INTERVAL_KEY_MAP.day);

		expect(handleFn).toEqual(undefined);
	});

	it('should return getSundays with day interval if the duration is more or equal 14 and less or equal 30 days', () => {
		const handleFn = getByCustomRangeKey(30, INTERVAL_KEY_MAP.day);

		expect(handleFn).toBe(getSundays);
	});

	it('should return getFirstAndFifteenthsDays with day interval if the duration is more than 30 and less or equal 180 days', () => {
		const handleFn = getByCustomRangeKey(180, INTERVAL_KEY_MAP.day);

		expect(handleFn).toBe(getFirstAndFifteenthsDays);
	});

	it('should return getFirstDays with day interval if the duration is more than 180', () => {
		const handleFn = getByCustomRangeKey(365, INTERVAL_KEY_MAP.day);

		expect(handleFn).toBe(getFirstDays);
	});

	it('should return getByEvenOrOddIndexes with week interval if the duration is more than 30 and less or equal 180 days', () => {
		const handleFn = getByCustomRangeKey(180, INTERVAL_KEY_MAP.week);

		expect(handleFn).toBe(getByEvenOrOddIndexes);
	});

	it('should return getByIndexesMultipleOfFour with day interval if the duration is more than 180', () => {
		const handleFn = getByCustomRangeKey(365, INTERVAL_KEY_MAP.week);

		expect(handleFn).toBe(getByIndexesMultipleOfFour);
	});
});

describe('getIntervalHandle functions', () => {
	it('should return undefined if a interval or rangeKey is not mapped', () => {
		let handleFn = getIntervalHandle(
			LAST_30_DAYS,
			[],
			INTERVAL_KEY_MAP.month
		);

		expect(handleFn).toEqual(undefined);

		handleFn = getIntervalHandle(LAST_7_DAYS, [], INTERVAL_KEY_MAP.day);

		expect(handleFn).toEqual(undefined);
	});

	it('should return a map object when a interval is mapped', () => {
		const intervalMaps = getIntervalsFromMap(30);

		expect(intervalMaps).toHaveProperty(INTERVAL_KEY_MAP.day);
		expect(intervalMaps).toHaveProperty(INTERVAL_KEY_MAP.week);

		expect(intervalMaps[INTERVAL_KEY_MAP.day]).toHaveProperty(CUSTOM_RANGE);
		expect(intervalMaps[INTERVAL_KEY_MAP.week]).toHaveProperty(
			CUSTOM_RANGE
		);
	});

	it('should return a map object with rangeKeys which have handle functions in day interval', () => {
		const dayIntervalMap = getDayIntervalsMap(30);

		expect(dayIntervalMap).toHaveProperty(CUSTOM_RANGE);
		expect(dayIntervalMap).toHaveProperty(LAST_180_DAYS);
		expect(dayIntervalMap).toHaveProperty(LAST_24_HOURS);
		expect(dayIntervalMap).toHaveProperty(LAST_28_DAYS);
		expect(dayIntervalMap).toHaveProperty(LAST_30_DAYS);
		expect(dayIntervalMap).toHaveProperty(LAST_90_DAYS);
		expect(dayIntervalMap).toHaveProperty(LAST_YEAR);
		expect(dayIntervalMap).toHaveProperty(YESTERDAY);
	});

	it('should return a map object with rangeKeys which have handle functions in week interval', () => {
		const weekIntervalMap = getWeekIntervalsMap(30);

		expect(weekIntervalMap).toHaveProperty(CUSTOM_RANGE);
		expect(weekIntervalMap).toHaveProperty(LAST_180_DAYS);
		expect(weekIntervalMap).toHaveProperty(LAST_90_DAYS);
		expect(weekIntervalMap).toHaveProperty(LAST_YEAR);
	});
});

describe('handleDayInterval', () => {
	it('should extract an array of dates from a start and end date using the handleFn argument as step', () => {
		// function to step two days
		const handleFn = date =>
			moment(date)
				.add(2, 'days')
				.toDate();
		const lastDate = getDate('2020-06-18');

		const intervals = handleDayInterval(handleFn, mockDate, lastDate);

		expect(intervals).toEqual([
			getDate('2020-06-12'),
			getDate('2020-06-14'),
			getDate('2020-06-16'),
			getDate('2020-06-18')
		]);
	});
});
