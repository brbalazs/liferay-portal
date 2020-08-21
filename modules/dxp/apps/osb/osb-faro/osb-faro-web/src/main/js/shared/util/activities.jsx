import FaroConstants from 'shared/util/constants';
import moment from 'moment';
import React from 'react';
import {
	countBy,
	filter,
	flattenDepth,
	flow,
	get,
	groupBy,
	map,
	mapValues,
	maxBy,
	orderBy,
	toPairs
} from 'lodash/fp';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

const {
	activityActions: {downloads, submissions, visits},
	assetTypes
} = FaroConstants;

const ACTIVITY_ACTIONS_TITLE_LANG_MAP = {
	[downloads]: Liferay.Language.get('downloaded-x'),
	[submissions]: Liferay.Language.get('submitted-x'),
	[visits]: Liferay.Language.get('visited-x')
};

const ACTIVITY_ACTIONS_DESCRIPTION_LANG_MAP = {
	[downloads]: {
		plural: Liferay.Language.get('x-downloads'),
		singular: Liferay.Language.get('x-download')
	},
	[submissions]: {
		plural: Liferay.Language.get('x-submissions'),
		singular: Liferay.Language.get('x-submission')
	},
	[visits]: {
		plural: Liferay.Language.get('x-visits'),
		singular: Liferay.Language.get('x-visit')
	}
};

/**
 * Filters out activities that are not in the activity actions title lang map
 * and formats it into an array of object for a vertical timeline.
 * @param {Array} activities
 * @param {string|number} groupId
 * @param {string} channelId
 * @returns {Array.<Object>} Array of objects for a vertical timeline.
 */
function formatActivities(activities, groupId, channelId) {
	return activities
		.filter(({action}) => !!ACTIVITY_ACTIONS_TITLE_LANG_MAP[action])
		.map(
			({
				action,
				assetType,
				canonicalUrl,
				dataSourceAssetPK,
				id,
				name,
				startTime
			}) => {
				const assetRoute = getAssetRoute(assetType);

				const assetURL = assetRoute
					? `${toRoute(assetRoute, {
							assetId:
								assetType === assetTypes.webPage && canonicalUrl
									? canonicalUrl
									: dataSourceAssetPK,
							channelId,
							groupId,
							title: encodeURIComponent(name),
							touchpoint:
								assetType !== assetTypes.webPage
									? 'Any'
									: canonicalUrl
									? encodeURIComponent(canonicalUrl)
									: dataSourceAssetPK
					  })}`
					: null;

				return {
					subtitle: canonicalUrl,
					symbol: getObjectTypeIcon(assetType),
					time: startTime,
					title: sub(
						ACTIVITY_ACTIONS_TITLE_LANG_MAP[action],
						[<strong key={id}>{name}</strong>],
						false
					),
					url: assetURL
				};
			}
		);
}

/**
 * Formats datetime to today or the current date.
 * @param {Date|string|number} datetime - Any value accepeted by Moment.
 * @returns {Moment} Date label to be displayed.
 */
export function formatGroupingTime(datetime) {
	const time = moment(datetime);

	return time.isSame(moment(), 'day')
		? Liferay.Language.get('today')
		: time.utc().format('ll');
}

/**
 * Format sessions into a format usable by the VerticalTimeline component while grouping them by day.
 * @param {Array} sessions
 * @param {string} groupId
 * @param {string} channelId
 * @returns {Array.<Object>} An array of session objects.
 */
export function formatSessions(sessions, groupId, channelId) {
	return flow(
		groupBy(({day}) =>
			moment
				.utc(day)
				.startOf('day')
				.format()
		),
		mapValues(items =>
			items.map(({activities, id, individual, startTime}) => ({
				id,
				individual,
				nestedItems: formatActivities(activities, groupId, channelId),
				subtitle: getActivitiesSummary(activities),
				time: startTime,
				title: sub(Liferay.Language.get('visited-x'), [
					new URL(activities[0].url).hostname
				])
			}))
		),
		toPairs,
		orderBy([([time]) => moment(time).unix()], ['desc']),
		map(([time, items]) => [
			{header: true, title: formatGroupingTime(time)},
			items
		]),
		flattenDepth(2)
	)(sessions);
}

/**
 * Gets the summary of activity types for a session. Wraps items in <span> for
 * styling. Example of output displayed: '3 Downloads 2 Visits'.
 * @returns {Array} Description to display
 */
const getActivitiesSummary = flow(
	filter(({action}) => !!ACTIVITY_ACTIONS_DESCRIPTION_LANG_MAP[action]),
	countBy(({action}) => action),
	toPairs,
	map(([action, count]) => [
		<span key={action}>
			{sub(
				get(
					[action, count === 1 ? 'singular' : 'plural'],
					ACTIVITY_ACTIONS_DESCRIPTION_LANG_MAP,
					''
				),
				[count]
			)}
		</span>
	]),
	flattenDepth(2)
);

/**
 * Helper function get the correct pluralization of count label.
 * @param {Number} totalElements
 * @returns {Array} Label to be displayed.
 */
export function getActivityLabel(totalElements) {
	return sub(
		totalElements === 1
			? Liferay.Language.get('activity-x')
			: Liferay.Language.get('activities-x'),
		[<b key='ACTIVITIES'>{totalElements}</b>],
		false
	);
}

/**
 * Get the asset route from the assetType.
 * @param {string} assetType
 * @return {string} Route to assetType page.
 */
function getAssetRoute(assetType) {
	switch (assetType) {
		case assetTypes.document:
			return Routes.ASSETS_DOCUMENTS_AND_MEDIA_DASHBOARD;
		case assetTypes.form:
			return Routes.ASSETS_FORMS_DASHBOARD;
		case assetTypes.webPage:
			return Routes.SITES_TOUCHPOINTS_OVERVIEW;
		default:
			return null;
	}
}

/**
 * Get the max activities value from activitiesHistory.
 * @param {Object[]} activitiesHistory - An array of activity history objects.
 * @param {number} activitiesHistory[].totalElements - Number of activities.
 * @param {number} [defaultMax] - The default value to return.
 * @returns {number} - Returns the larger of the max totalElements and the defaultMax.
 */
export function getMaxActivitiesValue(activitiesHistory, defaultMax = 10) {
	const maxTotalElements = flow(
		maxBy('totalElements'),
		get('totalElements')
	)(activitiesHistory);

	return maxTotalElements > defaultMax ? maxTotalElements : defaultMax;
}

/**
 * Get the icon name from the assetType.
 * @param {string} assetType
 * @return {string} Name of icon.
 */
function getObjectTypeIcon(assetType) {
	switch (assetType) {
		case assetTypes.document:
			return 'download';
		case assetTypes.form:
			return 'forms';
		case assetTypes.webPage:
			return 'page';
		default:
			return 'folder';
	}
}
