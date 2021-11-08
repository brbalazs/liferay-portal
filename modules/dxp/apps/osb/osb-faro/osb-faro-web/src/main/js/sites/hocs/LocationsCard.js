import getLocationsMapper, {
	getLocationsMapperCountries
} from 'cerebro-shared/hocs/mappers/locations';
import SessionLocationsQuery from 'shared/queries/SessionLocationsQuery';
import {graphql} from '@apollo/react-hoc';
import {withLocationsCard} from 'cerebro-shared/hocs/LocationsCard';

/**
 * HOC
 * @description Site Locations
 */
const withSiteLocations = () =>
	graphql(
		SessionLocationsQuery,
		getLocationsMapper(result => result.site.sessionsMetric)
	);

/**
 * HOC
 * @description Site Countries
 */
const withSiteLocationsCountries = () =>
	graphql(
		SessionLocationsQuery,
		getLocationsMapperCountries(result => result.site.sessionsMetric)
	);

export default withLocationsCard(withSiteLocations, withSiteLocationsCountries);
