import BlogMetricsQuery from '../queries/BlogMetricsQuery';
import getLocationsMapper, {
	getLocationsMapperCountries
} from 'cerebro-shared/hocs/mappers/locations';
import {graphql} from '@apollo/react-hoc';
import {withLocationsCard} from 'cerebro-shared/hocs/LocationsCard';

/**
 * HOC
 * @description Blogs Locations
 */
const withBlogsLocations = () =>
	graphql(
		BlogMetricsQuery,
		getLocationsMapper(result => result.blog.viewsMetric)
	);

/**
 * HOC
 * @description Blogs Countries
 */
const withBlogsLocationsCountries = () =>
	graphql(
		BlogMetricsQuery,
		getLocationsMapperCountries(result => result.blog.viewsMetric)
	);

export default withLocationsCard(
	withBlogsLocations,
	withBlogsLocationsCountries
);
