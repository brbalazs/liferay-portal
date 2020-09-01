/* eslint-disable jsx-a11y/click-events-have-key-events */

import autobind from 'autobind-decorator';
import CardTabs from 'shared/components/CardTabs';
import Checkbox from 'shared/components/Checkbox';
import getCN from 'classnames';
import MetricValue from 'cerebro-shared/components/MetricValue';
import PropTypes from 'prop-types';
import React, {Fragment} from 'react';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import Trend from 'cerebro-shared/components/Trend';
import {AXIS, getTextWidth} from 'shared/util/clay-recharts';
import {
	Bar,
	CartesianGrid,
	Cell,
	ComposedChart,
	Legend,
	Line,
	ResponsiveContainer,
	Text,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {find, get} from 'lodash';
import {
	formatXAxisDate,
	getLegendCircle,
	getLegendLine,
	getLegendLineDashed
} from 'shared/util/charts';
import {getDateTitle} from 'shared/util/charts';
import {LAST_24_HOURS, YESTERDAY} from 'shared/util/constants';
import {Map} from 'immutable';
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
		chartHeight: 350,
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
		hoverIndex: -1
	};

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
	 * Render Chart
	 */
	renderChart() {
		const {
			chartHeight: height,
			interval,
			rangeSelectors,
			showPrevious
		} = this.props;

		const {content, data, dateKeysIMap, intervals} = this.getActiveItem();
		const {name, title} = content;

		const timeline = data[data.length - 1];

		const chartData = data.slice(0, data.length - 1);

		const dataIds = chartData.map(item => item.id);

		let yAxisWidth = 60;

		const combinedData = timeline.data.map((date, i) =>
			dataIds.reduce(
				(acc, item, j) => {
					acc[item] = chartData[j].data[i];

					const textWidth = getTextWidth(
						this.renderFormat(chartData[j].data[i])
					);

					const labelWidth = getTextWidth(
						METRIC_TOOLTIP_LABEL_MAP[name] || title
					);

					if (yAxisWidth < textWidth) {
						yAxisWidth = textWidth;
					}

					if (yAxisWidth < labelWidth) {
						yAxisWidth = labelWidth;
					}

					return acc;
				},
				{
					date,
					dateString: formatXAxisDate(
						date,
						rangeSelectors.rangeKey,
						interval,
						dateKeysIMap
					)
				}
			)
		);

		const renderTick = ({payload, textAnchor, x, y}) => (
			<Text
				style={{
					fill: AXIS.textColor,
					font: AXIS.font,
					fontSize: '0.75rem'
				}}
				textAnchor={textAnchor}
				width={yAxisWidth}
				x={x}
				y={y + payload.offset}
			>
				{payload.value}
			</Text>
		);

		const barData = chartData.filter(item => item.type === 'bar');

		const lineData = chartData.filter(item => {
			if (!showPrevious && item.id === CHART_DATA_PREVIOUS) {
				return;
			}

			return item.type !== 'bar';
		});

		return (
			<ResponsiveContainer height={height}>
				<ComposedChart data={combinedData}>
					<CartesianGrid
						stroke={AXIS.gridStroke}
						strokeDasharray='3 3'
						vertical={false}
					/>

					<XAxis
						axisLine={{
							stroke: AXIS.borderStroke
						}}
						dataKey='dateString'
						stroke={AXIS.gridStroke}
						tick={({payload, textAnchor, x, y}) => (
							<Text
								style={{
									fill: AXIS.textColor,
									font: AXIS.font,
									fontSize: '0.75rem'
								}}
								textAnchor={textAnchor}
								x={x}
								y={y}
							>
								{payload.value}
							</Text>
						)}
						tickLine={false}
						tickMargin={12}
						ticks={intervals.map(int =>
							formatXAxisDate(
								int,
								rangeSelectors.rangeKey,
								interval,
								dateKeysIMap
							)
						)}
					/>

					<XAxis
						axisLine={{
							stroke: AXIS.borderStroke
						}}
						dataKey='dateString'
						orientation='top'
						stroke={AXIS.gridStroke}
						tick={false}
						tickLine={false}
						xAxisId='top'
					/>

					<YAxis
						axisLine={{
							stroke: AXIS.borderStroke
						}}
						label={{
							offset: 20,
							position: 'top',
							value: METRIC_TOOLTIP_LABEL_MAP[name] || title
						}}
						stroke={AXIS.gridStroke}
						tick={({payload, y, ...others}) =>
							renderTick({
								payload: {
									...payload,
									offset: 4,
									value: this.renderFormat(payload.value)
								},
								y,
								...others
							})
						}
						tickLine={false}
						width={yAxisWidth}
					/>

					<YAxis
						axisLine={{
							stroke: AXIS.borderStroke
						}}
						orientation='right'
						stroke={AXIS.gridStroke}
						tick={false}
						tickLine={false}
						width={12}
						yAxisId='right'
					/>

					<Tooltip content={this.renderTooltip} />

					<Legend
						align='right'
						iconSize={8}
						verticalAlign='bottom'
						wrapperStyle={{
							bottom: 'auto',
							color: AXIS.textColor,
							fontSize: '14px',
							lineHeight: '21px'
						}}
					/>

					{barData.map(item => (
						<Bar
							dataKey={item.id}
							fill={item.color}
							key={item.id}
							legendType='circle'
							name={item.name}
							onMouseEnter={(e, index) =>
								this.setState({hoverIndex: index})
							}
							onMouseLeave={() => this.setState({hoverIndex: -1})}
							stackId='a'
						>
							{item.data.map((entry, index) => (
								<Cell
									fill={item.color}
									key={`cell-${index}`}
									opacity={
										index === this.state.hoverIndex
											? 0.75
											: 1
									}
								/>
							))}
						</Bar>
					))}

					{lineData.map(item => (
						<Line
							dataKey={item.id}
							dot={false}
							fill={item.color}
							key={item.id}
							legendType='plainline'
							name={item.name}
							stroke={item.color}
							strokeDasharray={
								item.id === CHART_DATA_PREVIOUS
									? '5 5'
									: undefined
							}
							strokeWidth={2}
							type='linear'
						/>
					))}
				</ComposedChart>
			</ResponsiveContainer>
		);
	}

	/**
	 * Render Tooltip
	 * @param {Array} dataPoints
	 */
	@autobind
	renderTooltip({active, payload}) {
		if (!active) {
			return null;
		}

		const {interval, rangeSelectors, showPrevious} = this.props;

		const activeChartIndex = 0;
		const dateKey = payload[0].payload.date;

		const {
			compositeData,
			content: {name, title},
			data,
			dateKeysIMap,
			format,
			prevDateKeysIMap
		} = this.getActiveItem();

		const dataOneItemData = find(data, ({id}) => id === CHART_DATA_ID_1);
		const dataOneValue = payload[0].value;

		const dataTwoItemData = find(data, ({id}) => id === CHART_DATA_ID_2);
		const dataTwoValue = payload[1] && payload[1].value;

		const dataPreviousPoint = find(
			payload,
			({dataKey}) => dataKey === CHART_DATA_PREVIOUS
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

		return (
			<div className='bb-tooltip-container' style={{position: 'static'}}>
				<TooltipChart header={header} rows={rows} />
			</div>
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
			props: {onShowPreviousChange, showPrevious}
		} = this;

		const {
			content: {name}
		} = this.getActiveItem();

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
