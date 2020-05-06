import {getFilters} from 'shared/util/filter';

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
 * @param {string} rangeKey
 * @returns {variables: {...}}
 */
export function getVariables({filters, interval, params, rangeKey}) {
	const {assetId, channelId, title = '', touchpoint = ''} = params;

	let variables = {
		rangeKey: parseInt(rangeKey),
		title: decodeURIComponent(title),
		touchpoint: decodeURIComponent(touchpoint)
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

	return {
		variables
	};
}
