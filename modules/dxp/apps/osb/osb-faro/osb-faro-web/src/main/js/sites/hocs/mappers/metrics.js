import {getMetricsData, getSiteMetricsChartData} from 'shared/util/metrics';
import {safeResultToProps} from 'shared/util/mappers';

/**
 * MAPPER
 * @description Get Metrics Mapper
 * @param {function} getData
 * @param {array} metrics
 */
const getMetricsMapper = (getData, metrics) => {
	const mapResultToProps = safeResultToProps(
		(result, context, {interval, rangeKey}) => ({
			items: getMetricsData(
				getData(result),
				metrics,
				rangeKey,
				getSiteMetricsChartData,
				interval
			)
		})
	);

	/**
	 * Map Props to Options
	 * @param {object} param0 props
	 * @param {object} param1 context
	 */
	const mapPropsToOptions = ({
		channelId,
		interval,
		rangeKey,
		router: {params}
	}) => {
		const customDateRange = rangeKey && rangeKey.start && rangeKey.end;

		console.log(rangeKey);

		return {
			variables: {
				channelId: channelId || params.channelId,
				endDate: customDateRange ? rangeKey.end : null,
				interval,
				rangeKey: customDateRange ? null : parseInt(rangeKey),
				startDate: customDateRange ? rangeKey.start : null
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
