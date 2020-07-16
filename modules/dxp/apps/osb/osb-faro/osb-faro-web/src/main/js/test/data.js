import FaroConstants, {
	jobRunDataPeriods,
	jobRunFrequencies,
	jobStatuses,
	jobTypes
} from 'shared/util/constants';
import moment from 'moment';
import {clamp, find, isArray, range, times} from 'lodash';
import {
	CONJUNCTIONS,
	RELATIONAL_OPERATORS
} from 'contacts/components/segment-editor/dynamic/utils/constants';
import {fromJS, List, Map} from 'immutable';
import {getISODate} from 'shared/util/date';
import {Metric} from 'shared/util/records';

const {
	activityActions,
	assetTypes,
	clauseOperators,
	contactsCardTemplateTypes: {cardTypes, profileCardLayoutTypes},
	credentialTypes: {oAuth2},
	criterionTypes,
	dataSourceProgressStatuses,
	dataSourceStates,
	dataSourceStatuses,
	dataSourceTypes,
	entityTypes,
	projectStates,
	segmentStates,
	subscriptionStatuses,
	userRoleNames
} = FaroConstants;

const BASE_TIMESTAMP = 1531263666366;

export function getTimestamp(change = 0, unit = 'days') {
	return moment(BASE_TIMESTAMP)
		.add(change, unit)
		.valueOf();
}

export function getDate(change = 0, unit = 'days') {
	return moment(BASE_TIMESTAMP)
		.add(change, unit)
		.toDate();
}

export function getImmutableMock(Record, mockFn, id, config) {
	return new Record(fromJS(mockFn(id, config)));
}

export function mockApiToken(data) {
	return {
		createDate: getISODate(getTimestamp()),
		expirationDate: getISODate(getTimestamp(30)),
		lastAccessDate: getISODate(getTimestamp()),
		token: '1234ABC',
		...data
	};
}

export function mockForm(data) {
	return {
		errors: {},
		touched: {},
		...data
	};
}

export function mockIndividual(seed = 0, properties) {
	return {
		activitiesCount: 1000,
		colorId: String(seed),
		dateCreated: getTimestamp(-2),
		engagementScore: 0.4899,
		id: String(seed),
		image: '/path/to/portrait.png',
		lastActivityDate: getTimestamp(),
		name: 'Foo Bar',
		properties: {
			familyName: 'Bar',
			givenName: 'Foo',
			jobTitle: 'Developer',
			salary: '50,000',
			title: 'Developer',
			...properties
		},
		type: FaroConstants.entityTypes.individual
	};
}

export function mockIndividualDetails(data = {}) {
	return {
		custom: {
			favorite_fruit: [
				{
					dataSourceId: '123',
					fieldType: 'Text',
					name: 'favorite_fruit',
					sourceName: 'Favorite Fruit',
					value: 'bananas'
				}
			],
			favorite_pokemon: [
				{
					dataSourceId: '123',
					fieldType: 'Text',
					name: 'favorite_pokemon',
					sourceName: 'Favorite Pokemon',
					value: 'Charmander'
				}
			]
		},
		demographics: {
			familyName: [
				{
					dataSourceId: '123',
					fieldType: 'Text',
					name: 'familyName',
					value: 'Foo'
				}
			],
			givenName: [
				{
					dataSourceId: '123',
					fieldType: 'Text',
					name: 'givenName',
					value: 'Bar'
				}
			],
			jobTitle: [
				{
					dataSourceId: '123',
					fieldType: 'Text',
					name: 'jobTitle',
					value: 'Developer'
				}
			],
			...data
		}
	};
}

export function mockAccount(seed = 1, data = {}) {
	return {
		activitiesCount: 100,
		engagementScore: 0.4899,
		id: String(seed),
		individualCount: 10,
		name: `account${seed}`,
		properties: {
			accountNumber: '123123123',
			accountType: 'customer',
			annualRevenue: 'USD $1,000,000,000',
			billingAddress: '123 Fairy Lane Diamond Bar, CA 91765 USA',
			description: 'Foo company description',
			fax: '321-321-4321',
			industry: 'Agriculture',
			numberOfEmployees: '9001',
			ownership: 'private',
			phone: '123-123-1234',
			shippingAddress: '123 Fairy Lane Diamond Bar, CA 91765 USA',
			website: 'www.liferay.com',
			yearStarted: '2003'
		},
		type: entityTypes.account,
		...data
	};
}

export function mockAccountDetails(data = {}) {
	return {
		industry: [
			{
				context: 'organization',
				dataSourceId: '123',
				dataSourceName: 'test data source',
				dateModified: moment().subtract(7, 'days'),
				fieldType: 'Text',
				id: null,
				label: null,
				name: 'industry',
				ownerId: '23',
				ownerType: 'account',
				sourceName: 'industry',
				value: 'Agriculture'
			}
		],
		ownership: [
			{
				context: 'organization',
				dataSourceId: '123',
				dataSourceName: 'test data source',
				dateModified: moment().subtract(7, 'days'),
				fieldType: 'Text',
				id: null,
				label: null,
				name: 'name',
				ownerId: '23',
				ownerType: 'account',
				sourceName: 'businessOwnership',
				value: 'Northern Parts'
			},
			{
				context: 'organization',
				dataSourceId: '123',
				dataSourceName: 'test data source',
				dateModified: moment().subtract(7, 'days'),
				fieldType: 'Text',
				id: null,
				label: null,
				name: 'name',
				ownerId: '23',
				ownerType: 'account',
				sourceName: 'businessOwnership',
				value: 'Southern Parts'
			}
		],
		yearStarted: [
			{
				context: 'organization',
				dataSourceId: '123',
				dataSourceName: 'test data source',
				dateModified: moment().subtract(7, 'days'),
				fieldType: 'Text',
				id: null,
				label: null,
				name: 'yearStarted',
				ownerId: '23',
				ownerType: 'account',
				sourceName: 'yearStarted',
				value: '2003'
			}
		],
		...data
	};
}

export function mockAsset(seed = 0, data = {}) {
	return {
		canonicalUrl: `http://www.foo.com/${seed}`,
		dataSourceAssetPK: `asset-pk-${seed}`,
		dataSourceId: `dataSourceId${seed}`,
		description: null,
		id: String(seed),
		name: 'Test Asset',
		type: 'Form',
		...data
	};
}

export function mockCardTemplate(seed = 0, data = {}) {
	return {
		id: String(seed),
		layoutId: String(seed),
		name: '',
		size: 2,
		type: null,
		...data
	};
}

const MAPPING_TYPES = ['Boolean', 'Date', 'Number', 'Text'];

export function mockClause(seed = 0, data = {}) {
	const type = MAPPING_TYPES[clamp(seed, 0, MAPPING_TYPES.length - 1)];
	const operator = find(clauseOperators, ({supportedTypes}) =>
		supportedTypes.includes(type)
	);

	return {
		entity: {
			id: String(seed),
			name: `mapping${seed}`,
			type
		},
		entityRemoved: false,
		id: String(seed),
		operatorId: operator.id,
		type: criterionTypes.demographic,
		values: times(operator.labels.length, i => `value${i}`),
		...data
	};
}

export function mockCompany(seed = 0, data = {}) {
	return {
		companyId: 20155,
		id: String(seed),
		installationId: 13183,
		name: `Test Company ${seed}`,
		status: 1,
		url: 'http://www.liferay.com',
		version: '7.1',
		...data
	};
}

export function mockBlockedKeyword(seed = 0, data = {}) {
	return {
		createDate: 1575394162758,
		duplicate: false,
		id: '391941272912793194',
		keyword: `bar-${seed}`,
		...data
	};
}

export function mockChannel(seed = 1, permissionType = 0, data = {}) {
	return {
		createTime: 1581697505321,
		id: seed,
		name: `Channel ${seed}`,
		permissionType,
		tokenAuth: true,
		...data
	};
}

export function generateCriterion(customValues) {
	return {
		operatorName: RELATIONAL_OPERATORS.EQ,
		propertyName: 'firstName',
		touched: false,
		valid: true,
		value: 'test',
		...customValues
	};
}

export function mockNewCriteria(numOfItems = 1, criterionParams) {
	return {
		conjunctionName: CONJUNCTIONS.AND,
		groupId: 'group_01',
		items: range(numOfItems).map(() => generateCriterion(criterionParams))
	};
}

export function mockNewCriteriaNested() {
	return {
		conjunctionName: CONJUNCTIONS.AND,
		groupId: 'group_01',
		items: [
			{
				conjunctionName: CONJUNCTIONS.OR,
				groupId: 'group_02',
				items: [
					{
						conjunctionName: CONJUNCTIONS.AND,
						groupId: 'group_03',
						items: [
							{
								conjunctionName: CONJUNCTIONS.OR,
								groupId: 'group_04',
								items: range(2).map(generateCriterion)
							},
							generateCriterion()
						]
					},
					generateCriterion()
				]
			},
			generateCriterion()
		]
	};
}

export function mockCSVDataSource(seed = 1, data = {}) {
	return {
		dateCreated: getTimestamp(-2),
		disabled: false,
		event: null,
		fileName: null,
		id: String(seed),
		lastSyncDate: getTimestamp(),
		name: `CSV${seed}`,
		properties: {},
		provider: {
			type: dataSourceTypes.csv
		},
		providerType: dataSourceTypes.csv,
		state: dataSourceStates.ready,
		status: dataSourceStatuses.active,
		type: entityTypes.dataSource,
		url: 'liferay.example.faro.com',
		...data
	};
}

export function mockEngagementHistories(ids = [0], data = {}) {
	return ids.reduce((acc, curr, i) => {
		acc[curr] = mockEngagementData(i, data);

		return acc;
	}, {});
}

export function mockLiferayDataSource(seed = 1, data = {}) {
	return {
		credentials: {
			login: `LiferayUser${seed}`
		},
		dateCreated: getTimestamp(-2),
		disabled: false,
		event: null,
		fileName: null,
		id: String(seed),
		lastSyncDate: getTimestamp(),
		name: `Liferay${seed}`,
		properties: {},
		provider: {
			analyticsConfiguration: null,
			contactsConfiguration: null,
			instanceInfo: {},
			type: dataSourceTypes.liferay
		},
		providerType: dataSourceTypes.liferay,
		state: dataSourceStates.credentialsValid,
		status: dataSourceStatuses.active,
		type: entityTypes.dataSource,
		url: `https://www.faro-${seed}.io`,
		...data
	};
}

export function mockMetricFragment(seed = 0, data = {}) {
	return {
		histogram: range(30).map(i => ({
			key: `${moment(getTimestamp(i)).format('YYYY-MM-DD')}T00:00`,
			previousValue: seed * i * 2,
			previousValueKey: `${moment(getTimestamp(i - 30)).format(
				'YYYY-MM-DD'
			)}T00:00`,
			trend: {
				percentage: 0,
				trend: {
					percentage: -50,
					trendClassification: 'NEGATIVE'
				}
			},
			value: seed * i,
			valueKey: `${moment(getTimestamp(i)).format('YYYY-MM-DD')}T00:00`
		})),
		previousValue: seed * 0.75,
		trend: {
			percentage: 33.3,
			trendClassification: 'POSITIVE'
		},
		value: seed,
		...data
	};
}

export function mockProgress(data) {
	return {
		accounts: {
			dateRecorded: getTimestamp(),
			processedOperations: 100,
			status: dataSourceProgressStatuses.completed,
			totalOperations: 100
		},
		individuals: {
			dateRecorded: getTimestamp(),
			processedOperations: 100,
			status: dataSourceProgressStatuses.completed,
			totalOperations: 100
		},
		...data
	};
}

export function mockSalesforceDataSource(seed = 1, data = {}) {
	return {
		credentials: {
			oAuthClientId: `oAuthMockClientId-${seed}`,
			oAuthClientSecret: `oAuthMockClientSecret-${seed}`,
			type: oAuth2
		},
		dateCreated: getTimestamp(-2),
		disabled: false,
		event: null,
		fileName: null,
		id: String(seed),
		lastSyncDate: getTimestamp(),
		name: 'Salesforce{seed}',
		properties: {},
		provider: {
			analyticsConfiguration: null,
			contactsConfiguration: null,
			type: dataSourceTypes.salesforce
		},
		providerType: dataSourceTypes.salesforce,
		state: dataSourceStates.credentialsValid,
		status: dataSourceStatuses.active,
		type: entityTypes.dataSource,
		url: 'https://login.salesforce.com',
		...data
	};
}

export function mockDistribution(seed = 0) {
	return {
		count: seed,
		values: [`value${seed}`]
	};
}

export function mockEngagementData(data = {}) {
	return {
		change: -0.2,
		engagementAggregations: [
			{
				intervalInitDate: getTimestamp(-1),
				scoreAvg: 0.2,
				totalElements: 1
			},
			{
				intervalInitDate: getTimestamp(),
				scoreAvg: 0.28,
				totalElements: 1
			}
		],
		previousScoreAvg: 0.16,
		...data
	};
}

export function mockFieldMapping(seed = 0, data = {}) {
	return {
		context: 'demographics',
		dataSourceFieldNames: {},
		id: String(seed),
		name: `Test${seed}`,
		ownerType: 'individual',
		type: 'Text',
		values: [`value${seed}`],
		...data
	};
}

export function mockNullFieldMapping(seed = 0, data = {}) {
	return mockFieldMapping(seed, {
		value: null,
		...data
	});
}

export function mockIndividualEngagement(seed = 0, data = {}) {
	return {
		currentMember: true,
		emailAddress: `test${seed}@liferay.com`,
		id: String(seed),
		name: `Test Individual - ${seed}`,
		ownerId: String(`ownerId${seed}`),
		score: 0.8,
		...data
	};
}

export function mockIndividualAttributes(seed = 0, data = {}) {
	return {
		dataSources: range(seed + 1).map(i => ({
			dataSourceFieldName: `testMiddleName${i}`,
			dataSourceName: `LIFERAY-DATASOURCE-FARO-EXAMPLE-${i}`
		})),
		dateModified: getTimestamp(),
		fieldName: `testFildName${seed}`,
		...data
	};
}

export function mockInterestData(seed = 0) {
	return {
		interestAggregations: [
			{
				intervalInitDate: getTimestamp(-1),
				scoreAvg: 1.6,
				totalElements: 1,
				viewsSum: 2
			},
			{
				intervalInitDate: getTimestamp(),
				scoreAvg: 5,
				totalElements: 1,
				viewsSum: 2
			},
			{
				intervalInitDate: getTimestamp(1),
				scoreAvg: 2,
				totalElements: 1,
				viewsSum: 2
			}
		],
		name: `Bar${seed}`,
		pagesViewCount: 2,
		relatedPagesCount: 8,
		score: 1.6
	};
}

export function mockInterestObject(seed = 0, data = {}) {
	return {
		individualCount: 5,
		name: `interest${seed}`,
		...data
	};
}

export function mockLayout(seed = 0, faroEntity = mockSegment()) {
	return {
		activitiesCount: 1000,
		contactsCardData: {1: {id: 'cardData1'}, 2: {id: 'cardData2'}},
		contactsLayoutTemplate: {
			contactsCardTemplatesList: [
				[mockCardTemplate('cardTemplate1')],
				[mockCardTemplate('cardTemplate2')]
			],
			headerContactsCardTemplates: [
				mockCardTemplate('headerCardTemplate1')
			],
			id: seed,
			name: 'layoutTemplate'
		},
		engagementScore: 0.4899,
		faroEntity
	};
}

export function mockLiferaySyncCounts(data = {}) {
	return {
		allUsersCount: 1000,
		currentUsersCount: 248,
		organizationsUsersCount: 140,
		totalUsersCount: 448,
		userGroupsUsersCount: 260,
		...data
	};
}

export function mockMembershipChange(seed = 0, data = {}) {
	return {
		dateChanged: getTimestamp(),
		dateFirst: getTimestamp(-2),
		individualDeleted: false,
		individualEmail: 'test@faro.io',
		individualId: `id${seed}`,
		individualName: `Test${seed}`,
		individualsCount: 1,
		individualSegmentId: `segmentid${seed}`,
		operation: 'ADDED',
		...data
	};
}

export function mockMembershipChangeAggregation(seed = 0, data = {}) {
	return {
		addedIndividualsCount: 1,
		anonymousIndividualsCount: 1,
		individualsCount: seed,
		intervalInitDate: getTimestamp(),
		knownIndividualsCount: 1,
		removedIndividualsCount: 0,
		...data
	};
}

export function mockGraphqlOrganization(seed = 0, data = {}) {
	return {
		id: String(seed),
		name: `Test Organization ${seed}`,
		parentName: 'Foo Parent Organization',
		type: 'organization',
		...data
	};
}

export function mockOrganization(seed = 0, data = {}) {
	return {
		id: String(seed),
		name: `Test Organization ${seed}`,
		usersCount: seed,
		...data
	};
}

export function mockPageVisited(seed = 0, data = {}) {
	return {
		day: 0,
		description: 'Test Description',
		id: String(seed),
		interestName: `Bar${seed}`,
		title: 'Page Visited Title',
		url: 'https://www.liferay.com',
		viewCount: 1,
		...data
	};
}

export function mockSearch(mockEntity, total = 1, data = []) {
	return {
		disableSearch: false,
		items: range(total).map(i =>
			isArray(data) ? mockEntity(i, ...data) : mockEntity(i, data)
		),
		total
	};
}

export function mockSegment(seed = 0, data = {}) {
	return {
		activeIndividualCount: 0,
		activitiesCount: 1000,
		anonymousIndividualCount: 0,
		dateCreated: getTimestamp(-1),
		dateModified: getTimestamp(),
		engagementScore: 0.4899,
		id: String(seed),
		includeAnonymousUsers: false,
		individualCount: 10,
		knownIndividualCount: 0,
		lastActivityDate: getTimestamp(),
		name: `Seattle${seed}`,
		properties: {},
		state: segmentStates.ready,
		type: entityTypes.individualsSegment,
		...data
	};
}

export function mockSubscription(data = {}) {
	return {
		addOns: new List([
			new Map({
				name: 'Liferay Analytics Cloud Enterprise Contacts',
				quantity: 2
			}),
			new Map({
				name: 'Liferay Analytics Cloud Enterprise Tracked Pages',
				quantity: 1
			})
		]),
		endDate: getTimestamp(),
		individualsCount: 2057,
		individualsLimit: 105000,
		individualsStatus: subscriptionStatuses.ok,
		name: 'Liferay Analytics Cloud Enterprise',
		pageViewsCount: 100023,
		pageViewsLimit: 7000000,
		pageViewsStatus: subscriptionStatuses.ok,
		startDate: getTimestamp(-2),
		...data
	};
}

export function mockInfoCardTemplate(seed = 0, data = {}) {
	return {
		...mockCardTemplate(seed),
		fieldMappingTemplates: [
			mockMappingtemplate(seed, 'First Name:', 'givenName'),
			mockMappingtemplate(seed + 1, 'Last Name:', 'familyName'),
			mockMappingtemplate(seed + 2, 'Job Title:', 'jobTitle')
		],
		name: `Info Card ${seed}`,
		type: cardTypes.info,
		...data
	};
}

export function mockMapping(seed = 0, data = {}) {
	return {
		mapping: null,
		name: `Test${seed}`,
		suggestions: [],
		values: [],
		...data
	};
}

export function mockMappingtemplate(seed = 0, prefix, name, suffix) {
	return {
		fieldMappingId: `fieldMappingId${seed}`,
		fieldMappingName: name,
		prefix,
		suffix
	};
}

export function mockProfileCardTemplate(seed = 0, data = {}) {
	return {
		...mockCardTemplate(seed),
		fieldMappingNames: ['jobTitle', 'givenName', 'familyName'],
		fieldMappingTemplates: [
			mockMappingtemplate(seed, 'Salary', 'salary'),
			mockMappingtemplate(seed + 1, 'Title:', 'title')
		],
		layoutType: profileCardLayoutTypes.vertical,
		name: `Profile Card ${seed}`,
		type: cardTypes.profile,
		...data
	};
}

export function mockSegmentMembershipCardTemplate(seed = 0, data = {}) {
	return {
		...mockCardTemplate(seed),
		name: `Segment Membership Card ${seed}`,
		type: cardTypes.similar,
		...data
	};
}

export function mockActivity(seed = 0, data = {}, actionData) {
	return {
		activities: times(seed + (1 % 10), i => mockAction(i, actionData)),
		day: getTimestamp(),
		endTime: getTimestamp() + 10000000,
		id: `activity_id_${seed}`,
		name: `activity_${seed}`,
		startTime: getTimestamp(),
		...data
	};
}

export function mockActivityHistory(data = {}) {
	return {
		activityAggregations: [
			{
				intervalInitDate: getTimestamp(-1),
				totalElements: 1
			},
			{
				intervalInitDate: getTimestamp(),
				totalElements: 2
			}
		],
		change: 1.28,
		count: 123,
		prevCount: 54,
		...data
	};
}

export function mockAction(seed = 0, data = {}) {
	return {
		action: activityActions.visits,
		activityKey: `key_${seed}`,
		assetType: assetTypes.form,
		dataSourceAssetPK: String(seed),
		day: seed,
		eventId: String(seed),
		groupName: `group_${seed}`,
		id: `action_${seed}`,
		name: `Asset ${seed}`,
		startTime: seed + 100000,
		url: `https://www.liferay${seed}.com`,
		...data
	};
}

export function mockAddOns() {
	return [
		{
			baseSubscriptionPlan: 'Liferay Analytics Cloud Enterprise',
			limits: {
				individuals: 5000,
				pageViews: 0
			},
			name: 'Liferay Analytics Cloud Enterprise Contacts',
			price: 500
		},
		{
			baseSubscriptionPlan: 'Liferay Analytics Cloud Enterprise',
			limits: {
				individuals: 0,
				pageViews: 5000000
			},
			name: 'Liferay Analytics Cloud Enterprise Tracked Pages',
			price: 250
		}
	];
}

export function mockPlan({data = {}, individuals = {}, pageViews = {}} = {}) {
	return {
		addOns: {
			individuals: {
				name: 'Liferay Analytics Cloud Enterprise Contacts',
				quantity: 2
			},
			pageViews: {
				name: 'Liferay Analytics Cloud Enterprise Tracked Pages',
				quantity: 1
			}
		},
		endDate: getTimestamp(),
		metrics: {
			individuals: new Metric({
				count: 2057,
				limit: 105000,
				status: subscriptionStatuses.ok,
				...individuals
			}),
			pageViews: new Metric({
				count: 0,
				limit: 7000000,
				status: subscriptionStatuses.ok,
				...pageViews
			})
		},
		name: 'Liferay Analytics Cloud Enterprise',
		startDate: getTimestamp(-2),
		...{data}
	};
}

export function mockProject(seed = 1, data = {}) {
	return {
		accountKey: `accountKey${seed}`,
		accountName: `accountName${seed}`,
		addOnsIList: new Map(mockAddOns()),
		corpProjectName: `corpProjectName${seed}`,
		corpProjectUuid: `corpProjectUuid${seed}`,
		faroSubscription: mockSubscription(),
		groupId: seed,
		name: `project${seed}`,
		state: projectStates.ready,
		userId: seed,
		...data
	};
}

export function mockProjectState(seed = 1, data = {}) {
	return {
		groupId: seed,
		percentageComplete: 0,
		recommendationsEnabled: true,
		state: projectStates.ready,
		...data
	};
}

export function mockProperty(seed = 1, data = {}) {
	return {
		entityName: `fooProperty-${seed}`,
		entityType: '',
		id: null,
		label: '',
		name: '',
		propertyKey: '',
		type: '',
		...data
	};
}

export function mockRecommendationJobRun(seed = 0, data = {}) {
	return {
		completedDate: '2020-04-24',
		context: [
			{key: 'userItemInteractionsDatasetCount', value: 321},
			{key: 'itemsDatasetCount', value: 123}
		],
		id: seed,
		status: 'COMPLETED',
		...data
	};
}

export function mockRecommendationJobRunsMonthlyStatistics(data = {}) {
	return {
		availableJobRuns: 3,
		completedJobRuns: 4,
		failedJobRuns: 1,
		runningJobRuns: 1,
		scheduledJobRuns: 3,
		...data
	};
}

export function mockRecommendationPageAsset(seed = 0, data = {}) {
	return {
		canonicalUrl: `https://www.test${seed}.com`,
		description: 'Test description',
		id: seed,
		keywords: [],
		title: 'Test Title',
		url: `https://www.test${seed}.com`,
		...data
	};
}

export function mockRecommendationJob(seed = 0, data = {}) {
	return {
		id: String(seed),
		name: `Recommendation Job Name ${seed}`,
		nextRunDate: getTimestamp(0),
		parameters: [],
		runDataPeriod: jobRunDataPeriods.last30Days,
		runDate: getTimestamp(-2),
		runFrequency: jobRunFrequencies.every30Days,
		status: jobStatuses.ready,
		type: jobTypes.itemSimilarity,
		...data
	};
}

export function mockSite(seed = 0, data = {}) {
	return {
		friendlyURL: `/guest/${seed}`,
		id: String(seed),
		name: `Test - ${seed}`,
		parentGroupId: '23',
		...data
	};
}

export function mockUser(seed = 0, data = {}) {
	return {
		emailAddress: 'test@liferay.com',
		id: seed,
		name: 'Test Test',
		roleName: userRoleNames.owner,
		status: 1,
		...data
	};
}

export function mockMemberUser(seed, data) {
	return mockUser(seed, {roleName: userRoleNames.member, ...data});
}

export function mockUserGroup(seed = 0, data = {}) {
	return {
		id: String(seed),
		name: `Test User Group ${seed}`,
		usersCount: seed,
		...data
	};
}

export function getTreeCount(id) {
	return (id * 4) % 10;
}

export function mockTreeItem(seed = 0, parentId = 0) {
	const id = parentId * 10 + seed;
	const count = getTreeCount(id);

	return {
		count,
		description: count ? `${count} Assets` : '',
		id,
		name: `Child${id}`,
		parentId: parentId || undefined
	};
}
