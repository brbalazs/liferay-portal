import * as time from '../time';
import {
	LAST_24_HOURS,
	LAST_28_DAYS,
	LAST_30_DAYS,
	LAST_7_DAYS,
	LAST_90_DAYS,
	YESTERDAY
} from 'shared/util/constants';

describe('time', () => {
	describe('formatDuration', () => {
		it.each`
			milliseconds | unit            | retVal
			${3600000}   | ${time.SECONDS} | ${3600}
			${3600000}   | ${time.MINUTES} | ${60}
			${3600000}   | ${time.HOURS}   | ${1}
		`('format $milliseconds to $unit', ({milliseconds, retVal, unit}) => {
			expect(time.formatDuration(milliseconds, unit)).toBe(retVal);
		});
	});

	describe('hasRemainder', () => {
		it.each`
			milliseconds | unit            | retVal
			${3600000}   | ${time.SECONDS} | ${false}
			${3600001}   | ${time.SECONDS} | ${true}
			${3600000}   | ${time.MINUTES} | ${false}
			${3600001}   | ${time.MINUTES} | ${true}
			${3600000}   | ${time.HOURS}   | ${false}
			${3600001}   | ${time.HOURS}   | ${true}
		`(
			'return $retVal when checking for remainders in $unit unit',
			({milliseconds, retVal, unit}) => {
				expect(time.hasRemainder(milliseconds, unit)).toBe(retVal);
			}
		);
	});

	describe('isHourlyRangeKey', () => {
		it('should return true if the rangeKey is hourly', () => {
			expect(time.isHourlyRangeKey(YESTERDAY)).toBe(true);
			expect(time.isHourlyRangeKey(LAST_24_HOURS)).toBe(true);
		});

		it('should return false if the rangeKey is not hourly', () => {
			expect(time.isHourlyRangeKey(LAST_30_DAYS)).toBe(false);
		});
	});

	describe('isMonthlyRangeKey', () => {
		it('should return true if the rangeKey is monthly', () => {
			expect(time.isMonthlyRangeKey(LAST_28_DAYS)).toBe(true);
			expect(time.isMonthlyRangeKey(LAST_30_DAYS)).toBe(true);
			expect(time.isMonthlyRangeKey(LAST_90_DAYS)).toBe(true);
		});

		it('should return false if the rangeKey is not monthly', () => {
			expect(time.isMonthlyRangeKey(LAST_24_HOURS)).toBe(false);
			expect(time.isMonthlyRangeKey(LAST_7_DAYS)).toBe(false);
		});
	});

	describe('getMilliseconds', () => {
		it.each`
			duration | unit            | retVal
			${3600}  | ${time.SECONDS} | ${3600000}
			${60}    | ${time.MINUTES} | ${3600000}
			${1}     | ${time.HOURS}   | ${3600000}
		`('format $unit to milliseconds', ({duration, retVal, unit}) => {
			expect(time.getMilliseconds(duration, unit)).toBe(retVal);
		});
	});

	describe('getMillisecondsFromTime', () => {
		it.each`
			value         | retVal
			${'01:00:00'} | ${3600000}
			${'00:01:00'} | ${60000}
			${'00:00:01'} | ${1000}
		`('format $time to milliseconds', ({retVal, value}) => {
			expect(time.getMillisecondsFromTime(value)).toBe(retVal);
		});
	});

	describe('getLargestNaturalUnit', () => {
		it.each`
			milliseconds | unit            | retVal
			${16000}     | ${time.HOURS}   | ${time.SECONDS}
			${16000}     | ${time.MINUTES} | ${time.SECONDS}
			${16000}     | ${time.SECONDS} | ${time.SECONDS}
			${1800000}   | ${time.HOURS}   | ${time.MINUTES}
			${1800000}   | ${time.MINUTES} | ${time.MINUTES}
			${1800000}   | ${time.SECONDS} | ${time.SECONDS}
			${3600000}   | ${time.HOURS}   | ${time.HOURS}
			${1800000}   | ${time.MINUTES} | ${time.MINUTES}
			${1800000}   | ${time.SECONDS} | ${time.SECONDS}
		`(
			'return the largest natural unit start at $unit for $milliseconds milliseconds',
			({milliseconds, retVal, unit}) => {
				expect(time.getLargestNaturalUnit(milliseconds, unit)).toBe(
					retVal
				);
			}
		);
	});

	describe('getUnitLabel', () => {
		it.each`
			unit            | label
			${time.SECONDS} | ${time.UNITS[0]}
			${time.MINUTES} | ${time.UNITS[1]}
			${time.HOURS}   | ${time.UNITS[2]}
		`('return $label for $unit', ({label, unit}) => {
			expect(time.getUnitLabel(unit)).toBe(label);
		});
	});

	describe('getRemainder', () => {
		it.each`
			milliseconds | unit            | retVal
			${3600000}   | ${time.SECONDS} | ${0}
			${3600001}   | ${time.SECONDS} | ${1}
			${3600000}   | ${time.MINUTES} | ${0}
			${3600001}   | ${time.MINUTES} | ${1}
			${3600000}   | ${time.HOURS}   | ${0}
			${3600001}   | ${time.HOURS}   | ${1}
		`(
			'return $retVal when checking for remainders in $unit unit',
			({milliseconds, retVal, unit}) => {
				expect(time.getRemainder(milliseconds, unit)).toBe(retVal);
			}
		);
	});

	describe('formatTime', () => {
		it.each`
			milliseconds  | retVal
			${3600000}    | ${'01:00:00'}
			${3600}       | ${'00:00:04'}
			${1800000}    | ${'00:30:00'}
			${1801000}    | ${'00:30:01'}
			${1231231230} | ${'342:00:31'}
		`(
			'return $retVal when formatting $milliseconds to duration time',
			({milliseconds, retVal}) => {
				expect(time.formatTime(milliseconds)).toBe(retVal);
			}
		);
	});

	describe('formatTimezoneOffset', () => {
		it.each`
			offset    | retVal
			${0}      | ${'+00:00'}
			${-7}     | ${'+07:00'}
			${-10.25} | ${'+10:15'}
			${-10.5}  | ${'+10:30'}
			${-10.75} | ${'+10:45'}
			${7}      | ${'-07:00'}
			${10.25}  | ${'-10:15'}
			${10.5}   | ${'-10:30'}
			${10.75}  | ${'-10:45'}
			${12}     | ${'-12:00'}
			${-12}    | ${'+12:00'}
		`(
			'return $retVal when formatting $offset to GMT format',
			({offset, retVal}) => {
				expect(time.formatTimezoneOffset(offset)).toBe(retVal);
			}
		);
	});
});
