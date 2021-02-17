import * as data from 'test/data';
import {
	buildLegendItems,
	formatTickVal,
	getSafeRangeKey
} from '../engagement-activity';

describe('engagement-activity', () => {
	describe('buildLegendItems', () => {
		it('should return an array formatted for use as items in ChangeLegend', () => {
			const mockChangeData = {
				activityChange: 20,
				activityCount: 10,
				engagementChange: 30,
				engagementScore: 4
			};

			expect(buildLegendItems(mockChangeData)).toMatchSnapshot();
		});

		it('should return a fallback display configuration for engagementScore', () => {
			const mockChangeData = {
				activityChange: 20,
				activityCount: 10,
				engagementChange: 30,
				engagementScore: null
			};

			expect(buildLegendItems(mockChangeData)).toMatchSnapshot();
		});
	});

	describe('formatTickVal', () => {
		it('should return a given date with the format M/D', () => {
			const mockDate = data.getTimestamp();

			expect(formatTickVal(mockDate)).toMatchSnapshot();
		});
	});

	describe('getSafeRangeKey', () => {
		it('should return the rangeKey when it is different of CUSTOM', () => {
			const rangeKey = getSafeRangeKey('30');

			expect(rangeKey).toBe('30');
		});

		it('should return null when it is CUSTOM', () => {
			const rangeKey = getSafeRangeKey('CUSTOM');

			expect(rangeKey).toBe(null);
		});
	});
});
