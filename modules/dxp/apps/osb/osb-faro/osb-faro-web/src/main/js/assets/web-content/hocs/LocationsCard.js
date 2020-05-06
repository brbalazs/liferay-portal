import getLocationsMapper, {
	getLocationsMapperCountries
} from 'cerebro-shared/hocs/mappers/locations';
import WebContentMetricsQuery from '../queries/WebContentMetricsQuery';
import {graphql} from '@apollo/react-hoc';
import {withLocationsCard} from 'cerebro-shared/hocs/LocationsCard';

/**
 * HOC
 * @description Web Content Locations
 */
const withWebContentLocations = () =>
	graphql(
		WebContentMetricsQuery,
		getLocationsMapper(result => result.journal.viewsMetric)
	);

/**
 * HOC
 * @description Web Content Countries
 */
const withWebContentLocationsCountries = () =>
	graphql(
		WebContentMetricsQuery,
		getLocationsMapperCountries(result => result.journal.viewsMetric)
	);

export default withLocationsCard(
	withWebContentLocations,
	withWebContentLocationsCountries
);
