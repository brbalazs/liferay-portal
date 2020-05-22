/* eslint-disable jsx-a11y/click-events-have-key-events */
import autobind from 'autobind-decorator';
import CardTabs from 'shared/components/CardTabs';
import Chart, {COMBINED_CHART} from 'shared/components/Chart';
import Checkbox from 'shared/components/Checkbox';
import getCN from 'classnames';
import MetricValue from 'cerebro-shared/components/MetricValue';
import React, {Fragment} from 'react';
import ReactDOMServer from 'react-dom/server';
import Spinner from 'shared/components/Spinner';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import Trend from 'cerebro-shared/components/Trend';
import {find, get} from 'lodash';
import {
	formatXAxisDate,
	getLegendCircle,
	getLegendLine,
	getLegendLineDashed,
	isEmptyData
} from 'shared/util/charts';
import {
	getAxisMeasuresFromCompositeData,
	getAxisMeasuresFromData,
	getDateTitle
} from 'shared/util/charts';
import {hasChanges} from 'shared/util/react';
import {LAST_24_HOURS, YESTERDAY} from 'shared/util/constants';
import {Map} from 'immutable';
import {PropTypes} from 'prop-types';
import {toInt} from 'shared/util/numbers';

const CLASSNAME = 'analytics-metrics';
export const CHART_DATA_ID_1 = 'data_1';
export const CHART_DATA_ID_2 = 'data_2';
export const CHART_DATA_PREVIOUS = 'data_previous';
export const METRIC_TOOLTIP_LABEL_MAP = {
	bounceRateMetric: Liferay.Language.get('avg-bounce'),
	engagementMetric: Liferay.Language.get('avg-engagement')
};

/**
 * Tooltip Label Title
 * @param {string} rangeKey
 */
export const tooltipLabelTitle = rangeKey => {
	let label;

	if (rangeKey === LAST_24_HOURS || rangeKey === YESTERDAY) {
		label = Liferay.Language.get('time');
	} else {
		label = Liferay.Language.get('date');
	}

	return label;
};

/**
 * Main Metrics
 */
class MainMetrics extends React.Component {
	static defaultProps = {
		activeItemIndex: 0,
		chartHeight: 320,
		items: [],
		showPrevious: false,
		showTabs: true
	};

	static propTypes = {
		activeItemIndex: PropTypes.number,
		chartHeight: PropTypes.number,
		/**
		 * @type {array}
		 * @default undefined
		 */
		items: PropTypes.arrayOf(
			PropTypes.shape({
				active: PropTypes.bool,
				content: PropTypes.shape({
					details: PropTypes.shape({
						color: PropTypes.string,
						icon: PropTypes.string,
						label: PropTypes.string
					}),
					name: PropTypes.string,
					title: PropTypes.string,
					type: PropTypes.string,
					value: PropTypes.string
				}),
				data: PropTypes.arrayOf(
					PropTypes.shape({
						color: PropTypes.string,
						data: PropTypes.array,
						id: PropTypes.string.isRequired,
						name: PropTypes.string
					})
				),
				dateKeysIMap: PropTypes.instanceOf(Map),
				format: PropTypes.func,
				intervals: PropTypes.array,
				prevDateKeysIMap: PropTypes.instanceOf(Map)
			})
		),
		/**
		 * Callback for when activeItemIndex changes.
		 */
		onActiveItemIndexChange: PropTypes.func,
		rangeSelectors: PropTypes.object,

		showTabs: PropTypes.bool
	};

	state = {
		legend: {},
		loading: false
	};

	constructor(props) {
		super(props);

		this._chartRef = React.createRef();
	}

	/**
	 * Lifecycle Component Did Update - ReactJS
	 * @param {object} nextProps
	 */
	componentDidUpdate(prevProps) {
		if (
			hasChanges(
				prevProps,
				this.props,
				'activeItemIndex',
				'filters',
				'item',
				'rangeSelectors',
				'showPrevious'
			)
		) {
			this.loading();
		}
	}

	/**
	 * Lifecycle Component Will Unmount - ReactJS
	 */
	componentWillUnmount() {
		if (
			this._chartRef &&
			this._chartRef.current &&
			this._chartRef.current._chartRef.current
		) {
			this._chartRef.current._chartRef.current.destroyChart();
		}
	}

	/**
	 * Build Tabs
	 */
	buildTabs() {
		const {activeItemIndex, items} = this.props;

		return items.map(({content}, index) => {
			const isActiveTab = activeItemIndex === index;

			const {details, title, type, value} = content;

			const {color, icon, label} = details;

			return {
				onClick: isActiveTab ? undefined : this.handleClickTab(index),
				secondaryInfo: (
					<span>
						<span className='primary-content'>
							<MetricValue type={type} value={value} />
						</span>

						{label && (
							<Trend color={color} icon={icon} label={label} />
						)}
					</span>
				),
				tabId: index,
				title
			};
		});
	}

	/**
	 * Handle Click Tab
	 * @param {object} event
	 */
	@autobind
	handleClickTab(index) {
		const {onActiveItemIndexChange} = this.props;

		return () => {
			this.loading();

			if (onActiveItemIndexChange) {
				onActiveItemIndexChange(toInt(index));
			}
		};
	}

	/**
	 * Get Active Item
	 */
	getActiveItem() {
		const {activeItemIndex = 0, items = []} = this.props;

		if (items.length === 0) {
			return {
				data: [],
				intervals: []
			};
		}

		if (activeItemIndex < 0 || activeItemIndex >= items.length) {
			return items[0];
		}

		return items[activeItemIndex];
	}

	/**
	 * Loading
	 */
	loading() {
		// This loading state is to force disposing the chart. If we dispose
		// it directly, the chart will blink. So I figured it would be
		// better to display a quick loading indicator. Ideally, the chart
		// API would not need to be disposed and re-created in order to
		// render correctly :/

		this.setState({loading: true}, () =>
			setTimeout(() => this.setState({loading: false}), 125)
		);
	}

	/**
	 * Render Chart
	 */
	renderChart() {
		const {
			chartHeight: height,
			interval,
			rangeSelectors,
			showPrevious
		} = this.props;

		const {
			compositeData,
			content,
			data,
			dateKeysIMap,
			intervals
		} = this.getActiveItem();
		const {name, title} = content;

		const stackedBarChart = !!compositeData;

		const chartData = data
			.filter(({id}) =>
				showPrevious
					? [
							CHART_DATA_ID_1,
							CHART_DATA_ID_2,
							CHART_DATA_PREVIOUS
					  ].includes(id)
					: [CHART_DATA_ID_1, CHART_DATA_ID_2].includes(id)
			)
			.map(data => [data.id].concat(data.data));

		let maxValue = 1;
		const tickY = {
			format: this.renderFormat
		};
		const empty = isEmptyData(chartData.map(data => data.slice(1)));

		if (!empty) {
			let intervalsY = [];

			({intervals: intervalsY = [], maxValue} = stackedBarChart
				? getAxisMeasuresFromCompositeData(
						chartData.map(data => data.slice(1))
				  )
				: getAxisMeasuresFromData(chartData));

			tickY.values = intervalsY;
		} else {
			tickY.count = 5;
			tickY.format = value => (value === 0 ? this.renderFormat(0) : '');
		}

		return (
			<Chart
				axisX={{
					tick: {
						centered: false,
						format: date =>
							formatXAxisDate(
								date,
								rangeSelectors.rangeKey,
								interval,
								dateKeysIMap
							),
						values: intervals
					},
					type: 'timeseries'
				}}
				axisY={{
					max: maxValue,
					min: 0,
					padding: {
						bottom: 0,
						top: 0
					},
					tick: tickY
				}}
				bar={{
					width: {
						ratio: 0.9
					}
				}}
				chartType={COMBINED_CHART}
				className={name}
				data={
					showPrevious
						? data
						: data.filter(({id}) => id !== CHART_DATA_PREVIOUS)
				}
				dataId={`${name}Data`}
				generateChartOnLoad
				height={height}
				id={`${name}LineChartMetricsData`}
				legend={{
					contents: {
						bindto: `#Legend-${name}`,
						template: this.renderLegends
					},
					item: {
						onclick: () => false
					},
					show: true
				}}
				line={{
					classes: stackedBarChart
						? ['bb-line-dashed-4-4']
						: ['bb-line', 'bb-line-dashed-4-4']
				}}
				otherData={
					stackedBarChart
						? {
								groups: [[CHART_DATA_ID_1, CHART_DATA_ID_2]],
								order: null
						  }
						: undefined
				}
				padding={{
					right: 20,
					top: 1
				}}
				ref={this._chartRef}
				tooltip={{
					contents: this.renderTooltip
				}}
				x='x'
				yLabel={METRIC_TOOLTIP_LABEL_MAP[name] || title}
			/>
		);
	}

	/**
	 * Render Tooltip
	 * @param {Array} dataPoints
	 */
	@autobind
	renderTooltip(dataPoints) {
		const {interval, rangeSelectors, showPrevious} = this.props;

		const activeChartIndex = get(dataPoints[0], 'index') || 0;
		const dateKey = dataPoints[0].x;

		const {
			compositeData,
			content: {name, title},
			data,
			dateKeysIMap,
			format,
			prevDateKeysIMap
		} = this.getActiveItem();

		const dataOneItemData = find(data, ({id}) => id === CHART_DATA_ID_1);
		const dataOneValue = dataOneItemData.data[activeChartIndex];

		const dataTwoItemData = find(data, ({id}) => id === CHART_DATA_ID_2);
		const dataTwoValue = get(dataTwoItemData, ['data', activeChartIndex]);

		const dataPreviousPoint = find(
			dataPoints,
			({id}) => id === CHART_DATA_PREVIOUS
		);

		const dataOnePreviousValue = compositeData
			? get(compositeData, [
					get(dataOneItemData, 'dataName'),
					activeChartIndex,
					'previousValue'
			  ])
			: get(dataPreviousPoint, 'value');

		const dataTwoPreviousValue = get(compositeData, [
			get(dataTwoItemData, 'dataName'),
			activeChartIndex,
			'previousValue'
		]);

		const currentPeriodTitle = getDateTitle(
			dateKeysIMap.get(dateKey),
			rangeSelectors.rangeKey,
			interval
		);
		const previousPeriodTitle = getDateTitle(
			prevDateKeysIMap.get(dateKey),
			rangeSelectors.rangeKey,
			interval
		);

		const header = [
			{label: title, weight: 'semibold', width: 100},
			showPrevious && {
				align: 'right',
				label: previousPeriodTitle,
				weight: 'normal',
				width: 55
			},
			{
				align: 'right',
				label: currentPeriodTitle,
				weight: 'semibold',
				width: 55
			}
		].filter(Boolean);

		const getDataRowName = itemData =>
			get(itemData, 'tooltipTitle') ||
			METRIC_TOOLTIP_LABEL_MAP[name] ||
			get(itemData, 'name');

		const rows = [
			{
				columns: [
					{
						label: getDataRowName(dataOneItemData),
						weight: showPrevious ? 'semibold' : 'normal'
					},
					showPrevious && {
						align: 'right',
						label: format(dataOnePreviousValue)
					},
					{
						align: 'right',
						label: format(dataOneValue),
						weight: 'semibold'
					}
				].filter(Boolean)
			},
			compositeData && {
				columns: [
					{
						label: getDataRowName(dataTwoItemData),
						weight: showPrevious ? 'semibold' : 'normal'
					},
					showPrevious && {
						align: 'right',
						label: format(dataTwoPreviousValue)
					},
					{
						align: 'right',
						label: format(dataTwoValue),
						weight: 'semibold'
					}
				].filter(Boolean)
			},
			compositeData && {
				columns: [
					{
						label: Liferay.Language.get('total'),
						weight: showPrevious ? 'semibold' : 'normal'
					},
					showPrevious && {
						align: 'right',
						label: format(
							dataOnePreviousValue + dataTwoPreviousValue
						)
					},
					{
						align: 'right',
						label: format(dataOneValue + dataTwoValue),
						weight: 'semibold'
					}
				].filter(Boolean)
			}
		].filter(Boolean);

		return ReactDOMServer.renderToString(
			<TooltipChart header={header} rows={rows} />
		);
	}

	/**
	 * Render Format
	 * @param {string} value
	 */
	@autobind
	renderFormat(value) {
		return this.getActiveItem().format(value);
	}

	/**
	 * Render Legends
	 * @param {number} id
	 * @param {string} color
	 */
	@autobind
	renderLegends(id, color) {
		const {compositeData, data} = this.getActiveItem();

		const name = get(find(data, d => d.id === id), 'name');
		let icon;

		if (compositeData) {
			icon =
				id === CHART_DATA_PREVIOUS
					? getLegendLineDashed(color)
					: getLegendCircle(color);
		} else if (id === CHART_DATA_PREVIOUS) {
			icon = getLegendLineDashed(color);
		} else if (id === CHART_DATA_ID_1) {
			icon = getLegendLine(color);
		}

		return `<li class="chart-legend-item">${icon} ${name}</li>`;
	}

	renderItems() {
		const {
			props: {items, onShowPreviousChange, showPrevious},
			state: {loading}
		} = this;

		const {
			content: {name}
		} = this.getActiveItem();

		if (loading) {
			return <Spinner alignCenter key='LOADING' />;
		} else if (items) {
			return (
				<Fragment key='CHART'>
					{this.renderChart()}

					<div className={`${CLASSNAME}-chart-sub-content-wrapper`}>
						<Checkbox
							checked={showPrevious}
							label={Liferay.Language.get('compare-to-previous')}
							onChange={() => onShowPreviousChange(!showPrevious)}
						/>

						<ul
							className={`${CLASSNAME}-legend chart-legend`}
							id={`Legend-${name}`}
						/>
					</div>
				</Fragment>
			);
		}
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {activeItemIndex, className, showTabs} = this.props;

		return (
			<div className={getCN(CLASSNAME, className)}>
				{showTabs && (
					<CardTabs
						activeTabId={activeItemIndex}
						className='analytics-metrics-tabs'
						tabs={this.buildTabs()}
					/>
				)}

				<div className={`${CLASSNAME}-chart`}>{this.renderItems()}</div>
			</div>
		);
	}
}

export default MainMetrics;
