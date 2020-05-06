import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Card from 'shared/components/Card';
import Chart, {CHART_COLOR_NAMES, SPLINE_CHART} from 'shared/components/Chart';
import ChartTooltip from 'shared/components/ChartTooltip';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import moment from 'moment';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {
	findLastIndex,
	get,
	head,
	isFinite,
	isNil,
	isNull,
	noop
} from 'lodash/fp';
import {formatChange, getFinitePercentChange} from 'shared/util/change';
import {formatEngagementScore} from 'shared/util/engagement';
import {formatUTCDateFromUnix, getLastDate} from 'shared/util/date';
import {getLegendCircle} from 'shared/util/charts';
import {omit} from 'lodash';
import {PropTypes} from 'prop-types';
import {SCORE} from 'shared/util/pagination';
import {sub} from 'shared/util/lang';
import {withSelectedPoint, withStatefulPagination} from 'shared/hoc';

const {
	pagination: {orderDescending}
} = FaroConstants;

const {martell: CHART_GREEN} = CHART_COLOR_NAMES;

const CHART_ID = 'engagement';
const CHART_DATA_ID = 'engagement-score';

const EngagementTable = withStatefulPagination(
	SearchableEntityTable,
	{
		defaultOrderBy: orderDescending,
		defaultOrderByField: SCORE
	},
	props => omit(props, 'onSearchValueChange')
);

function getMembersEngagement({
	delta,
	endDate,
	entityType,
	groupId,
	id,
	orderByFields,
	page,
	query,
	startDate
}) {
	return API.engagement
		.fetch({
			contactsEntityId: id,
			contactsEntityType: entityType,
			cur: page,
			delta,
			endDate,
			groupId,
			includeAnonymousUsers: true,
			orderByFields,
			query,
			startDate
		})
		.then(({items, total}) => ({
			items: items.map(({score, ...otherParams}) => ({
				score: formatEngagementScore(score),
				...otherParams
			})),
			total
		}));
}

function getNetChangeLabel(curVal, prevVal) {
	const change = curVal - prevVal;

	const percentChange = Math.abs(getFinitePercentChange(curVal, prevVal));

	return (
		<span
			className={getCN('net-change', {
				decrease: change < 0,
				increase: change > 0
			})}
			key='NET_CHANGE'
		>
			<b>{formatChange(change)}</b>

			{!isNil(percentChange) && `(${percentChange}%)`}
		</span>
	);
}

export class EngagementChart extends React.Component {
	static propTypes = {
		data: PropTypes.arrayOf(
			PropTypes.shape({
				contributors: PropTypes.number,
				intervalInitDate: PropTypes.number,
				scoreAvg: PropTypes.number
			})
		).isRequired,
		hasSelectedPoint: PropTypes.bool,
		onPointSelect: PropTypes.func,
		selectedPoint: PropTypes.number,
		tooltipLabels: PropTypes.shape({
			scoreLabel: PropTypes.string,
			subtitleLabel: PropTypes.string
		}).isRequired
	};

	constructor() {
		super();

		this._chartRef = React.createRef();
	}

	@autobind
	setInitialPoint() {
		const {
			data,
			hasSelectedPoint,
			onPointSelect,
			selectedPoint
		} = this.props;

		if (onPointSelect && data.length) {
			const lastIndex = findLastIndex(point => !isNull(point.scoreAvg))(
				data
			);

			const indexToSelect = hasSelectedPoint ? selectedPoint : lastIndex;

			this._chartRef.current.select([indexToSelect]);

			onPointSelect({index: indexToSelect});
		}
	}

	@autobind
	getHTMLTooltipString(data) {
		const {index} = head(data);

		const {scoreLabel, subtitleLabel} = this.props.tooltipLabels;

		const {contributors = 0, intervalInitDate, scoreAvg} = this.props.data[
			index
		];

		return ReactDOMServer.renderToString(
			<ChartTooltip
				items={[
					{
						label: scoreLabel,
						value: scoreAvg.toFixed(2)
					}
				]}
				subtitle={sub(
					subtitleLabel,
					[<b key='MEMBERSHIP'>{contributors.toLocaleString()}</b>],
					false
				)}
				title={sub(Liferay.Language.get('as-of-x'), [
					formatUTCDateFromUnix(intervalInitDate, 'll')
				])}
			/>
		);
	}

	render() {
		const {data, onPointSelect} = this.props;

		return (
			<>
				<Chart
					alwaysShowSelectedTooltip
					axisX={{
						categories: data.map(item =>
							item.intervalInitDate.toString()
						),
						tick: {
							centered: false,
							format: dateObj =>
								moment.utc(dateObj).format('M/D'),
							multiline: true,
							outer: false
						},
						type: 'timeseries'
					}}
					axisY={{min: 0, padding: {bottom: 0}}}
					chartType={SPLINE_CHART}
					className='engagement-chart-root'
					data={[
						{
							data: data.map(item => item.intervalInitDate),
							id: 'date'
						},
						{
							color: CHART_GREEN,
							data: data.map(item => item.scoreAvg),
							id: CHART_DATA_ID,
							name: Liferay.Language.get('engagement')
						}
					]}
					dataId={CHART_DATA_ID}
					id={CHART_ID}
					legend={{
						contents: {
							bindto: '#legend-engagement',
							template: (_, color) =>
								`<li class="chart-legend-item">${getLegendCircle(
									color
								)} ${Liferay.Language.get('engagement')}</li>`
						},
						item: {
							onclick: () => false
						},
						show: true
					}}
					onafterinit={this.setInitialPoint}
					onPointSelect={onPointSelect}
					otherData={{colors: {[CHART_DATA_ID]: CHART_GREEN}}}
					ref={this._chartRef}
					splineInterpolationType='monotone-x'
					tooltip={{
						contents: this.getHTMLTooltipString
					}}
					unloadBeforeLoad={false}
					x='date'
					yLabel={Liferay.Language.get('engagement')}
				/>

				<div className='chart-legend' id='legend-engagement'></div>
			</>
		);
	}
}

export class SelectedPointInfo extends React.Component {
	static propTypes = {
		data: PropTypes.arrayOf(
			PropTypes.shape({
				contributors: PropTypes.number,
				intervalInitDate: PropTypes.number,
				scoreAvg: PropTypes.number
			})
		).isRequired,
		previousScore: PropTypes.number.isRequired,
		scoreLabel: PropTypes.string.isRequired,
		selectedPoint: PropTypes.number
	};

	getIntervalChange(index) {
		const {data, previousScore} = this.props;

		const prevVal =
			index === 0 ? previousScore : get([index - 1, 'scoreAvg'], data);

		const curVal = get([index, 'scoreAvg'], data);

		return getNetChangeLabel(curVal, prevVal);
	}

	render() {
		const {data, scoreLabel, selectedPoint} = this.props;

		const {intervalInitDate = 0, scoreAvg = 0} = data[selectedPoint] || {};

		return (
			<div className='selected-point-info'>
				<h4>
					{sub(Liferay.Language.get('engaged-members-as-of-x'), [
						formatUTCDateFromUnix(intervalInitDate)
					])}
				</h4>

				<div className='secondary-info'>
					<div className='score'>
						{`${scoreLabel} `}
						{
							<b key='SCORE'>
								{isFinite(scoreAvg) && scoreAvg.toFixed(2)}
							</b>
						}
					</div>

					<div className='changed-values'>
						{sub(
							Liferay.Language.get('x-vs-previous-day'),
							[this.getIntervalChange(selectedPoint)],
							false
						)}
					</div>
				</div>
			</div>
		);
	}
}

export class EngagementWithList extends React.Component {
	static defaultProps = {
		checkDisabledFn: noop
	};

	static propTypes = {
		checkDisabledFn: PropTypes.func,
		columns: PropTypes.array,
		data: PropTypes.arrayOf(
			PropTypes.shape({
				contributors: PropTypes.number,
				intervalInitDate: PropTypes.number,
				scoreAvg: PropTypes.number
			})
		).isRequired,
		entityType: PropTypes.number,
		groupId: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
			.isRequired,
		hasSelectedPoint: PropTypes.bool,
		id: PropTypes.string.isRequired,
		onPointSelect: PropTypes.func.isRequired,
		previousScore: PropTypes.number.isRequired,
		selectedPoint: PropTypes.number,
		tooltipLabels: PropTypes.shape({
			scoreLabel: PropTypes.string,
			subtitleLabel: PropTypes.string
		}).isRequired
	};

	getDateRange() {
		const {data, hasSelectedPoint, selectedPoint} = this.props;

		if (!hasSelectedPoint) {
			return {
				endDate: getLastDate(data, 'intervalInitDate'),
				startDate: getLastDate(data, 'intervalInitDate')
			};
		}

		const intervalInitDate =
			get('intervalInitDate', data[selectedPoint]) || null;

		return {endDate: intervalInitDate, startDate: intervalInitDate};
	}

	render() {
		const {
			checkDisabledFn,
			className,
			columns,
			data,
			entityType,
			groupId,
			id,
			onPointSelect,
			previousScore,
			selectedPoint,
			tooltipLabels
		} = this.props;

		return (
			<Card.Body
				className={getCN('engagement-chart-list-root', className)}
				noPadding
			>
				<EngagementChart
					data={data}
					onPointSelect={onPointSelect}
					selectedPoint={selectedPoint}
					tooltipLabels={tooltipLabels}
				/>

				<SelectedPointInfo
					data={data}
					previousScore={previousScore}
					scoreLabel={tooltipLabels.scoreLabel}
					selectedPoint={selectedPoint}
				/>

				<EngagementTable
					checkDisabled={checkDisabledFn}
					columns={columns}
					dataSourceFn={getMembersEngagement}
					dataSourceParams={{
						...this.getDateRange(),
						entityType,
						groupId,
						id
					}}
					defaultSort={{
						field: SCORE,
						sortOrder: orderDescending
					}}
					rowIdentifier='id'
				/>
			</Card.Body>
		);
	}
}

export default withSelectedPoint(EngagementWithList);
