import DocumentsAndMediaMetricsQuery from '../queries/DocumentsAndMediaMetricsQuery';
import getLocationsMapper, {
	getLocationsMapperCountries
} from 'cerebro-shared/hocs/mappers/locations';
import {graphql} from '@apollo/react-hoc';
import {withLocationsCard} from 'cerebro-shared/hocs/LocationsCard';

/**
 * HOC
 * @description Documents And Media Locations
 */
const withBlogsLocations = () =>
	graphql(
		DocumentsAndMediaMetricsQuery,
		getLocationsMapper(result => result.document.downloadsMetric)
	);

/**
 * HOC
 * @description Documents And Media Countries
 */
const withBlogsLocationsCountries = () =>
	graphql(
		DocumentsAndMediaMetricsQuery,
		getLocationsMapperCountries(result => result.document.downloadsMetric)
	);

export default withLocationsCard(
	withBlogsLocations,
	withBlogsLocationsCountries
);
