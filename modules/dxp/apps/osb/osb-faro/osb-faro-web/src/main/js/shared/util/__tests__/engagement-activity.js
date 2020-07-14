import * as data from 'test/data';
import {
	buildEngagementActivityAxes,
	buildLegendItems,
	convertHistoryInitDateToDate,
	createDateKeysIMap,
	formatTickVal,
	renderTooltip,
	renderTooltipToString
} from '../engagement-activity';
import {Map} from 'immutable';

const mockHistoryDate = [
	{
		intervalInitDate: data.getDate(0),
		scoreAvg: 2,
		totalElements: 5
	},
	{
		intervalInitDate: data.getDate(-1),
		scoreAvg: 4,
		totalElements: 10
	}
];

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

	describe('createDateKeysIMap', () => {
		it('should create an dateKeysIMap', () => {
			const dateKeysIMap = createDateKeysIMap('D', mockHistoryDate);

			expect(dateKeysIMap).toBeInstanceOf(Map);
		});

		it('should create an dateKeysIMap with two date when interval is week', () => {
			const dateKeysIMap = createDateKeysIMap('W', mockHistoryDate);

			const dates = dateKeysIMap.get(data.getDate(0));

			expect(dates[0]).toBeValidDate();
			expect(dates[1]).toBeValidDate();
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
			const dateKeysIMap = createDateKeysIMap(interval, mockHistoryDate);

			const tooltipOptions = {
				dateKeysIMap,
				history: mockHistoryDate,
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
});
