import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ChartTooltip from 'shared/components/ChartTooltip';
import FaroConstants, {LAST_30_DAYS} from 'shared/util/constants';
import getCN from 'classnames';
import PropTypes from 'prop-types';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {
	Area,
	AreaChart,
	CartesianGrid,
	Legend,
	ReferenceDot,
	ReferenceLine,
	ResponsiveContainer,
	Text,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {AXIS} from 'shared/util/clay-recharts';
import {
	changesListColumns,
	individualsListColumns
} from 'shared/util/table-columns';
import {CHART_COLOR_NAMES} from 'shared/components/Chart';
import {createDateKeysIMap} from 'shared/util/intervals';
import {DATE_CHANGED, NAME} from 'shared/util/pagination';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {formatXAxisDate, getIntervals} from 'shared/util/charts';
import {get, isNil, omit} from 'lodash';
import {getNetChange} from 'shared/util/change';
import {INDIVIDUALS} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withSelectedPoint, withStatefulPagination} from 'shared/hoc';

const {mormont: CHART_ORANGE, stark: CHART_BLUE} = CHART_COLOR_NAMES;

const INTERVAL = 'D';

const SearchableEntityTableHOC = withStatefulPagination(
	SearchableEntityTable,
	null,
	props => omit(props, 'onSearchValueChange')
);

const {orderAscending, orderDescending} = FaroConstants.pagination;

const CHANGES_AGGREGATION_SHAPE = PropTypes.arrayOf(
	PropTypes.shape({
		added: PropTypes.number,
		anonymousCount: PropTypes.number,
		knownCount: PropTypes.number,
		modifiedDate: PropTypes.number,
		removed: PropTypes.number,
		value: PropTypes.number
	})
).isRequired;

function getAllMembers(data) {
	const {channelId, delta, groupId, id, orderByFields, page, query} = data;

	return API.individuals.search({
		channelId,
		delta,
		groupId,
		individualSegmentId: id,
		orderByFields,
		page,
		query
	});
}

function getMemberChanges(data) {
	const {delta, groupId, id, modifiedDate, orderByFields, page, query} = data;

	return API.individualSegment.fetchMembershipChanges({
		cur: page,
		delta,
		endDate: modifiedDate,
		groupId,
		id,
		orderByFields,
		query,
		startDate: modifiedDate
	});
}

export class SegmentGrowthChart extends React.Component {
	static defaultProps = {
		alwaysShowSelectedTooltip: false,
		height: 360
	};

	static propTypes = {
		alwaysShowSelectedTooltip: PropTypes.bool,
		data: CHANGES_AGGREGATION_SHAPE,
		hasSelectedPoint: PropTypes.bool,
		height: PropTypes.number,
		onPointSelect: PropTypes.func,
		selectedPoint: PropTypes.number
	};

	state = {
		legendHoverItem: null,
		mouseOutside: false,
		selectedTooltipX: null
	};

	constructor(props) {
		super(props);

		this._tooltipRef = React.createRef();
	}

	@autobind
	renderTooltip({active, payload}) {
		const {data, hasSelectedPoint, selectedPoint} = this.props;

		if ((active && !!payload.length) || hasSelectedPoint) {
			const {
				added,
				anonymousCount,
				knownCount,
				modifiedDate,
				removed,
				value
			} = get(payload, [0, 'payload'], data[selectedPoint]);

			const change = [
				{
					label: Liferay.Language.get('added'),
					value: added
				},
				{
					label: Liferay.Language.get('removed'),
					value: removed
				}
			];

			const index = data.findIndex(
				item => item.modifiedDate === modifiedDate
			);

			const netChange = getNetChange(
				get(data[index - 1], 'value'),
				value
			);

			return (
				<div
					className='bb-tooltip-container'
					style={{position: 'static'}}
				>
					<ChartTooltip
						items={
							isNil(netChange)
								? change
								: [
										...change,
										{
											label: Liferay.Language.get(
												'net-change'
											),
											value: `${netChange[0]}(${
												netChange[1]
											}%)`
										}
								  ]
						}
						subtitle={[
							sub(
								Liferay.Language.get('x-total-members'),
								[<b key='VALUE'>{value.toLocaleString()}</b>],
								false
							),
							sub(
								Liferay.Language.get('x-anonymous-members'),
								[
									<b key='VALUE'>
										{anonymousCount.toLocaleString()}
									</b>
								],
								false
							),
							sub(
								Liferay.Language.get('x-known-members'),
								[
									<b key='VALUE'>
										{knownCount.toLocaleString()}
									</b>
								],
								false
							)
						].map((subtitle, i) => (
							<div key={i}>{subtitle}</div>
						))}
						title={sub(
							Liferay.Language.get('as-of-x'),
							[formatUTCDateFromUnix(modifiedDate, 'll')],
							false
						)}
					/>
				</div>
			);
		}
	}

	render() {
		const {
			props: {
				alwaysShowSelectedTooltip,
				data,
				hasSelectedPoint,
				height,
				onPointSelect,
				selectedPoint
			},
			state: {legendHoverItem, mouseOutside, selectedTooltipX}
		} = this;

		const commonAreaChartStyles = {
			isAnimationActive: true,
			legendType: 'circle',
			stackId: 'count'
		};

		const showFixedTooltip = hasSelectedPoint && mouseOutside;

		const dateKeysIMap = createDateKeysIMap(INTERVAL, data, 'modifiedDate');

		const intervals = getIntervals(
			LAST_30_DAYS,
			data.map(({modifiedDate}) => modifiedDate),
			INTERVAL,
			dateKeysIMap
		);

		return (
			<ResponsiveContainer height={height} width='100%'>
				<AreaChart
					data={data}
					onClick={pointData => {
						if (alwaysShowSelectedTooltip && pointData) {
							if (this._tooltipRef) {
								const {
									getTranslate,
									props: {viewBox},
									state: {boxWidth}
								} = this._tooltipRef.current;

								this.setState({
									selectedTooltipX: getTranslate({
										key: 'x',
										tooltipDimension: boxWidth,
										viewBoxDimension: viewBox.width
									})
								});
							}

							onPointSelect({
								index: pointData.activeTooltipIndex
							});
						}
					}}
					onMouseLeave={() => this.setState({mouseOutside: true})}
					onMouseMove={() => this.setState({mouseOutside: false})}
				>
					<CartesianGrid
						stroke={AXIS.gridStroke}
						strokeDasharray='3 3'
						vertical={false}
					/>

					<XAxis
						axisLine={{stroke: AXIS.borderStroke}}
						dataKey='modifiedDate'
						domain={['dataMin', 'dataMax']}
						padding={{left: 20, right: 20}}
						tick={({payload: {value}, textAnchor, x, y}) => (
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
								{formatXAxisDate(
									value,
									LAST_30_DAYS,
									INTERVAL,
									dateKeysIMap
								)}
							</Text>
						)}
						tickLine={false}
						tickMargin={12}
						ticks={intervals}
					/>

					<XAxis
						axisLine={{stroke: AXIS.borderStroke}}
						dataKey='modifiedDate'
						orientation='top'
						stroke={AXIS.gridStroke}
						tick={false}
						tickLine={false}
						xAxisId='top'
					/>

					<YAxis
						allowDecimals={false}
						axisLine={{stroke: AXIS.borderStroke}}
						label={{
							fill: AXIS.textColor,
							offset: 20,
							position: 'top',
							value: Liferay.Language.get('growth')
						}}
						name={Liferay.Language.get('growth')}
						stroke={AXIS.gridStroke}
						tick={({
							payload: {offset, value},
							textAnchor,
							x,
							y
						}) => (
							<Text
								style={{
									fill: AXIS.textColor,
									font: AXIS.font,
									fontSize: '0.75rem'
								}}
								textAnchor={textAnchor}
								x={x}
								y={y + offset}
							>
								{value}
							</Text>
						)}
						tickCount={6}
						tickLine={false}
						type='number'
					/>

					<YAxis
						axisLine={{stroke: AXIS.borderStroke}}
						orientation='right'
						stroke={AXIS.gridStroke}
						tick={false}
						tickLine={false}
						type='number'
						width={1}
						yAxisId='right'
					/>

					<Legend
						align='right'
						iconSize={8}
						onMouseEnter={({dataKey}) =>
							this.setState({legendHoverItem: dataKey})
						}
						onMouseLeave={() =>
							this.setState({legendHoverItem: null})
						}
						verticalAlign='bottom'
						wrapperStyle={{
							bottom: 0,
							color: AXIS.textColor,
							fontSize: '14px',
							lineHeight: '21px',
							right: 0
						}}
					/>

					<Tooltip
						content={this.renderTooltip}
						cursor={{stroke: CHART_BLUE}}
						position={
							showFixedTooltip
								? {
										x: selectedTooltipX
								  }
								: null
						}
						ref={this._tooltipRef}
						wrapperStyle={
							showFixedTooltip
								? {
										visibility: 'visible'
								  }
								: null
						}
					/>

					<ReferenceLine
						strokeWidth={1}
						x={
							showFixedTooltip
								? data[selectedPoint].modifiedDate
								: null
						}
					/>

					<ReferenceDot
						fill={CHART_BLUE}
						fillOpacity={
							legendHoverItem === 'anonymousCount' ? 0.1 : 1
						}
						isFront
						r={4}
						stroke='none'
						x={
							hasSelectedPoint
								? data[selectedPoint].modifiedDate
								: null
						}
						y={
							hasSelectedPoint
								? data[selectedPoint].knownCount
								: null
						}
					/>

					<ReferenceDot
						fill={CHART_ORANGE}
						fillOpacity={legendHoverItem === 'knownCount' ? 0.1 : 1}
						isFront
						r={4}
						stroke='none'
						x={
							hasSelectedPoint
								? data[selectedPoint].modifiedDate
								: null
						}
						y={
							hasSelectedPoint
								? data[selectedPoint].knownCount +
								  data[selectedPoint].anonymousCount
								: null
						}
					/>

					<Area
						{...commonAreaChartStyles}
						activeDot={{r: 4, stroke: CHART_BLUE}}
						dataKey='knownCount'
						fill={CHART_BLUE}
						fillOpacity={
							legendHoverItem === 'anonymousCount' ? 0.1 : 0.2
						}
						isAnimationActive={false}
						name={Liferay.Language.get('known-members')}
						stroke={CHART_BLUE}
						strokeOpacity={
							legendHoverItem === 'anonymousCount' ? 0.2 : 1
						}
					/>

					<Area
						{...commonAreaChartStyles}
						activeDot={{r: 4, stroke: CHART_ORANGE}}
						dataKey='anonymousCount'
						fill={CHART_ORANGE}
						fillOpacity={
							legendHoverItem === 'knownCount' ? 0.1 : 0.2
						}
						isAnimationActive={false}
						name={Liferay.Language.get('anonymous-members')}
						stroke={CHART_ORANGE}
						strokeOpacity={
							legendHoverItem === 'knownCount' ? 0.2 : 1
						}
					/>
				</AreaChart>
			</ResponsiveContainer>
		);
	}
}

export class SelectedPointInfo extends React.Component {
	static propTypes = {
		data: CHANGES_AGGREGATION_SHAPE,
		hasSelectedPoint: PropTypes.bool,
		onClearSelection: PropTypes.func.isRequired,
		selectedPoint: PropTypes.number
	};

	render() {
		const {
			data,
			hasSelectedPoint,
			onClearSelection,
			selectedPoint
		} = this.props;

		const {added, modifiedDate, removed} = get(data, selectedPoint, {});

		const changeValues =
			hasSelectedPoint &&
			selectedPoint > 0 &&
			getNetChange(data[selectedPoint - 1], data[selectedPoint]);

		return (
			<div className='selected-point-info'>
				{hasSelectedPoint ? (
					<>
						<div className='d-flex align-items-baseline'>
							<h4>
								{sub(
									Liferay.Language.get(
										'segment-membership-activities-x'
									),
									[formatUTCDateFromUnix(modifiedDate)]
								)}
							</h4>

							<Button
								display='link'
								onClick={onClearSelection}
								size='sm'
							>
								{Liferay.Language.get('clear-date-selection')}
							</Button>
						</div>

						<div className='changed-values'>
							{sub(
								Liferay.Language.get('added-x'),
								[<b key='ADDED'>{added}</b>],
								false
							)}

							{sub(
								Liferay.Language.get('removed-x'),
								[<b key='REMOVED'>{removed}</b>],
								false
							)}

							{changeValues &&
								sub(
									Liferay.Language.get('net-change-x'),
									[
										<b key='CHANGE'>
											{`${changeValues[0]}(${
												changeValues[1]
											}%)`}
										</b>
									],
									false
								)}
						</div>
					</>
				) : (
					<h4>{Liferay.Language.get('known-members')}</h4>
				)}
			</div>
		);
	}
}

export class SegmentGrowthWithList extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		data: CHANGES_AGGREGATION_SHAPE,
		groupId: PropTypes.any.isRequired,
		hasSelectedPoint: PropTypes.bool,
		id: PropTypes.string.isRequired,
		selectedPoint: PropTypes.number
	};

	@autobind
	fetchMembers(params) {
		const {hasSelectedPoint} = this.props;

		const fetchMembersFn = hasSelectedPoint
			? getMemberChanges
			: getAllMembers;

		return fetchMembersFn(params);
	}

	getColumns() {
		const {channelId, groupId, hasSelectedPoint} = this.props;

		if (hasSelectedPoint) {
			return [
				changesListColumns.getIndividualName({channelId, groupId}),
				changesListColumns.individualEmail,
				individualsListColumns.accountNames,
				changesListColumns.dateFirst,
				changesListColumns.operation
			];
		}

		return [
			individualsListColumns.getName({channelId, groupId}),
			individualsListColumns.email,
			individualsListColumns.accountNames,
			individualsListColumns.dateCreated
		];
	}

	@autobind
	handleClearSelection() {
		const {onPointSelect} = this.props;

		onPointSelect({index: null});
	}

	render() {
		const {
			channelId,
			className,
			data,
			groupId,
			hasSelectedPoint,
			id,
			onPointSelect,
			selectedPoint
		} = this.props;

		const {modifiedDate} = get(
			data,
			get(selectedPoint, ['activeTooltipIndex']),
			{}
		);

		return (
			<Card.Body
				className={getCN('segment-growth-root', className)}
				noPadding
			>
				<div className='segment-growth-chart-container'>
					<SegmentGrowthChart
						alwaysShowSelectedTooltip
						data={data}
						hasSelectedPoint={hasSelectedPoint}
						onPointSelect={onPointSelect}
						selectedPoint={selectedPoint}
					/>
				</div>

				<SelectedPointInfo
					data={data}
					hasSelectedPoint={hasSelectedPoint}
					onClearSelection={this.handleClearSelection}
					selectedPoint={selectedPoint}
				/>

				<SearchableEntityTableHOC
					columns={this.getColumns()}
					dataSourceFn={this.fetchMembers}
					dataSourceParams={{channelId, groupId, id, modifiedDate}}
					defaultSort={{
						field: hasSelectedPoint ? DATE_CHANGED : NAME,
						sortOrder: hasSelectedPoint
							? orderDescending
							: orderAscending
					}}
					entityType={hasSelectedPoint ? '' : INDIVIDUALS}
					rowIdentifier='id'
				/>
			</Card.Body>
		);
	}
}

export default withSelectedPoint(SegmentGrowthWithList);
