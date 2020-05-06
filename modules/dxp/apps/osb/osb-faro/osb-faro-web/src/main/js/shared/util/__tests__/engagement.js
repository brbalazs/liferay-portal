import * as data from 'test/data';
import {
	formatEngagementAggregation,
	formatEngagementScore,
	getSafeEngagementDisplay,
	getScoreFromHistory,
	mergeArraysByKey
} from '../engagement';

const mockEngagementAggregations = [
	{
		intervalInitDate: data.getTimestamp(-2),
		scoreAvg: 2.1,
		totalElements: 1
	},
	{
		intervalInitDate: data.getTimestamp(-1),
		scoreAvg: 2.0,
		totalElements: 1
	},
	{
		intervalInitDate: data.getTimestamp(),
		scoreAvg: 2.8,
		totalElements: 1
	}
];

const mockGrowthHistory = [
	{
		addedIndividualsCount: 10,
		individualsCount: 100,
		intervalInitDate: data.getTimestamp(-2),
		removedIndividualsCount: 10
	},
	{
		addedIndividualsCount: 20,
		individualsCount: 200,
		intervalInitDate: data.getTimestamp(-1),
		removedIndividualsCount: 20
	},
	{
		addedIndividualsCount: 30,
		individualsCount: 300,
		intervalInitDate: data.getTimestamp(),
		removedIndividualsCount: 30
	}
];

describe('engagement', () => {
	describe('formatEngagementAggregation', () => {
		it('should format the engagement score in an engagement aggregation', () => {
			const engagementAggregation = {
				intervalInitDate: data.getTimestamp(),
				scoreAvg: 0.4899,
				totalElements: 2
			};

			const expected = {
				contributors: 2,
				intervalInitDate: data.getTimestamp(),
				scoreAvg: 4.899
			};

			expect(formatEngagementAggregation(engagementAggregation)).toEqual(
				expected
			);
		});
	});

	describe('formatEngagementScore', () => {
		it('should format engagement score to a 10 point scale when engagement score is a string', () => {
			const result = formatEngagementScore('0.15');

			expect(result).toBe(1.5);
		});

		it('should format engagement score to a 10 point scale when engagement score is a number', () => {
			const result = formatEngagementScore(0.15);

			expect(result).toBe(1.5);
		});

		it('should not format engagement score when engagement score is null', () => {
			const result = formatEngagementScore(null);

			expect(result).toBe(null);
		});
	});

	describe('getSafeEngagementDisplay', () => {
		it('should return a formatted engagement score', () => {
			expect(getSafeEngagementDisplay(0.4899)).toEqual('0.49/10');
		});

		it('should return a display fallback for engagement score', () => {
			expect(getSafeEngagementDisplay(null)).toEqual('-');
		});
	});

	describe('getScoreFromHistory', () => {
		it('should return the scoreAvg from the last engagementAggregation', () => {
			expect(getScoreFromHistory(mockEngagementAggregations)).toBe(
				mockEngagementAggregations[
					mockEngagementAggregations.length - 1
				].scoreAvg
			);
		});
	});

	describe('mergeArraysByKey', () => {
		it('should merge two object arrays together by an indicated key', () => {
			const expected = mockEngagementAggregations.map((item, i) => ({
				...item,
				...mockGrowthHistory[i]
			}));

			expect(
				mergeArraysByKey(
					mockEngagementAggregations,
					mockGrowthHistory,
					'intervalInitDate'
				)
			).toEqual(expected);
		});

		it('should exclude data from the destination object array if it does not have a match in the source array', () => {
			const mockEngagementAggregationsPlusOne = [
				{
					intervalInitDate: data.getTimestamp(-3),
					scoreAvg: 4.0
				}
			].concat(mockEngagementAggregations);

			const expected = mockEngagementAggregations.map((item, i) => ({
				...item,
				...mockGrowthHistory[i]
			}));

			expect(
				mergeArraysByKey(
					mockEngagementAggregationsPlusOne,
					mockGrowthHistory,
					'intervalInitDate'
				)
			).toEqual(expected);
		});
	});
});
