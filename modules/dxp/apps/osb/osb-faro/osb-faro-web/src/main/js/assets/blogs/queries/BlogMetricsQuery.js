import {
	AUDIENCE_REPORT_FRAGMENT,
	BROWSER_FRAGMENT,
	DEVICE_FRAGMENT,
	GEOLOCATION_FRAGMENT,
	METRIC_FRAGMENT
} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query BlogsMetrics(
		$assetId: String!
		$channelId: String
		$devices: String
		$location: String
		$rangeKey: Int
		$title: String
		$touchpoint: String!
	) {
		blog(
			assetId: $assetId
			channelId: $channelId
			country: $location
			deviceType: $devices
			rangeKey: $rangeKey
			title: $title
			url: $touchpoint
		) {
			assetId
			assetTitle
			urls
			commentsMetric {
				...metricFragment
			}
			ratingsMetric {
				...metricFragment
			}
			readingTimeMetric {
				...metricFragment
			}
			viewsMetric {
				...audienceReportFragment
				...browserFragment
				...deviceFragment
				...geolocationFragment
				...metricFragment

				previousValue
				value
			}
		}
	}

	${AUDIENCE_REPORT_FRAGMENT}
	${BROWSER_FRAGMENT}
	${DEVICE_FRAGMENT}
	${GEOLOCATION_FRAGMENT}
	${METRIC_FRAGMENT}
`;
