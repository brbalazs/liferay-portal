import * as data from 'test/data';
import {
	formatGroupingTime,
	formatSessions,
	getActivityLabel,
	getMaxActivitiesValue
} from '../activities';

describe('activities', () => {
	describe('formatGroupingTime', () => {
		it('should format grouping time', () => {
			const result = formatGroupingTime(data.getTimestamp());

			expect(result).toMatchSnapshot();
		});
	});

	describe('formatSessions', () => {
		it('should format sessions', () => {
			const result = formatSessions(
				[data.mockActivity(2, {}, {assetType: 'foo'})],
				'123',
				'321'
			);

			expect(result).toMatchSnapshot();
		});
	});

	describe('getActivityLabel', () => {
		it('should get singular label', () => {
			const result = getActivityLabel(1);

			expect(result).toMatchSnapshot();
		});

		it('should plural label', () => {
			const result = getActivityLabel(2);

			expect(result).toMatchSnapshot();
		});
	});

	describe('getMaxActivitiesValue', () => {
		const activitiesHistory = [
			{
				totalElements: 15
			},
			{
				totalElements: 5
			}
		];

		it('should return the max totalElements value', () => {
			const result = getMaxActivitiesValue(activitiesHistory);

			expect(result).toEqual(15);
		});

		it('should return the minVal', () => {
			const minVal = 20;

			const result = getMaxActivitiesValue(activitiesHistory, minVal);

			expect(result).toEqual(minVal);
		});
	});
});
