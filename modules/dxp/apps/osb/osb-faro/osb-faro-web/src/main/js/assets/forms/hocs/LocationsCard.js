import FormMetricsQuery from '../queries/FormMetricsQuery';
import getLocationsMapper, {
	getLocationsMapperCountries
} from 'cerebro-shared/hocs/mappers/locations';
import {graphql} from '@apollo/react-hoc';
import {withLocationsCard} from 'cerebro-shared/hocs/LocationsCard';

/**
 * HOC
 * @description Forms Locations
 */
const withFormsLocations = () =>
	graphql(
		FormMetricsQuery,
		getLocationsMapper(result => result.form.submissionsMetric)
	);

/**
 * HOC
 * @description Forms Countries
 */
const withFormsLocationsCountries = () =>
	graphql(
		FormMetricsQuery,
		getLocationsMapperCountries(result => result.form.submissionsMetric)
	);

export default withLocationsCard(
	withFormsLocations,
	withFormsLocationsCountries
);
