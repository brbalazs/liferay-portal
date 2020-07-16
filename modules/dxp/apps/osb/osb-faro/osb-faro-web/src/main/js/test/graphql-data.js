import IndividualMetricsQuery from 'contacts/individual/dashboard/queries/IndividualMetricsQuery';
import InterestsQuery from 'contacts/individual/dashboard/queries/InterestsQuery';
import OrganizationsQuery from 'contacts/components/segment-editor/dynamic/queries/OrganizationsQuery';
import RecommendationActivitiesQuery from 'settings/recommendations/queries/RecommendationActivitiesQuery';
import RecommendationJobRunsQuery from 'settings/recommendations/queries/RecommendationJobRunsQuery';
import RecommendationPageAssetsQuery from 'settings/recommendations/queries/RecommendationPageAssetsQuery';
import RecommendationQuery from 'settings/recommendations/queries/RecommendationQuery';
import SitesDashboardQuery from 'sites/queries/SitesDashboardQuery';
import SuppressedUsersListQuery from 'settings/data-privacy/queries/SuppressedUsersListQuery';
import TimeRangeQuery from 'shared/queries/TimeRangeQuery';
import TouchpointsQuery from 'sites/queries/TouchpointsQuery';
import {getSafeRangeSelectors} from 'shared/util/util';
import {INTERVAL_KEY_MAP} from 'shared/util/time';
import {isArray, mapValues, range} from 'lodash';
import {LAST_30_DAYS} from 'shared/util/constants';

const METRIC_TYPENAME_MAP = {
	histogram: 'HistogramMetric',
	trend: 'Trend'
};

export function mockBag({items, itemTypeName, name, typeName}) {
	return {
		[name]: {
			__typename: typeName,
			[name]: items.map(item => ({
				__typename: itemTypeName,
				...item
			})),
			total: items.length
		}
	};
}

export function mockCompositionBag({
	compositionBagName,
	compositions = [],
	maxCount = 0,
	total = 0,
	totalCount = 0
}) {
	const result = {
		__typename: 'CompositionBag',
		compositions: compositions.map(item => ({
			__typename: 'Composition',
			...item
		})),
		maxCount,
		total,
		totalCount
	};

	return compositionBagName ? {[compositionBagName]: result} : result;
}

export function mockDataControlTaskBag(items) {
	return mockBag({
		items,
		itemTypeName: 'DataControlTask',
		name: 'dataControlTasks',
		typeName: 'DataControlTaskBag'
	});
}

export function mockDataSourcesReq(dataSources = [], variables = {type: null}) {
	return {
		request: {
			query: SitesDashboardQuery,
			variables
		},
		result: {
			data: {
				dataSources: dataSources.map(dataSource => ({
					...dataSource,
					__typename: 'DataSource'
				}))
			}
		}
	};
}

export function mockIndividualInterestsReq(getVariables) {
	const defaultVariables = {
		active: true,
		id: undefined,
		keywords: '',
		size: 5,
		sort: {column: 'count', type: 'DESC'},
		start: 0
	};

	return {
		request: {
			query: InterestsQuery,
			variables: getVariables
				? getVariables(defaultVariables)
				: defaultVariables
		},
		result: {
			data: mockCompositionBag({
				compositionBagName: 'individualInterests',
				compositions: [
					{
						count: 2,
						name: 'cutting-edge platforms'
					},
					{count: 2, name: 'mesh'},
					{
						count: 2,
						name: 'mesh synergistic schemas'
					},
					{
						count: 2,
						name: 'synergistic schemas'
					},
					{
						count: 1,
						name: 'rich e-commerce'
					}
				],
				maxCount: 2,
				total: 5,
				totalCount: 3
			})
		}
	};
}

export function mockIndividualMetricsReq() {
	return {
		request: {
			query: IndividualMetricsQuery,
			variables: {
				channelId: '123123',
				interval: INTERVAL_KEY_MAP.week,
				...getSafeRangeSelectors({rangeKey: LAST_30_DAYS})
			}
		},
		result: {
			data: {
				individualMetric: {
					__typename: 'IndividualMetric',
					anonymousIndividualsMetric: mockMetric({
						histogram: [{key: '1', value: 1323321, valueKey: '1'}],
						trend: {percentage: 0},
						value: 1323321
					}),
					knownIndividualsMetric: mockMetric({
						histogram: [{key: '2', value: 11987, valueKey: '1'}],
						trend: {percentage: 12.5},
						value: 11987
					}),
					totalIndividualsMetric: mockMetric({
						histogram: [
							{key: '3', value: 1300000000, valueKey: '1'}
						],
						trend: {percentage: -25},
						value: 1300000000
					})
				}
			}
		}
	};
}

export function mockDXPEntitiesBag(entityName, items) {
	return {
		[entityName]: {
			__typename: 'DXPEntityBag',
			dxpEntities: items.map(item => ({
				__typename: 'DXEntity',
				...item
			})),
			total: items.length
		}
	};
}

export function mockOrganizationsListReq(items) {
	return {
		request: {
			query: OrganizationsQuery,
			variables: {
				keywords: '',
				size: 5,
				sort: {column: 'name', type: 'ASC'},
				start: 0
			}
		},
		result: {
			data: {
				...mockDXPEntitiesBag(
					'organizations',
					items ||
						range(5).map(i => ({
							dataSourceName: `fooDataSource-${i}`,
							id: i,
							name: `fooOrganization-${i}`,
							parentName: 'fooParentOrganization',
							type: 'fooOrganizationType'
						}))
				)
			}
		}
	};
}

export function mockMetric(metrics = {}) {
	return {
		...mapValues(metrics, (value, key) => {
			const typeName = METRIC_TYPENAME_MAP[key];
			return typeName
				? isArray(value)
					? value.map(item => ({...item, __typename: typeName}))
					: {...value, __typename: typeName}
				: value;
		}),
		__typename: 'Metric'
	};
}

export function mockRecommendationReq(item = {}, mockVariables = {}) {
	return {
		request: {
			query: RecommendationQuery,
			variables: {
				jobId: '321',
				...mockVariables
			}
		},
		result: {
			data: {
				jobById: {
					...item,
					__typename: 'Job'
				}
			}
		}
	};
}

export function mockRecommendationActivitiesReq(items, mockVariables = {}) {
	return {
		request: {
			query: RecommendationActivitiesQuery,
			variables: {
				applicationId: 'Page',
				eventContextPropertyFilters: [
					{filter: '.*custom-assets', negate: false}
				],
				eventId: 'pageUnloaded',
				rangeKey: '30',
				size: 0,
				start: 0,
				...mockVariables
			}
		},
		result: {
			data: {
				activities: {
					__typename: 'ActivityBag',
					activities: items,
					total: items.length
				}
			}
		}
	};
}

export function mockRecommendationJobRunsReq(items, mockVariables = {}) {
	return {
		request: {
			query: RecommendationJobRunsQuery,
			variables: {
				jobId: '321',
				size: 5,
				sort: {column: 'id', type: 'DESC'},
				start: 0,
				...mockVariables
			}
		},
		result: {
			data: {
				jobRuns: {
					__typename: 'JobRunBag',
					jobRuns: items,
					total: items.length
				}
			}
		}
	};
}

export function mockRecommendationPageAssetsReq(items, mockVariables = {}) {
	return {
		request: {
			query: RecommendationPageAssetsQuery,
			variables: {
				propertyFilters: [{filter: '.*custom-assets', negate: false}],
				size: 5,
				sort: {column: 'title', type: 'DESC'},
				start: 0,
				...mockVariables
			}
		},
		result: {
			data: {
				pageAssets: {
					__typename: 'PageAssetBag',
					pageAssets: items,
					total: items.length
				}
			}
		}
	};
}

export function mockSuppressedUsersListReq(items, mockVariables = {}) {
	return {
		request: {
			query: SuppressedUsersListQuery,
			variables: {
				keywords: '',
				size: 5,
				sort: {column: 'createDate', type: 'DESC'},
				start: 0,
				...mockVariables
			}
		},
		result: {
			data: {
				suppressions: {
					__typename: 'SuppressionBag',
					suppressions: items,
					total: items.length
				}
			}
		}
	};
}

export function mockTimeRangeReq() {
	return {
		request: {
			query: TimeRangeQuery
		},
		result: {
			data: {
				timeRange: [
					{
						__typename: 'TimeRange',
						default: false,
						endDate: '2020-05-08T23:00',
						rangeKey: 0,
						startDate: '2020-05-08T00:00'
					},
					{
						__typename: 'TimeRange',
						default: false,
						endDate: '2020-05-07T23:00',
						rangeKey: 1,
						startDate: '2020-05-07T00:00'
					},
					{
						__typename: 'TimeRange',
						default: false,
						endDate: '2020-05-07T23:59:59.999999999',
						rangeKey: 7,
						startDate: '2020-05-01T00:00'
					},
					{
						__typename: 'TimeRange',
						default: false,
						endDate: '2020-05-07T23:59:59.999999999',
						rangeKey: 90,
						startDate: '2020-02-08T00:00'
					},
					{
						__typename: 'TimeRange',
						default: false,
						endDate: '2020-05-07T23:59:59.999999999',
						rangeKey: 28,
						startDate: '2020-04-10T00:00'
					},
					{
						__typename: 'TimeRange',
						default: true,
						endDate: '2020-05-07T23:59:59.999999999',
						rangeKey: 30,
						startDate: '2020-04-08T00:00'
					}
				]
			}
		}
	};
}

export function mockTouchpointsReq(items, mockVariables = {}) {
	return {
		request: {
			query: TouchpointsQuery,
			variables: {
				channelId: '321321',
				devices: 'Any',
				keywords: '',
				location: 'Any',
				size: 5,
				sort: {column: 'visitorsMetric', type: 'DESC'},
				start: 0,
				terms: 'test',
				title: '',
				touchpoint: '',
				...getSafeRangeSelectors({rangeKey: LAST_30_DAYS}),
				...mockVariables
			}
		},
		result: {
			data: {
				pages: {
					__typename: 'AssetMetricBag',
					assetMetrics: items,
					total: items.length
				}
			}
		}
	};
}

export function mockJobBag(items) {
	return mockBag({
		items,
		itemTypeName: 'Job',
		name: 'jobs',
		typeName: 'JobBag'
	});
}
