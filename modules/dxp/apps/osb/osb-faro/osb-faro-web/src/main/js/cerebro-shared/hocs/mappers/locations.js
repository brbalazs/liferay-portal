import {getFilters} from 'shared/util/filter';
import {getLocationsData} from 'shared/util/charts';
import {getSafeRangeSelectors} from 'shared/util/util';
import {getVariables, safeResultToProps} from 'shared/util/mappers';

/**
 * MAPPER
 * @description Get Locations Mapper
 * @param {function} getMetric
 */
const getLocationsMapper = getMetric => {
	const mapResultToProps = safeResultToProps(
		(result, {filters}, {rangeSelectors}) => {
			let {geolocation} = getMetric(result);

			if (!geolocation || geolocation.length === 0) {
				return {empty: true};
			}

			const {location} = getFilters(filters);

			if (location !== 'Any') {
				geolocation = geolocation[0].metrics;
			}

			return {
				data: getLocationsData(geolocation, location),
				empty: false,
				...getSafeRangeSelectors(rangeSelectors)
			};
		}
	);

	/**
	 * Map Props to Options
	 * @param {object} param0 props
	 * @param {object} param1 context
	 */
	const mapPropsToOptions = ({filters, rangeSelectors, router: {params}}) =>
		getVariables({filters, params, rangeSelectors});

	return {
		options: mapPropsToOptions,
		props: mapResultToProps
	};
};

/**
 * MAPPER
 * @description Get Countries Mapper
 * @param {function} getMetric
 */
const getLocationsMapperCountries = getMetric => {
	const mapResultToProps = safeResultToProps(result => {
		const {geolocation} = getMetric(result);

		return {
			countries: getLocationsData(geolocation, location)
		};
	});

	/**
	 * Map Props to Options
	 * @param {object} param0 props
	 * @param {object} param1 context
	 */
	const mapPropsToOptions = ({filters, rangeSelectors, router: {params}}) => {
		const {variables} = getVariables({filters, params, rangeSelectors});

		return {
			variables: {
				...variables,
				location: 'Any'
			}
		};
	};

	return {
		options: mapPropsToOptions,
		props: mapResultToProps
	};
};
export {getLocationsMapper, getLocationsMapperCountries};
export default getLocationsMapper;
