import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Chart, {
	AREA,
	CHART_COLOR_NAMES,
	COMBINED_CHART
} from 'shared/components/Chart';
import ChartTooltip from 'shared/components/ChartTooltip';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import React from 'react';
import ReactDOMServer from 'react-dom/server';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {
	changesListColumns,
	individualsListColumns
} from 'shared/util/table-columns';
import {DATE_CHANGED, NAME} from 'shared/util/pagination';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {get, isNil, maxBy, omit} from 'lodash';
import {getLegendCircle} from 'shared/util/charts';
import {getNetChange} from 'shared/util/change';
import {INDIVIDUALS} from 'shared/util/router';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';
import {withSelectedPoint, withStatefulPagination} from 'shared/hoc';

const {mormont: CHART_ORANGE, stark: CHART_BLUE} = CHART_COLOR_NAMES;

const SearchableEntityTableHOC = withStatefulPagination(
	SearchableEntityTable,
	null,
	props => omit(props, 'onSearchValueChange')
);

const {orderAscending, orderDescending} = FaroConstants.pagination;
const CHART_ID = 'segmentGrowth';
const CHART_DATA_ID_1 = 'known-membership';
const CHART_DATA_ID_2 = 'anonymous-membership';

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
	static propTypes = {
		data: CHANGES_AGGREGATION_SHAPE,
		onPointSelect: PropTypes.func
	};

	constructor(props) {
		super(props);

		this._chartRef = React.createRef();
	}

	@autobind
	getHTMLTooltipString(tooltipData) {
		const {index} = tooltipData[0];

		const {data} = this.props;

		const {
			added,
			anonymousCount,
			knownCount,
			modifiedDate,
			removed,
			value
		} = data[index];

		const netChange = getNetChange(get(data[index - 1], 'value'), value);

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

		return ReactDOMServer.renderToString(
			<ChartTooltip
				items={
					isNil(netChange)
						? change
						: [
								...change,
								{
									label: Liferay.Language.get('net-change'),
									value: `${netChange[0]}(${netChange[1]}%)`
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
						[<b key='VALUE'>{anonymousCount.toLocaleString()}</b>],
						false
					),
					sub(
						Liferay.Language.get('x-known-members'),
						[<b key='VALUE'>{knownCount.toLocaleString()}</b>],
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
		);
	}

	render() {
		const {data, onPointSelect} = this.props;

		const maxY = get(maxBy(data, 'value'), 'value');

		return (
			<>
				<Chart
					alwaysShowSelectedTooltip
					axisX={{
						categories: data.map(item => String(item.modifiedDate)),
						tick: {
							centered: false,
							format: dateObj =>
								formatUTCDateFromUnix(dateObj, 'MMM DD'),
							multiline: true,
							outer: false
						},
						type: 'timeseries'
					}}
					axisY={{
						max: maxY < 10 ? 10 : maxY,
						min: 0,
						padding: {bottom: 0}
					}}
					chartType={COMBINED_CHART}
					className='segment-growth-chart-root'
					data={[
						{
							data: data.map(item => item.modifiedDate),
							id: 'modifiedDate'
						},
						{
							color: CHART_BLUE,
							data: data.map(({knownCount}) => knownCount),
							id: CHART_DATA_ID_1,
							name: Liferay.Language.get('known-members'),
							type: AREA
						},
						{
							color: CHART_ORANGE,
							data: data.map(
								({anonymousCount}) => anonymousCount
							),
							id: CHART_DATA_ID_2,
							name: Liferay.Language.get('anonymous-members'),
							type: AREA
						}
					]}
					dataId={CHART_DATA_ID_1}
					id={CHART_ID}
					legend={{
						contents: {
							bindto: '#legend-growth',
							template: (id, color) =>
								`<li class="chart-legend-item">${getLegendCircle(
									color
								)} ${
									id === CHART_DATA_ID_1
										? Liferay.Language.get('known-members')
										: Liferay.Language.get(
												'anonymous-members'
										  )
								}</li>`
						},
						item: {
							onclick: () => false
						},
						show: true
					}}
					onPointSelect={onPointSelect}
					otherData={{
						groups: [[CHART_DATA_ID_1, CHART_DATA_ID_2]],
						order: null
					}}
					ref={this._chartRef}
					tooltip={{
						contents: this.getHTMLTooltipString
					}}
					x='modifiedDate'
					yLabel={Liferay.Language.get('growth')}
				/>

				<div className='chart-legend' id='legend-growth'></div>
			</>
		);
	}
}

export class SelectedPointInfo extends React.Component {
	static propTypes = {
		data: CHANGES_AGGREGATION_SHAPE,
		hasSelectedPoint: PropTypes.bool,
		onClearSelection: PropTypes.func.isRequired
	};

	@autobind
	handleClearSelection() {
		this.props.onClearSelection();
	}

	render() {
		const {data, hasSelectedPoint, selectedPoint} = this.props;

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
								onClick={this.handleClearSelection}
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

	constructor(props) {
		super(props);

		this._chartRef = React.createRef();
	}

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

		this._chartRef.current._chartRef.current.unselect();

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

		const {modifiedDate} = get(data, selectedPoint, {});

		return (
			<Card.Body
				className={getCN('segment-growth-root', className)}
				noPadding
			>
				<SegmentGrowthChart
					data={data}
					onPointSelect={onPointSelect}
					ref={this._chartRef}
				/>

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
