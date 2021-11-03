import getLocationsMapper, {
	getLocationsMapperCountries
} from 'cerebro-shared/hocs/mappers/locations';
import TouchpointMetricsQuery from 'shared/queries/TouchpointMetricsQuery';
import {graphql} from '@apollo/react-hoc';
import {withLocationsCard} from 'cerebro-shared/hocs/LocationsCard';

/**
 * HOC
 * @description Touchpoint Locations
 */
const withTouchpointLocations = () =>
	graphql(
		TouchpointMetricsQuery,
		getLocationsMapper(result => result.page.viewsMetric)
	);

/**
 * HOC
 * @description Touchpoint Countries
 */
const withTouchpointsLocationsCountries = () =>
	graphql(
		TouchpointMetricsQuery,
		getLocationsMapperCountries(result => result.page.viewsMetric)
	);

export default withLocationsCard(
	withTouchpointLocations,
	withTouchpointsLocationsCountries
);
