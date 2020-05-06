import {
	AUDIENCE_REPORT_FRAGMENT,
	BROWSER_FRAGMENT,
	DEVICE_FRAGMENT,
	GEOLOCATION_FRAGMENT,
	METRIC_FRAGMENT
} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query TouchpointMetrics(
		$channelId: String
		$title: String
		$touchpoint: String!
		$rangeKey: Int!
		$location: String
		$devices: String
	) {
		page(
			channelId: $channelId
			url: $touchpoint
			rangeKey: $rangeKey
			country: $location
			deviceType: $devices
			title: $title
		) {
			assetTitle
			avgTimeOnPageMetric {
				...metricFragment
			}
			bounceRateMetric {
				...metricFragment
			}
			engagementMetric {
				...metricFragment
			}
			entrancesMetric {
				...metricFragment
			}
			exitRateMetric {
				...metricFragment
			}
			visitorsMetric {
				...metricFragment
			}
			viewsMetric {
				...audienceReportFragment
				...browserFragment
				...deviceFragment
				...metricFragment
				...geolocationFragment

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
