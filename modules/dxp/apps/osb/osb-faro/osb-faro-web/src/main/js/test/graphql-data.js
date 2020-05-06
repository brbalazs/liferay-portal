import IndividualMetricsQuery from 'contacts/individual/dashboard/queries/IndividualMetricsQuery';
import InterestsQuery from 'contacts/individual/dashboard/queries/InterestsQuery';
import OrganizationsQuery from 'contacts/components/segment-editor/dynamic/queries/OrganizationsQuery';
import SitesDashboardQuery from 'sites/queries/SitesDashboardQuery';
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
				rangeKey: parseInt(LAST_30_DAYS)
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
