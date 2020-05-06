import autobind from 'autobind-decorator';
import ClayChart from 'clay-charts-react';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import TooltipChart from 'cerebro-shared/components/TooltipChart';
import {Colors, getAxisMeasuresFromData} from 'shared/util/charts';
import {getDeviceLabel, sub} from 'shared/util/lang';
import {hasChanges} from 'shared/util/react';
import {isUndefined} from 'lodash';
import {PropTypes} from 'prop-types';
import {toRounded, toThousands} from 'shared/util/numbers';

const CLASSNAME = 'analytics-operating-system-chart';
const MAX_SYSTEMS = 3;
const MIN_VALUE = '< 0.1%';

/**
 * Operating System
 * @class
 */
class OperatingSystem extends React.Component {
	static defaultProps = {
		devices: [],
		metricLabel: Liferay.Language.get('views')
	};

	static propTypes = {
		/**
		 * @type {?array}
		 * @default undefined
		 */
		devices: PropTypes.array,

		/**
		 * @type {?boolean}
		 * @default undefined
		 */
		empty: PropTypes.bool,

		/**
		 * @type {string}
		 * @default views
		 */
		metricLabel: PropTypes.string
	};

	constructor(props) {
		super(props);

		this._chartRef = React.createRef();
	}

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'devices')) {
			let groups = ['data1'];
			let values = [0];
			let y = 1;

			if (!this.isEmpty()) {
				const {intervals, maxValue} = getAxisMeasuresFromData(
					this.props.devices.map(({totalViews, type}) => [
						type,
						totalViews
					])
				);

				groups = this.props.groups;
				values = intervals;
				y = maxValue;
			}

			const {chart} = this._chartRef.current;

			chart.axis.max({y});

			chart.groups([groups]);

			chart.config('axis.y.tick.values', values, true);
		}
	}

	/**
	 * Align Tooltip
	 */
	@autobind
	alignTooltip(values, width, height) {
		const arrowPopoverSize = 6;
		const tooltipDistance = 8;

		const {layerX, layerY} = window.event;

		return {
			left: layerX - width / 2,
			top: layerY - (height + arrowPopoverSize + tooltipDistance)
		};
	}

	getAxisX() {
		const {categories} = this.props;

		const retVal = {categories: [' '], type: 'category'};

		if (!this.isEmpty()) {
			retVal.categories = categories;
		}

		return retVal;
	}

	getAxisY() {
		const {devices} = this.props;

		let retVal = {
			max: 1,
			min: 0,
			padding: {
				bottom: 0,
				top: 0
			},
			tick: {
				count: 1
			}
		};

		if (!this.isEmpty()) {
			const {intervals, maxValue} = getAxisMeasuresFromData(
				devices.map(({totalViews, type}) => [type, totalViews])
			);

			retVal = {
				...retVal,
				max: maxValue,
				tick: {
					count: 5,
					format: toThousands,
					values: intervals
				}
			};
		}

		return retVal;
	}

	/**
	 * Get Item Percentage
	 * @param {number} percentage
	 */
	getItemPercentage(percentage) {
		if (percentage < 0.1) {
			return `${MIN_VALUE}`;
		}

		return `${toRounded(percentage)}%`;
	}

	isEmpty() {
		const {categories, empty} = this.props;

		return empty && isUndefined(categories);
	}

	/**
	 * Format Tooltip Data
	 * @param {array} content
	 * @param {string} color
	 */
	formatTooltipData(content, color) {
		const {empty} = this.props;
		const {devices, metricLabel} = this.props;

		if (empty) {
			return ReactDOMServer.renderToString(
				<TooltipChart
					rows={[
						{
							columns: [
								{
									className: 'pt-0',
									label: sub(
										Liferay.Language.get(
											'empty-message-metric'
										),
										[metricLabel.toLowerCase()]
									)
								}
							]
						}
					]}
				/>
			);
		}

		const currentDevice = devices[content[0].x];
		const items = content
			.map(item => {
				if (!item) {
					return;
				}

				const data = currentDevice.data.filter(
					deviceData => deviceData.id == item.id
				)[0];

				if (data) {
					data.color = color(item.id);
				}

				return data;
			})
			.filter(item => item);

		items.sort((a, b) => b.views - a.views);

		const header = [
			{
				label: getDeviceLabel(currentDevice.type) || currentDevice.type
			},
			{
				align: 'right',
				label: `${toThousands(currentDevice.totalViews)} ${metricLabel}`
			},
			{
				align: 'right',
				label: `${toRounded(
					toRounded(currentDevice.percentageOfTotal)
				)}%`
			}
		];

		const rows = items.map(item => {
			const {color, percentage, type, views} = item;

			return {
				columns: [
					{
						color,
						label: type,
						width: 100
					},
					{
						align: 'right',
						label: toThousands(views),
						width: 80
					},
					{
						align: 'right',
						label: this.getItemPercentage(percentage),
						weight: 'semibold',
						width: 50
					}
				]
			};
		});

		return ReactDOMServer.renderToString(
			<TooltipChart header={header} rows={rows} />
		);
	}

	/**
	 * Format Grouped Tooltip Data
	 */
	formatGroupedTooltipData() {
		const {others} = this.props;
		const {metricLabel} = this.props;

		const header = [
			{
				label: Liferay.Language.get('others')
			},
			{
				label: ''
			}
		];

		const rows = [
			{
				columns: [
					{
						label: `${toThousands(others.data)} ${metricLabel}`,
						width: 120
					},
					{
						align: 'right',
						label: this.getItemPercentage(others.data),
						weight: 'semibold',
						width: 50
					}
				]
			}
		];

		return ReactDOMServer.renderToString(
			<TooltipChart header={header} rows={rows} />
		);
	}

	/**
	 * Render Tooltip
	 * @param {array} content
	 * @param {string} defaultTitleFormat
	 * @param {string} defaultValueFormat
	 * @param {string} color
	 */
	@autobind
	renderTooltip(content, defaultTitleFormat, defaultValueFormat, color) {
		if (content[0].x < MAX_SYSTEMS) {
			return this.formatTooltipData(content, color);
		}

		return this.formatGroupedTooltipData();
	}

	/**
	 * Lifecycle Render - ReactJS
	 */
	render() {
		const {data, groups} = this.props;

		return (
			<ClayChart
				axis={{
					x: this.getAxisX(),
					y: this.getAxisY()
				}}
				className={CLASSNAME}
				data={{
					colors: {
						data1: Colors.pallete[0],
						data2: Colors.pallete[1],
						data3: Colors.pallete[2],
						data4: Colors.pallete[3],
						data5: Colors.pallete[4],
						data6: Colors.pallete[5],
						data7: Colors.pallete[6],
						data8: Colors.pallete[7],
						data9: Colors.pallete[8]
					},
					columns: this.isEmpty() ? [['data1', 0]] : data,
					groups: this.isEmpty() ? [['data1']] : [groups],
					order: 'asc',
					type: 'bar'
				}}
				grid={{
					x: {
						lines: [{value: 4}]
					},
					y: {
						show: true
					}
				}}
				legend={{
					show: false
				}}
				padding={{
					top: 1
				}}
				ref={this._chartRef}
				tooltip={{
					contents: this.renderTooltip,
					position: this.alignTooltip
				}}
				unloadBeforeLoad
			/>
		);
	}
}

export default OperatingSystem;
