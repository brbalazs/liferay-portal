import {Colors} from 'shared/util/charts';
import {getDeviceLabel} from 'shared/util/lang';
import {getPercentage} from 'shared/util/util';
import {getVariables, safeResultToProps} from 'shared/util/mappers';
import {groupData} from 'shared/util/util';

const MAX_SYSTEMS = 3;

const groupDeviceData = (data, max) => {
	if (data.length <= max) {
		return data;
	}

	const otherData = data.slice(max).reduce(
		(actual, next) => ({
			...actual,
			data: [
				{
					type: Liferay.Language.get('other'),
					views:
						actual.data[0].views +
						next.data.reduce((acc, next) => acc + next.views, 0)
				}
			],
			percentageOfTotal: actual.percentageOfTotal + next.percentageOfTotal
		}),
		{
			data: [{views: 0}],
			percentageOfTotal: 0,
			type: Liferay.Language.get('other')
		}
	);

	return [
		...data.slice(0, max).map(group => ({
			...group,
			label: getDeviceLabel(group.type)
		})),
		{
			...otherData,
			label: Liferay.Language.get('others')
		}
	];
};

/**
 * Format Browsers
 * @param {array} browsers
 * @returns {array}
 */
const formatBrowsers = browsers => {
	const data = browsers.map((browser, index) => ({
		color: Colors.pallete[index] || null,
		data: [browser.value],
		id: index + browser.valueKey
	}));

	data.sort((a, b) => b.data[0] - a.data[0]);

	const groupedData = groupData(data, 8);

	if (groupedData.length) {
		const groupSize = groupedData.length - 1;

		groupedData[groupSize].color = Colors.pallete[groupSize];
	}

	return groupedData;
};

/**
 * MAPPER
 * @description Get Devices Mapper
 * @param {function} getMetric
 */
const getDevicesMapper = getMetric => {
	const mapResultToProps = safeResultToProps(result => {
		const metric = getMetric(result);

		if (
			!metric.browser ||
			!metric.device ||
			metric.browser.length === 0 ||
			metric.device.length === 0
		) {
			return {empty: true};
		}

		const devices = metric.device.map(device => {
			const data = device.metrics.map(({value, valueKey}) => ({
				percentage: getPercentage(value, metric.value),
				type: valueKey,
				views: value
			}));

			return {
				data,
				percentageOfTotal: getPercentage(device.value, metric.value),
				totalViews: device.value,
				type: device.valueKey
			};
		});

		return {
			browsers: formatBrowsers(metric.browser),
			devices: groupDeviceData(devices, MAX_SYSTEMS),
			empty: false,
			total: metric.value
		};
	});

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

export {getDevicesMapper};
export default getDevicesMapper;
