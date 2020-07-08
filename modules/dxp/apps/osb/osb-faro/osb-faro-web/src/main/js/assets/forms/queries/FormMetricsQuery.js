import {
	AUDIENCE_REPORT_FRAGMENT,
	BROWSER_FRAGMENT,
	DEVICE_FRAGMENT,
	GEOLOCATION_FRAGMENT,
	METRIC_FRAGMENT
} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query FormsMetrics(
		$assetId: String!
		$channelId: String
		$devices: String
		$location: String
		$rangeKey: Int
		$title: String
		$touchpoint: String!
	) {
		form(
			assetId: $assetId
			channelId: $channelId
			country: $location
			deviceType: $devices
			rangeKey: $rangeKey
			title: $title
			url: $touchpoint
		) {
			abandonmentsMetric {
				...metricFragment
			}
			assetId
			assetTitle
			completionTimeMetric {
				...metricFragment
			}
			formPageMetrics {
				formFieldMetrics {
					fieldAbandonmentsMetric {
						value
					}
					fieldEmptyMetric {
						value
					}
					fieldInteractionDurationMetric {
						value
					}
					fieldInteractionsMetric {
						value
					}
					fieldName
					fieldRefilledMetric {
						value
					}
				}
				pageAbandonmentsMetric {
					value
				}
				pageIndex
				pageViewsMetric {
					value
				}
			}
			submissionsMetric {
				...audienceReportFragment
				...browserFragment
				...deviceFragment
				...geolocationFragment
				...metricFragment
			}
			urls
			viewsMetric {
				...metricFragment
			}
		}
	}

	${AUDIENCE_REPORT_FRAGMENT}
	${BROWSER_FRAGMENT}
	${DEVICE_FRAGMENT}
	${GEOLOCATION_FRAGMENT}
	${METRIC_FRAGMENT}
`;
