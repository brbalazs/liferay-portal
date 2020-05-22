import FaroConstants from 'shared/util/constants';
import getMetricsMapper from '../metrics';
import {getSafeRangeSelectors} from 'shared/util/util';

const {
	pagination: {orderDescending}
} = FaroConstants;

const mockData = {
	pages: {
		assetMetrics: [
			{
				assetId: '123',
				assetTitle: 'Test Test',
				avgTimeOnPageMetric: {value: 123123},
				bounceRateMetric: {value: 0.123},
				engagementMetric: {value: 0.02},
				entrancesMetric: {value: 43},
				exitRateMetric: {value: 0.013},
				viewsMetric: {value: 1},
				visitorsMetric: {value: 2}
			}
		],
		total: 123
	}
};

const mockProps = {
	defaultSort: {
		field: 'bounceRateMetric',
		sortOrder: orderDescending
	},
	rangeSelectors: {rangeKey: '90'},
	router: {
		params: {
			channelId: '321',
			interestId: 'Testing Wiki',
			title: 'test test',
			touchpoint: 'test.com/test'
		},
		query: {
			delta: '5',
			orderBy: orderDescending,
			orderByField: 'bounceRateMetric',
			page: '2',
			query: 'Test Test',
			rangeKey: '90'
		}
	}
};

describe('metrics', () => {
	describe('getMetricsMapper', () => {
		it('mapPropsToOptions', () => {
			const mapper = getMetricsMapper(result => ({
				items: result.pages.assetMetrics,
				total: result.pages.total
			}));

			const {
				params: {channelId, interestId, title, touchpoint},
				query: {delta, orderByField, query, rangeKey}
			} = mockProps.router;

			expect(mapper.options(mockProps)).toEqual(
				expect.objectContaining({
					variables: {
						channelId,
						keywords: query,
						size: parseInt(delta),
						sort: {
							column: orderByField,
							type: 'DESC'
						},
						start: 5,
						terms: interestId,
						title,
						touchpoint,
						...getSafeRangeSelectors({rangeKey})
					}
				})
			);
		});

		it('mapResultToProps', () => {
			const mapper = getMetricsMapper(result => ({
				items: result.pages.assetMetrics,
				total: result.pages.total
			}));

			expect(mapper.props({data: mockData})).toEqual(
				expect.objectContaining({
					items: [
						{
							assetId: '123',
							assetTitle: 'Test Test',
							avgTimeOnPageMetric: 123123,
							bounceRateMetric: 0.123,
							engagementMetric: 0.02,
							entrancesMetric: 43,
							exitRateMetric: 0.013,
							viewsMetric: 1,
							visitorsMetric: 2
						}
					],
					total: expect.any(Number)
				})
			);
		});

		it('Returns empty=true if no results', () => {
			const mapper = getMetricsMapper(result => ({
				items: result.pages.assetMetrics,
				total: result.pages.total
			}));

			expect(
				mapper.props({
					data: {pages: {assetMetrics: []}}
				})
			).toEqual(
				expect.objectContaining({
					empty: true
				})
			);
		});
	});
});
