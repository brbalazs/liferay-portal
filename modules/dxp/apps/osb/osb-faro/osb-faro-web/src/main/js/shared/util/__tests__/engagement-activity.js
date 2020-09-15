import * as data from 'test/data';
import {
	buildEngagementActivityAxes,
	buildLegendItems,
	convertHistoryInitDateToDate,
	formatTickVal,
	getSafeRangeKey,
	renderTooltip
} from '../engagement-activity';
import {createDateKeysIMap} from 'shared/util/intervals';

const mockHistory = [
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

describe('engagement-activity', () => {
	describe('buildHistoryData', () => {
		it('should return an array formatted for use in a chart', () => {
			expect(buildEngagementActivityAxes(mockHistory)).toMatchSnapshot();
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

	describe('convertHistoryInitDateToDate', () => {
		it('should convert intervalInitDates in Date', () => {
			const parsedHistory = convertHistoryInitDateToDate(mockHistory);

			parsedHistory.forEach(({intervalInitDate}) =>
				expect(intervalInitDate).toBeValidDate()
			);
		});
	});

	describe('renderTooltip', () => {
		it('should render', () => {
			const mockData = [{index: 0}];
			const interval = 'D';
			const dateKeysIMap = createDateKeysIMap(
				interval,
				mockHistory,
				'intervalInitDate'
			);

			const tooltipOptions = {
				dateKeysIMap,
				history: mockHistory,
				interval,
				name: 'Activities',
				rangeSelectors: {
					rangeKey: '30'
				},
				title: 'Activities',
				type: 'number'
			};

			expect(renderTooltip(tooltipOptions)(mockData)).toMatchSnapshot();
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
