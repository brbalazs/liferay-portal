import {flow, get, isNull, last, merge} from 'lodash/fp';

/**
 * Object containing a collection of aggregated data where intervalInitDate
 * represents the date of the data snapshot.
 * @typedef {Object} ActivitiesAggregation
 * @property {number} intervalInitDate - The date of this data snapshot.
 * @property {number} totalElements - The total number of activites in snapshot.
 */

/**
 * Object containing a collection of aggregated engagement data where intervalInitDate
 * represents the date of the data snapshot.
 * @typedef {Object} EngagementAggregation
 * @property {number} intervalInitDate - The date of this data snapshot.
 * @property {number} scoreAvg - The engagement score.
 * @property {number} totalElements - The contributors to the scoreAvg.
 */

/**
 * An EngagementAggregation object with totalElements renamed to contributors
 * and scoreAvg formatted to be a value between 0 and 10.
 * @typedef {Object} FormattedEngagementAggregation
 * @property {number} contributors - The contributors to the scoreAvg.
 * @property {number} intervalInitDate - The date of this data snapshot.
 * @property {number} scoreAvg - The engagement score.
 */

/**
 * Object containing a collection of aggregated data where intervalInitDate
 * represents the date of the data snapshot.
 * @typedef {Object} GrowthAggregation
 * @property {number} addedIndividualsCount
 * @property {number} individualsCount - The total number of individual membership as of the snapshot.
 * @property {number} intervalInitDate - The date of this data snapshot.
 * @property {number} removedIndividualsCount
 */

/**
 * Format an individual engagement aggregation.
 * @param {EngagementAggregation} engagementAggregation - The EngagementAggregation as provided by backend.
 * @param {number} engagementAggregation.intervalInitDate - The date of this data snapshot.
 * @param {number} engagementAggregation.scoreAvg - The engagement score.
 * @param {number} engagementAggregation.totalElements - The contributors to the scoreAvg.
 * @returns {FormattedEngagementAggregation} Aggregation with the engagement score formatted and totalElements renamed to contributors.
 */
export function formatEngagementAggregation({
	intervalInitDate,
	scoreAvg,
	totalElements
}) {
	return {
		contributors: totalElements,
		intervalInitDate,
		scoreAvg: formatEngagementScore(scoreAvg)
	};
}

/**
 * Format the score to a 10 point scale.
 * @param {string|number} score - Engagement score on a 1 point scale.
 * @returns {number} Engagement score on a 10 point scale.
 */
export function formatEngagementScore(score) {
	return isNull(score) ? score : Number(score) * 10;
}

/**
 * Format the engagement score for display and fallback if engagement score is null.
 * @param {number} score - The engagement score.
 * @param {string} [defaultValue] - The fallback display value.
 * @returns {string} The formatted engagement score display.
 */
export function getSafeEngagementDisplay(score, defaultValue = '-') {
	return isNull(score) ? defaultValue : `${score.toFixed(2)}/10`;
}

/**
 * Get engagement score from last {@link EngagementAggregation}
 * in engagementHistory.
 * @param {Array.<EngagementAggregation>} engagementHistory
 */
export function getScoreFromHistory(engagementHistory) {
	return flow(
		last,
		get('scoreAvg')
	)(engagementHistory);
}

/**
 * Merge source Array into destination Array by specified key.
 * @param {Array.<Object>} destination - Array to be base.
 * @param {Array.<Object>} source - Array to merge into base.
 * @param {string} key - The key to use for comparison.
 * @returns {Array.<Object>} Merged Arrays.
 */
export function mergeArraysByKey(destination, source, key) {
	const sourceMap = new Map();

	source.forEach(val => sourceMap.set(val[key], val));

	return destination.reduce((acc, val) => {
		const sourceVal = sourceMap.get(val[key]);

		if (sourceVal && sourceVal[key] === val[key]) {
			return acc.concat([merge(val, sourceVal)]);
		}

		return acc;
	}, []);
}

/**
 * Merge engagement history with another history, where the history
 * with precedence is the one with the most data points.
 * @param {Array.<EngagementAggregation>} engagementHistory
 * @param {Array.<GrowthAggregation|ActivitesAggregation>} otherHistory
 */
export function mergeHistoryByDate(engagementHistory, otherHistory) {
	return otherHistory.length > engagementHistory.length
		? mergeArraysByKey(otherHistory, engagementHistory, 'intervalInitDate')
		: mergeArraysByKey(engagementHistory, otherHistory, 'intervalInitDate');
}
