import * as data from 'test/data';
import {
	buildEngagementActivityAxes,
	buildLegendItems,
	formatTickVal,
	renderTooltipToString
} from '../engagement-activity';

describe('engagement-activity', () => {
	describe('buildHistoryData', () => {
		it('should return an array formatted for use in a chart', () => {
			const mockHistoryData = [
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

			expect(
				buildEngagementActivityAxes(mockHistoryData)
			).toMatchSnapshot();
		});
	});

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

	describe('renderTooltipToString', () => {
		it('should render', () => {
			const mockData = [{index: 0}];

			const mockHistory = [
				{
					intervalInitDate: data.getTimestamp(-2),
					scoreAvg: 2.1,
					totalElements: 10
				}
			];

			expect(
				renderTooltipToString(mockData, mockHistory)
			).toMatchSnapshot();
		});
	});
});
