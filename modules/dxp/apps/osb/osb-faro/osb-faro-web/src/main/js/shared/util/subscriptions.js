import FaroConstants from 'shared/util/constants';
import {fromJS, List, Map} from 'immutable';
import {isNil} from 'lodash';
import {Metric, Plan} from 'shared/util/records';

const {subscriptionPlans, subscriptionStatuses} = FaroConstants;

export const INDIVIDUALS = 'individuals';

export const PAGEVIEWS = 'pageViews';

export const PLAN_TYPES = {
	['Liferay Analytics Cloud Basic']: 'basic',
	['Liferay Analytics Cloud Business']: 'business',
	['Liferay Analytics Cloud Business Contacts']: INDIVIDUALS,
	['Liferay Analytics Cloud Business Tracked Pages']: PAGEVIEWS,
	['Liferay Analytics Cloud Enterprise']: 'enterprise',
	['Liferay Analytics Cloud Enterprise Contacts']: INDIVIDUALS,
	['Liferay Analytics Cloud Enterprise Tracked Pages']: PAGEVIEWS
};

function formatSubscriptions(allPlans) {
	const addOns = {
		[INDIVIDUALS]: {business: {}, enterprise: {}},
		[PAGEVIEWS]: {business: {}, enterprise: {}}
	};

	const plans = {};

	const hasKeyProperty = key =>
		Object.prototype.hasOwnProperty.call(allPlans, key);

	for (const key in allPlans) {
		if (hasKeyProperty(key)) {
			const {
				baseSubscriptionPlan,
				individualsLimit,
				name,
				pageViewsLimit,
				price
			} = allPlans[key];

			const planType = PLAN_TYPES[key];

			const formattedPlan = {
				baseSubscriptionPlan,
				limits: {
					[INDIVIDUALS]: individualsLimit,
					[PAGEVIEWS]: pageViewsLimit
				},
				name,
				price
			};

			const parentPlanType = PLAN_TYPES[baseSubscriptionPlan];

			if (baseSubscriptionPlan) {
				addOns[planType][parentPlanType] = formattedPlan;
			} else {
				plans[planType] = formattedPlan;
			}
		}
	}

	return {addOns, plans};
}

const {addOns, plans} = formatSubscriptions(subscriptionPlans);

export {addOns as ADD_ONS};

export {plans as PLANS};

export const STATUS_DISPLAY_MAP = {
	[subscriptionStatuses.ok]: 'primary',
	[subscriptionStatuses.approaching]: 'warning',
	[subscriptionStatuses.over]: 'danger'
};

export const DEFAULT_ADDONS = {
	[INDIVIDUALS]: addOns[INDIVIDUALS].business,
	[PAGEVIEWS]: addOns[PAGEVIEWS].business
};

export function getPlanAddOns(planType) {
	return planType === 'basic'
		? [DEFAULT_ADDONS[INDIVIDUALS], DEFAULT_ADDONS[PAGEVIEWS]]
		: [addOns[INDIVIDUALS][planType], addOns[PAGEVIEWS][planType]];
}

export function getPlanLabel(name) {
	switch (name) {
		case plans.basic.name:
			return Liferay.Language.get('basic-plan');
		case plans.business.name:
			return Liferay.Language.get('business-plan');
		case plans.enterprise.name:
			return Liferay.Language.get('enterprise-plan');
		default:
			return '';
	}
}

export function getPropIcon(name) {
	switch (name) {
		case INDIVIDUALS:
			return 'ac-individual';
		case PAGEVIEWS:
			return 'faro-page-views';
		default:
			return '';
	}
}

export function getPropLabel(name) {
	switch (name) {
		case INDIVIDUALS:
		case `${INDIVIDUALS}Limit`:
			return Liferay.Language.get('individuals');
		case PAGEVIEWS:
		case `${PAGEVIEWS}Limit`:
			return Liferay.Language.get('page-views');
		case plans.basic.name:
			return Liferay.Language.get('basic');
		case plans.business.name:
			return Liferay.Language.get('business');
		case plans.enterprise.name:
			return Liferay.Language.get('enterprise');
		default:
			return '';
	}
}

export function formatPlanData(subscriptionIMap) {
	if (isNil(subscriptionIMap)) {
		subscriptionIMap = new Map();
	}

	return new Plan(
		fromJS({
			addOns: {
				...subscriptionIMap
					.get('addOns', new List())
					.reduce((acc, addOn) => {
						acc[PLAN_TYPES[addOn.get('name')]] = addOn;
						return acc;
					}, {})
			},
			endDate: subscriptionIMap.get('endDate'),
			metrics: {
				individuals: new Metric({
					count: subscriptionIMap.get('individualsCount', 0),
					limit: subscriptionIMap.get('individualsLimit', 0),
					status: subscriptionIMap.get(
						'individualsStatus',
						subscriptionStatuses.ok
					)
				}),
				pageViews: new Metric({
					count: subscriptionIMap.get('pageViewsCount', 0),
					limit: subscriptionIMap.get('pageViewsLimit', 0),
					status: subscriptionIMap.get(
						'pageViewsStatus',
						subscriptionStatuses.ok
					)
				})
			},
			name: subscriptionIMap.get('name'),
			startDate: subscriptionIMap.get('startDate')
		})
	);
}
