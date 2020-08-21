import {getFilters} from 'shared/util/filter';
import {getSafeRangeSelectors} from 'shared/util/util';

/**
 * Safe Result To Props
 * @param {function} mapper
 */
export function safeResultToProps(mapper) {
	return ({data, ownProps}, context) => {
		let result = {};

		try {
			const {error, loading, refetch} = data;

			if (error) {
				console.error(error); // eslint-disable-line no-console
			}

			if (error || loading) {
				return {error, loading, refetch};
			}

			result = Object.assign(mapper(data, context, ownProps), {
				error: null,
				loading: false,
				refetch
			});
		} catch (error) {
			result.error = error;
			console.error(error); // eslint-disable-line no-console
		}

		return result;
	};
}

/**
 * Get Variables
 * @description Method to return the formatted
 * variables to make the GraphQL request
 * @param {object} filters
 * @param {object} params
 * @param {string} rangeSelectors
 */
export function getVariables({filters, interval, params, rangeSelectors = {}}) {
	const {assetId, channelId, title = '', touchpoint = ''} = params;

	let variables = {
		title: decodeURIComponent(title),
		touchpoint:
			touchpoint !== 'Any' ? decodeURIComponent(touchpoint) : null,
		...getSafeRangeSelectors(rangeSelectors)
	};

	if (assetId) {
		variables = {
			...variables,
			assetId
		};
	}

	if (filters) {
		variables = {
			...variables,
			...getFilters(filters)
		};
	}

	if (interval) {
		variables = {
			...variables,
			interval
		};
	}

	if (channelId) {
		variables = {
			...variables,
			channelId
		};
	}

	return {variables};
}
