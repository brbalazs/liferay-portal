import {getMetricsData} from 'shared/util/metrics';
import {getVariables, safeResultToProps} from 'shared/util/mappers';

/**
 * MAPPER
 * @description Get Metrics Mapper
 * @param {function} getData
 * @param {array} metrics
 */
const getMetricsMapper = (getData, metrics, chartDataMapFn) => {
	const mapResultToProps = safeResultToProps(
		(result, context, {rangeKey}) => ({
			items: getMetricsData(
				getData(result),
				metrics,
				rangeKey,
				chartDataMapFn
			)
		})
	);

	/**
	 * Map Props to Options
	 * @param {object} param0 props
	 * @param {object} param1 context
	 */
	const mapPropsToOptions = ({
		assetId: assetIdProps,
		filters,
		interval,
		rangeKey,
		router: {params}
	}) => {
		const {variables} = getVariables({filters, interval, params, rangeKey});

		return {
			variables: {
				...variables,
				assetId: assetIdProps || params.assetId
			}
		};
	};

	return {
		options: mapPropsToOptions,
		props: mapResultToProps
	};
};

export default getMetricsMapper;
export {getMetricsMapper};
