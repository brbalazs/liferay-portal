import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ChartTooltip from 'shared/components/ChartTooltip';
import FaroConstants from 'shared/util/constants';
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
import {DATE_CHANGED, NAME} from 'shared/util/pagination';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {get, isNil, omit} from 'lodash';
import {getNetChange} from 'shared/util/change';
import {INDIVIDUALS} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withSelectedPoint, withStatefulPagination} from 'shared/hoc';

const {mormont: CHART_ORANGE, stark: CHART_BLUE} = CHART_COLOR_NAMES;

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
		height: PropTypes.number,
		onPointSelect: PropTypes.func,
		selectedPoint: PropTypes.shape({
			activeCoordinate: PropTypes.shape({
				x: PropTypes.number,
				y: PropTypes.number
			}),
			activeLabel: PropTypes.any,
			activePayload: PropTypes.arrayOf(PropTypes.object),
			activeTooltipIndex: PropTypes.number,
			chartX: PropTypes.number,
			chartY: PropTypes.number
		})
	};

	state = {
		legendHoverItem: null,
		mouseOutside: false
	};

	@autobind
	renderTooltip({active, payload}) {
		const {data, selectedPoint} = this.props;

		if (active || (selectedPoint && !!selectedPoint.activePayload.length)) {
			const {
				payload: {
					added,
					anonymousCount,
					knownCount,
					modifiedDate,
					removed,
					value
				}
			} = payload[0] || selectedPoint.activePayload[0];

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
				height,
				onPointSelect,
				selectedPoint
			},
			state: {legendHoverItem, mouseOutside}
		} = this;

		const commonAreaChartStyles = {
			isAnimationActive: true,
			legendType: 'circle',
			stackId: 'count'
		};

		return (
			<ResponsiveContainer height={height} width='100%'>
				<AreaChart
					data={data}
					onClick={pointData =>
						alwaysShowSelectedTooltip &&
						onPointSelect({index: pointData})
					}
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
						domain={['dataMin - 43200000', 'dataMax + 43200000']}
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
								{formatUTCDateFromUnix(payload.value, 'MMM DD')}
							</Text>
						)}
						tickCount={8}
						tickLine={false}
						tickMargin={12}
						type='number'
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
						axisLine={{stroke: AXIS.borderStroke}}
						domain={[
							0,
							dataMax => dataMax + Math.ceil(dataMax / 10)
						]}
						label={{
							fill: AXIS.textColor,
							offset: 20,
							position: 'top',
							value: Liferay.Language.get('growth')
						}}
						name={Liferay.Language.get('growth')}
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
								y={y + payload.offset}
							>
								{payload.value}
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
							selectedPoint && mouseOutside
								? {
										x: selectedPoint.chartX,
										y: selectedPoint.chartY
								  }
								: null
						}
						wrapperStyle={
							selectedPoint && mouseOutside
								? {
										visibility: 'visible'
								  }
								: null
						}
					/>

					<ReferenceLine
						strokeWidth={1}
						x={selectedPoint && selectedPoint.activeLabel}
					/>

					<ReferenceDot
						fill={CHART_BLUE}
						fillOpacity={
							legendHoverItem === 'anonymousCount' ? 0.1 : 1
						}
						isFront
						r={4}
						stroke='none'
						x={selectedPoint && selectedPoint.activeLabel}
						y={
							selectedPoint &&
							selectedPoint.activePayload[0].payload.knownCount
						}
					/>

					<ReferenceDot
						fill={CHART_ORANGE}
						fillOpacity={legendHoverItem === 'knownCount' ? 0.1 : 1}
						isFront
						r={4}
						stroke='none'
						x={selectedPoint && selectedPoint.activeLabel}
						y={
							selectedPoint &&
							selectedPoint.activePayload[0].payload
								.anonymousCount +
								selectedPoint.activePayload[0].payload
									.knownCount
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
		onClearSelection: PropTypes.func.isRequired,
		selectedPoint: PropTypes.object
	};

	render() {
		const {data, onClearSelection, selectedPoint} = this.props;

		const index = get(selectedPoint, ['activeTooltipIndex']);

		const {added, modifiedDate, removed} = get(data, index, {});

		const changeValues =
			selectedPoint &&
			index > 0 &&
			getNetChange(data[index - 1], data[index]);

		return (
			<div className='selected-point-info'>
				{selectedPoint ? (
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
		id: PropTypes.string.isRequired,
		selectedPoint: PropTypes.object
	};

	@autobind
	fetchMembers(params) {
		const {selectedPoint} = this.props;

		const fetchMembersFn = selectedPoint ? getMemberChanges : getAllMembers;

		return fetchMembersFn(params);
	}

	getColumns() {
		const {channelId, groupId, selectedPoint} = this.props;

		if (selectedPoint) {
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
						onPointSelect={onPointSelect}
						selectedPoint={selectedPoint}
					/>
				</div>

				<SelectedPointInfo
					data={data}
					onClearSelection={this.handleClearSelection}
					selectedPoint={selectedPoint}
				/>

				<SearchableEntityTableHOC
					columns={this.getColumns()}
					dataSourceFn={this.fetchMembers}
					dataSourceParams={{channelId, groupId, id, modifiedDate}}
					defaultSort={{
						field: selectedPoint ? DATE_CHANGED : NAME,
						sortOrder: selectedPoint
							? orderDescending
							: orderAscending
					}}
					entityType={selectedPoint ? '' : INDIVIDUALS}
					rowIdentifier='id'
				/>
			</Card.Body>
		);
	}
}

export default withSelectedPoint(SegmentGrowthWithList);
