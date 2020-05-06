import {
	formatEngagementAggregation,
	formatEngagementScore
} from 'shared/util/engagement';

/**
 * Map Engagement API response for use in a Component.
 * @param {Object} engagement - The engagement history data.
 * @returns {Object} - The remapped engagement history.
 */
export function mapEngagementHistory(engagement) {
	const {engagementAggregations, previousScoreAvg} = engagement;

	return {
		data: engagementAggregations.map(formatEngagementAggregation),
		previousScore: formatEngagementScore(previousScoreAvg)
	};
}

/**
 * Map Segment Growth API response for use in a Component.
 * @param {Array} growth - The Segment Growth history data.
 * @returns {Object} - The remapped Segment Growth history.
 */
export function mapGrowthHistory(growth) {
	return {
		data: growth.map(item => ({
			added: item.addedIndividualsCount,
			anonymousCount: item.anonymousIndividualsCount,
			knownCount: item.knownIndividualsCount,
			modifiedDate: item.intervalInitDate,
			removed: item.removedIndividualsCount,
			value: item.individualsCount
		}))
	};
}

/**
 * Map Segment Growth and Engagement API responses for use in a Component.
 * @param {Object} engagement - The engagement history data.
 * @param {Array} growth - The Segment Growth history data.
 * @returns {Object} - The remapped Segment Growth history.
 */
export function mapHistories([engagement, growth]) {
	return {
		engagementHistory: mapEngagementHistory(engagement),
		growthHistory: mapGrowthHistory(growth)
	};
}
