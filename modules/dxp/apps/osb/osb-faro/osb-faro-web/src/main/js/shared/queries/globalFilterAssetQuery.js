import {DEVICE_FRAGMENT, GEOLOCATION_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

/**
 * Global Filter Asset Query
 * @description Create a GraphQL query
 * @param {string} queryName
 * @param {string} metricName
 * @returns GraphQL query
 */
export default (queryName, metricName) => gql`
		query GlobalFilterAssetQuery($assetId: String!, $channelId: String, $title: String, $touchpoint: String!, $rangeKey: Int!) {
			${queryName}(assetId: $assetId, channelId: $channelId, title: $title, url: $touchpoint, rangeKey: $rangeKey) {
				${metricName} {
					...deviceFragment
					...geolocationFragment
				}
			}
		}

		${DEVICE_FRAGMENT}
		${GEOLOCATION_FRAGMENT}
	`;
