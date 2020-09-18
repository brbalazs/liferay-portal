import {Colors} from 'shared/util/charts';
import {getDeviceLabel} from 'shared/util/lang';
import {getPercentage} from 'shared/util/util';
import {getVariables, safeResultToProps} from 'shared/util/mappers';
import {groupData} from 'shared/util/util';

const formatOthersDevices = groupedData => {
	const others = Object.assign(groupedData[groupedData.length - 1], {});

	if (others.group.length - 1) {
		others.data = others.group.reduce((actual, next) => {
			if (actual.totalViews) {
				return actual.totalViews + next.totalViews;
			} else {
				return actual + next.totalViews;
			}
		});
	} else {
		others.data = others.group[0].totalViews;
	}

	return others;
};

const formatDevices = devices => {
	const MAX_SYSTEMS = 3;
	const data = [];
	const groups = [];
	const groupedData = groupData(devices, MAX_SYSTEMS);
	let others;

	if (groupedData.length > MAX_SYSTEMS) {
		others = formatOthersDevices(groupedData);

		groupedData.splice(-1, 1);
	}

	groupedData.sort((a, b) => b.totalViews - a.totalViews);

	const categories = groupedData.map((information, index) => {
		const categoryName =
			getDeviceLabel(information.type) || information.type;

		information.data.forEach((currentData, dataIndex) => {
			const groupItem = `data${dataIndex + 1}`;

			if (!data[dataIndex]) {
				data[dataIndex] = [groupItem];
				groups.push(groupItem);
			}

			for (
				let i = data[dataIndex].length;
				data[dataIndex].length - 1 < index;
				i++
			) {
				data[dataIndex].push(0);
			}

			data[dataIndex].push(groupedData[index].data[dataIndex].views);

			currentData.id = groupItem;
		});

		return categoryName;
	});

	if (others) {
		data[0].push(others.data);
		categories.push(Liferay.Language.get('other'));
	}

	return {
		categories,
		data,
		devices: groupedData,
		groups,
		others
	};
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
			devices: formatDevices(devices),
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
