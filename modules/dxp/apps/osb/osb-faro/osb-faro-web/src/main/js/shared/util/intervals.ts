import moment from 'moment';
import {
	CUSTOM_RANGE,
	LAST_180_DAYS,
	LAST_24_HOURS,
	LAST_28_DAYS,
	LAST_30_DAYS,
	LAST_90_DAYS,
	LAST_YEAR,
	YESTERDAY
} from 'shared/util/constants';
import {Interval, RangeSelectors} from 'shared/types';
import {INTERVAL_KEY_MAP} from 'shared/util/time';

export const getIntervalHandle = (
	rangeKey: RangeSelectors['rangeKey'],
	arr: Date[],
	timeInterval: Interval
) => {
	const intervalMapsByRangeKey = getIntervalsFromMap(arr.length)[
		timeInterval
	];

	return intervalMapsByRangeKey && intervalMapsByRangeKey[rangeKey];
};

const getIntervalsFromMap = (duration: number) => ({
	[INTERVAL_KEY_MAP.day]: getDayIntervalsMap(duration),
	[INTERVAL_KEY_MAP.week]: getWeekIntervalsMap(duration)
});

const getDayIntervalsMap = (duration: number) => ({
	[CUSTOM_RANGE]: getByCustomRangeKey(duration, INTERVAL_KEY_MAP.day),
	[LAST_180_DAYS]: getFirstAndFifteenthsDays,
	[LAST_24_HOURS]: getByIndexesMultipleOfSix,
	[LAST_28_DAYS]: getSundays,
	[LAST_30_DAYS]: getSundays,
	[LAST_90_DAYS]: getFirstAndFifteenthsDays,
	[LAST_YEAR]: getFirstDays,
	[YESTERDAY]: getByIndexesMultipleOfSix
});

const getWeekIntervalsMap = (duration: number) => ({
	[CUSTOM_RANGE]: getByCustomRangeKey(duration, INTERVAL_KEY_MAP.week),
	[LAST_180_DAYS]: getByEvenOrOddIndexes,
	[LAST_90_DAYS]: getByEvenOrOddIndexes,
	[LAST_YEAR]: getByIndexesMultipleOfFour
});

const handleDayInterval = (
	handleFn: (date: Date) => Date,
	firstTick: Date,
	lastDate: Date
): Date[] => {
	const intervals = [firstTick];
	let lastTick = firstTick;

	while (lastTick < lastDate) {
		lastTick = handleFn(lastTick);

		if (lastTick <= lastDate) {
			intervals.push(lastTick);
		}
	}

	return intervals;
};

const getByEvenOrOddIndexes = (arr: Date[]): Date[] =>
	arr.length % 2 === 0
		? [arr[0], ...arr.filter((_, index) => index % 2 !== 0)]
		: arr.filter((_, index) => index % 2 === 0);

const getByIndexesMultipleOfFour = (arr: Date[]): Date[] =>
	arr.filter((_, index) => index % 4 === 0);

const getByIndexesMultipleOfSix = (arr: Date[]): Date[] => [
	...arr.filter((_, index) => index % 6 === 0),
	arr[arr.length - 1]
];

const getSundays = (arr: Date[]): Date[] => {
	const firstDate = arr[0];
	const lastDate = arr[arr.length - 1];

	const firstTick =
		firstDate.getUTCDay() === 0 ? firstDate : getNextSunday(firstDate);

	return handleDayInterval(getNextSunday, firstTick, lastDate);
};

const getFirstAndFifteenthsDays = (arr: Date[]): Date[] => {
	const firstDate = arr[0];
	const lastDate = arr[arr.length - 1];

	const firstTick =
		firstDate.getUTCDate() === 1 || firstDate.getUTCDate() === 15
			? firstDate
			: getNextFirstOrFifteenth(firstDate);

	return handleDayInterval(getNextFirstOrFifteenth, firstTick, lastDate);
};

const getFirstDays = (arr: Date[]): Date[] => {
	const firstDate = arr[0];
	const lastDate = arr[arr.length - 1];

	const firstTick =
		firstDate.getUTCDate() === 1 ? firstDate : getNextFirst(firstDate);

	return handleDayInterval(getNextFirst, firstTick, lastDate);
};

const getByCustomRangeKey = (duration: number, timeInterval: Interval) => {
	if (timeInterval === INTERVAL_KEY_MAP.day) {
		if (duration >= 14 && duration <= 30) {
			return getSundays;
		} else if (duration > 30 && duration <= 180) {
			return getFirstAndFifteenthsDays;
		} else if (duration > 180) {
			return getFirstDays;
		}
	} else if (timeInterval === INTERVAL_KEY_MAP.week) {
		if (duration > 30 && duration <= 180) {
			return getByEvenOrOddIndexes;
		} else if (duration > 180) {
			return getByIndexesMultipleOfFour;
		}
	}
};

const getNextSunday = (date: Date): Date =>
	moment(date)
		.utc()
		.day(7)
		.startOf('day')
		.toDate();

const getNextFirstOrFifteenth = (date: Date): Date => {
	if (date.getUTCDate() >= 15) {
		return getNextFirst(date);
	}

	return moment(date)
		.utc()
		.date(15)
		.startOf('day')
		.toDate();
};

const getNextFirst = (date: Date): Date =>
	moment(date)
		.utc()
		.endOf('month')
		.add(1)
		.toDate();
