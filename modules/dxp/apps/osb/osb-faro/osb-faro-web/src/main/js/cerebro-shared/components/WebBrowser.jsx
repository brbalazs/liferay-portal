import autobind from 'autobind-decorator';
import Chart, {DONUT_CHART} from 'shared/components/Chart';
import getCN from 'classnames';
import NoResultsDisplay from 'shared/components/NoResultsDisplay';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import {
	getPercentage,
	groupData,
	removeNumbers,
	removeSpacing
} from 'shared/util/util';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';
import {TooltipChart} from './TooltipChart';
import {toRounded, toThousands} from 'shared/util/numbers';

const CLASSNAME = 'analytics-web-browser-chart';

const getPercentageChart = (value, total) =>
	toRounded(getPercentage(value, total));

const othersTooltipData = (data, total) => {
	const {group, id: title} = Object.assign(data, []).filter(
		({id}) => id == Liferay.Language.get('others')
	)[0];

	const others = {title, views: 0};

	const groupedItems = groupData(group, 10);

	others.items = groupedItems.map(item => {
		item.percentage = getPercentageChart(item.data[0], total);
		item.views = item.data[0];
		item.title = removeNumbers(item.id);

		others.views += item.views;

		return item;
	});

	others.percentage = getPercentageChart(others.views, total);

	if (group.length > 10) {
		others.groupedItems = Object.assign(
			others.items[others.items.length - 1],
			[]
		);
		others.items.splice(-1, 1);
	}

	return others;
};

/**
 * Web Browser
 */
class WebBrowser extends React.Component {
	static defaultProps = {
		browsers: []
	};

	static propTypes = {
		browsers: PropTypes.array
	};

	constructor(props) {
		super(props);

		this._legendElementRef = React.createRef();
	}

	componentDidMount() {
		this.legendElement = this._legendElementRef.current;
	}

	componentDidUpdate() {
		this.legendElement = this._legendElementRef.current;
	}

	/**
	 * Disable Legend Element
	 */
	disableLegendElement() {
		this.legendElement.classList.remove('enable-interaction');
	}

	/**
	 * Enable Legend Element
	 */
	enableLegendElement() {
		this.legendElement.classList.add('enable-interaction');
	}

	/**
	 * Handle Legend Move Over
	 */
	@autobind
	handleLegendMouseOver() {
		this.enableLegendElement();
	}

	/**
	 * Handle Legend Mouse Out
	 */
	@autobind
	handleLegendMouseOut() {
		this.disableLegendElement();
	}

	/**
	 * Handle Point Mouse Out
	 */
	@autobind
	handlePointMouseOut() {
		this.disableLegendElement();
	}

	/**
	 * Render Tooltip
	 * @param {array} content
	 */
	@autobind
	renderTooltip(content) {
		const {browsers, metricLabel, total} = this.props;
		let header = [];
		let rows = [];

		if (content[0].id == Liferay.Language.get('others')) {
			const {
				groupedItems,
				items,
				percentage,
				title,
				views
			} = othersTooltipData(browsers, total);

			header = [
				{
					label: title
				},
				{
					align: 'right',
					label: `${toThousands(views)} ${metricLabel}`
				},
				{
					align: 'right',
					label: `${toRounded(percentage)}%`
				}
			];

			rows = items.map(({percentage, title, views}) => ({
				columns: [
					{
						label: title,
						width: 100
					},
					{
						align: 'right',
						label: toThousands(views),
						width: 80
					},
					{
						align: 'right',
						label: `${toRounded(percentage)}%`,
						weight: 'semibold',
						width: 50
					}
				]
			}));

			if (groupedItems && groupedItems.group) {
				const {group} = groupedItems;

				rows.push({
					className: 'text-l-secondary',
					columns: [
						{
							label: `${group.length} more browsers`,
							width: 150
						},
						{
							align: 'right',
							label: toThousands(views)
						},
						{
							align: 'right',
							label: `${toRounded(percentage)}%`,
							weight: 'semibold'
						}
					]
				});
			}
		} else {
			header = [
				{
					label: removeNumbers(content[0].id)
				},
				{
					label: ''
				}
			];

			rows = [
				{
					columns: [
						{
							label: `${toThousands(
								content[0].value
							)} ${metricLabel}`,
							width: 120
						},
						{
							align: 'right',
							label: `${getPercentageChart(
								content[0].value,
								total
							)}%`,
							weight: 'semibold',
							width: 50
						}
					]
				}
			];
		}

		return ReactDOMServer.renderToString(
			<TooltipChart header={header} rows={rows} />
		);
	}

	/**
	 * Render Empty State Message
	 */
	renderEmptyState() {
		const {metricLabel} = this.props;

		return (
			<>
				<div className='col-7'>
					<div className={`${CLASSNAME}-donut-empty`} />
				</div>

				<NoResultsDisplay
					title={sub(Liferay.Language.get('empty-message-metric'), [
						metricLabel.toLowerCase()
					])}
				/>
			</>
		);
	}

	/**
	 * Render Legend Item
	 * @param {string} title
	 * @param {string} color
	 * @param {array} data
	 */
	@autobind
	renderLegendItem(title, color, data) {
		const {total} = this.props;

		const percentage = getPercentageChart(data[0].value, total);

		return `<li data-title=${removeSpacing(title)} class="two-columns">
				<div class='legend-template-column'>
					<span class='circle' style='background-color: ${color}'></span>
					<span class='text-truncate'>${removeNumbers(title)}</span>
				</div>
				<div class='legend-template-column justify-content-end'>${percentage}%</div>
			</li>`;
	}

	/**
	 * Render Chart
	 */
	renderChart() {
		const {browsers} = this.props;

		return (
			<div className={`${CLASSNAME}-donut`}>
				<Chart
					chartType={DONUT_CHART}
					data={browsers}
					dataId='webBrowserDataId'
					donut={{
						label: {
							show: false
						},
						padAngle: 0.03,
						width: 55
					}}
					id='webBrowserId'
					label={{
						ratio: 0.5
					}}
					legend={{
						contents: {
							bindto: '#webBrowserLegend',
							template: this.renderLegendItem
						},
						item: {
							onclick: () => false
						},
						show: true
					}}
					tooltip={{
						contents: this.renderTooltip
					}}
				/>
			</div>
		);
	}

	render() {
		const {empty} = this.props;

		const classes = getCN(
			CLASSNAME,
			'd-flex align-items-center w-100 position-relative'
		);

		return (
			<div className={classes}>
				{empty ? (
					this.renderEmptyState()
				) : (
					<>
						{this.renderChart()}

						<ul
							className={`${CLASSNAME}-legend legend-template`}
							id='webBrowserLegend'
							onBlur={this.handleLegendMouseOut}
							onFocus={this.handleLegendMouseOver}
							onMouseOut={this.handleLegendMouseOut}
							onMouseOver={this.handleLegendMouseOver}
							ref={this._legendElementRef}
						/>
					</>
				)}
			</div>
		);
	}
}

export {WebBrowser};
export default WebBrowser;
