import {setUriQueryValue, toRoute} from 'shared/util/router';

/**
 * Get URL
 * @param {string} path
 * @param {object} router
 */
export const getUrl = (path, {params, query}) => {
	const {rangeKey} = query;
	const {assetId, channelId, groupId, id, title, touchpoint} = params;

	const routeParams = {
		assetId,
		channelId,
		groupId,
		id,
		title,
		touchpoint
	};

	return rangeKey
		? setUriQueryValue(toRoute(path, routeParams), 'rangeKey', rangeKey)
		: toRoute(path, routeParams);
};
