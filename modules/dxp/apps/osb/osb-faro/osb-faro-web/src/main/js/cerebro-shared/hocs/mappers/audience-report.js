import {CHART_COLORS} from 'shared/components/Chart';
import {getPercentage} from 'shared/util/util';
import {getVariables, safeResultToProps} from 'shared/util/mappers';
import {sub} from 'shared/util/lang';
import {toRounded, toThousands} from 'shared/util/numbers';

/**
 * Handle Colors
 * @param {object} param
 * @description It is necessary to get the ordered color defined by the Lexicon
 */
const handleColors = ({knownIndividualsData, uniqueVisitorsData}) => {
	const COLORS = [...CHART_COLORS];

	const uniqueVisitorsArr = uniqueVisitorsData.map(metric => {
		if (metric.count) {
			metric.color = COLORS.shift();
		}

		return metric;
	});

	const knownIndividualsArr = knownIndividualsData.map(metric => {
		if (metric.count) {
			metric.color = COLORS.shift();
		}

		return metric;
	});

	return {
		knownIndividualsArr,
		uniqueVisitorsArr
	};
};

/**
 * Get Donut Data based on Metrics
 * @param {object} metrics
 * @param {string} id
 */
const getDonutData = (metrics, id) => {
	const total = metrics.reduce((total, metric) => total + metric.count, 0);

	return {
		data: metrics
			.filter(({count}) => count > 0)
			.map(({color, count, label}, index) => ({
				color,
				data: [count],
				id: index + label
			})),
		empty: {
			show: total === 0
		},
		id,
		total
	};
};

/**
 * Get formatted Segments Data
 * @param {object} param
 * @param {string} color
 */
const getSegmentsData = ({segments, total, totalOthers}, color) => {
	const MAX_BARS = 6;
	const MAX_VALUE_EMPTY_STATE = 30;
	const TOOLTIP_HEADER = [
		{
			label: Liferay.Language.get('segment'),
			weight: 'semibold'
		},
		{
			label: ''
		},
		{
			align: 'right',
			label: '%',
			weight: 'semibold'
		}
	];

	/**
	 * Get formatted tooltip column
	 * @param {object} item
	 */
	const getTooltipColumns = ({value, valueKey}) => [
		{
			label:
				valueKey === 'others'
					? Liferay.Language.get('other-segments')
					: valueKey,
			truncated: true,
			weight: 'semibold',
			width: 160
		},
		{
			align: 'right',
			label: `${toThousands(value)}`
		},
		{
			align: 'right',
			label: `${toRounded(getPercentage(value, total))}%`,
			weight: 'semibold',
			width: 50
		}
	];

	/**
	 * Convert value to percentage based on total
	 * @param {number} value
	 */
	const getValue = value => parseInt(toRounded(getPercentage(value, total)));

	/**
	 * Sum all the keys value of the array
	 * @param {array} arr
	 */
	const sumArrValues = arr =>
		arr.map(({value}) => value).reduce((a, b) => a + b);

	let items = segments.slice(0, MAX_BARS).map(({value, valueKey}) => ({
		columns: [
			{
				icon: 'ac-segment',
				label: valueKey
			}
		],
		progress: [
			{
				color,
				value: getValue(value)
			}
		],
		tooltip: {
			header: TOOLTIP_HEADER,
			rows: [
				{
					columns: getTooltipColumns({value, valueKey})
				}
			]
		}
	}));

	// Max value
	let maxValue = Math.max(...segments.map(({value}) => value));

	if (segments.length > MAX_BARS) {
		const otherArrItems = segments.slice(MAX_BARS - 1);

		const value = sumArrValues(otherArrItems);

		items = [
			...items.slice(0, MAX_BARS - 1),
			{
				columns: [
					{
						icon: 'ac-segment',
						label: sub(Liferay.Language.get('x-more-segments'), [
							totalOthers - (MAX_BARS - 1)
						])
					}
				],
				progress: [
					{
						color,
						value: getValue(value)
					}
				],
				tooltip: {
					header: TOOLTIP_HEADER,
					rows: otherArrItems.map(item => ({
						columns: getTooltipColumns(item)
					}))
				}
			}
		];

		// Update max value
		maxValue = value > maxValue ? value : maxValue;
	}

	return {
		disableScroll: true,
		formatSpacement: false,
		grid: {
			maxValue: segments.length
				? getValue(maxValue)
				: MAX_VALUE_EMPTY_STATE,
			minValue: 0,
			show: true,
			type: 'percentage'
		},
		items
	};
};

/**
 * MAPPER
 * @description Get Segments Mapper
 * @param {function} getMetric
 */
const getAudienceReportMapper = (getMetric, pathUrl) => {
	const mapResultToProps = safeResultToProps(result => {
		const {
			anonymousUsersCount,
			knownUsersCount,
			nonsegmentedKnownUsersCount,
			segment: {metrics, total: totalOthers},
			segmentedKnownUsersCount
		} = getMetric(result);

		const knownIndividualsData = [
			{
				count: nonsegmentedKnownUsersCount,
				label: Liferay.Language.get('non-segmented')
			},
			{
				count: segmentedKnownUsersCount,
				id: 'segmented',
				label: Liferay.Language.get('segmented')
			}
		].sort((a, b) => (a.count > b.count ? -1 : 1));

		const uniqueVisitorsData = [
			{
				count: anonymousUsersCount,
				label: Liferay.Language.get('anonymous-individuals')
			},
			{
				count: knownUsersCount,
				label: Liferay.Language.get('known-individuals')
			}
		].sort((a, b) => (a.count > b.count ? -1 : 1));

		const {knownIndividualsArr, uniqueVisitorsArr} = handleColors({
			knownIndividualsData,
			uniqueVisitorsData
		});

		const segments = getSegmentsData(
			{
				segments: [
					...metrics
						.sort((a, b) => b.value - a.value)
						.filter(({valueKey}) => valueKey !== 'others'),
					...metrics.filter(({valueKey}) => valueKey === 'others')
				],
				total: segmentedKnownUsersCount,
				totalOthers
			},
			knownIndividualsArr.find(({id}) => id === 'segmented').color ||
				CHART_COLORS[0]
		);

		let knownIndividuals = getDonutData(
			knownIndividualsArr,
			'known-individuals'
		);
		let uniqueVisitors = getDonutData(uniqueVisitorsArr, 'unique-visitors');

		knownIndividuals = {
			...knownIndividuals,
			empty: {
				...knownIndividuals.empty,
				message: sub(
					Liferay.Language.get(
						'x-known-individuals-interacted-with-this-content'
					),
					[0]
				)
			}
		};

		uniqueVisitors = {
			...uniqueVisitors,
			empty: {
				...uniqueVisitors.empty,
				message: sub(
					Liferay.Language.get(
						'x-visitors-interacted-with-this-content'
					),
					[0]
				)
			}
		};

		return {
			knownIndividuals,
			pathUrl,
			segments,
			uniqueVisitors
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

export {getAudienceReportMapper};
export default getAudienceReportMapper;
