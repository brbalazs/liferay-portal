import Constants, {
	CUSTOM_RANGE,
	LAST_180_DAYS,
	LAST_24_HOURS,
	LAST_28_DAYS,
	LAST_30_DAYS,
	LAST_90_DAYS,
	LAST_YEAR,
	YESTERDAY
} from 'shared/util/constants';
import {Interval} from 'shared/types';

const {timeIntervals} = Constants;

export const INTERVAL_KEY_MAP: {[s: string]: Interval} = {
	[timeIntervals.day]: 'D',
	[timeIntervals.month]: 'M',
	[timeIntervals.week]: 'W'
};

export const UNITS: string[] = [
	Liferay.Language.get('seconds'),
	Liferay.Language.get('minutes'),
	Liferay.Language.get('hours')
];

export const SECONDS: number = 0;
export const MINUTES: number = 1;
export const HOURS: number = 2;

export function formatDuration(milliseconds: number, unit: number): number {
	switch (unit) {
		case HOURS:
			return milliseconds / (Math.pow(60, 2) * 1000);
		case MINUTES:
			return milliseconds / (60 * 1000);
		case SECONDS:
		default:
			return milliseconds / 1000;
	}
}

export function getRemainder(milliseconds: number, unit: number): number {
	switch (unit) {
		case HOURS:
			return milliseconds % (Math.pow(60, 2) * 1000);
		case MINUTES:
			return milliseconds % (60 * 1000);
		case SECONDS:
		default:
			return milliseconds % 1000;
	}
}

export function hasRemainder(milliseconds: number, unit: number): boolean {
	return !!getRemainder(milliseconds, unit);
}

export function getMilliseconds(value: number, unit: number): number {
	switch (unit) {
		case HOURS:
			return value * Math.pow(60, 2) * 1000;
		case MINUTES:
			return value * 60 * 1000;
		case SECONDS:
		default:
			return value * 1000;
	}
}

export function getLargestNaturalUnit(
	milliseconds: number,
	unit: number = HOURS
) {
	if (hasRemainder(milliseconds, unit) && unit !== 0) {
		return getLargestNaturalUnit(milliseconds, unit - 1);
	}

	return unit;
}

export function formatTimezoneOffset(offset: number): string {
	const sign = Math.sign(offset) > 0 ? '-' : '+';

	const fractionalMinutes = Math.abs(offset % 1);

	const hourFormatted = `${sign}${String(
		Math.abs(Math.trunc(offset))
	).padStart(2, '0')}`;

	if (fractionalMinutes) {
		return `${hourFormatted}:${60 * fractionalMinutes}`;
	}

	return `${hourFormatted}:00`;
}

export function getUnitLabel(unit: number): string {
	return UNITS[unit];
}

export function isHourlyRangeKey(rangeKey: string): boolean {
	return [LAST_24_HOURS, YESTERDAY].includes(rangeKey);
}

export function isMonthlyRangeKey(rangeKey: string): boolean {
	return [
		CUSTOM_RANGE,
		LAST_28_DAYS,
		LAST_30_DAYS,
		LAST_90_DAYS,
		LAST_180_DAYS,
		LAST_YEAR
	].includes(rangeKey);
}

/**
 * Take milliseconds and converts it to duration time value.
 * @returns {string} Time in HH:MM:SS format.
 */
export function formatTime(milliseconds: number): string {
	const timeArray = [HOURS, MINUTES, SECONDS].map(unit => {
		const remainingTime =
			unit === HOURS
				? milliseconds
				: getRemainder(milliseconds, unit + 1);

		let formattedTime = formatDuration(remainingTime, unit);

		if (unit === SECONDS) {
			formattedTime = Math.round(formattedTime);
		} else {
			formattedTime = Math.trunc(formattedTime);
		}

		return String(formattedTime).padStart(2, '0');
	});

	return timeArray.join(':');
}
