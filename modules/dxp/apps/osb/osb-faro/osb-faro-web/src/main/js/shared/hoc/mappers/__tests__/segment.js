import * as data from 'test/data';
import {mapEngagementHistory, mapGrowthHistory, mapHistories} from '../segment';

describe('Segment Mappers', () => {
	describe('mapEngagementHistory', () => {
		it('should remap an engagement history API response', () => {
			const mockAPIResponse = data.mockEngagementData();

			expect(mapEngagementHistory(mockAPIResponse)).toEqual(
				expect.objectContaining({
					data: expect.any(Array),
					previousScore: expect.any(Number)
				})
			);
		});
	});

	describe('mapGrowthHistory', () => {
		it('should remap a Segment growth history API response', () => {
			const mockGrowthAggregation = {
				addedIndividualsCount: 1,
				individualsCount: 2,
				intervalInitDate: data.getTimestamp(),
				removedIndividualsCount: 3
			};

			const mockAPIResponse = [mockGrowthAggregation];

			expect(mapGrowthHistory(mockAPIResponse)).toEqual(
				expect.objectContaining({
					data: expect.arrayContaining([
						expect.objectContaining({
							added: mockGrowthAggregation.addedIndividualsCount,
							modifiedDate:
								mockGrowthAggregation.intervalInitDate,
							removed:
								mockGrowthAggregation.removedIndividualsCount,
							value: mockGrowthAggregation.individualsCount
						})
					])
				})
			);
		});
	});

	describe('mapHistories', () => {
		it('should combing and remap engagement and growth history API responses', () => {
			const mockAPIResponse = [
				data.mockEngagementData(),
				[data.mockMembershipChangeAggregation()]
			];

			expect(mapHistories(mockAPIResponse)).toEqual(
				expect.objectContaining({
					engagementHistory: expect.any(Object),
					growthHistory: expect.any(Object)
				})
			);
		});
	});
});
