import {COHORT_HEATMAP_FRAGMENT} from 'shared/queries/fragments';
import {gql} from 'apollo-boost';

export default gql`
	query CohortHeatMap(
		$channelId: String
		$interval: String!
		$timeZone: String!
	) {
		site(
			channelId: $channelId
			includePrevious: false
			timeZoneId: $timeZone
			interval: $interval
		) {
			anonymousVisitorsMetric {
				...cohortHeatMapFragment
			}

			knownVisitorsMetric {
				...cohortHeatMapFragment
			}

			visitorsMetric {
				...cohortHeatMapFragment
			}
		}
	}

	${COHORT_HEATMAP_FRAGMENT}
`;
