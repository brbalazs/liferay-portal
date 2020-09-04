import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Card from 'shared/components/Card';
import EngagementChart from './EngagementChart';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import React from 'react';
import SearchableEntityTable from 'shared/components/SearchableEntityTable';
import {findLastIndex, get, isFinite, isNil, isNull, noop} from 'lodash/fp';
import {formatChange, getFinitePercentChange} from 'shared/util/change';
import {formatEngagementScore} from 'shared/util/engagement';
import {formatUTCDateFromUnix, getLastDate} from 'shared/util/date';
import {omit} from 'lodash';
import {PropTypes} from 'prop-types';
import {SCORE} from 'shared/util/pagination';
import {sub} from 'shared/util/lang';
import {toThousands} from 'shared/util/numbers';
import {withSelectedPoint, withStatefulPagination} from 'shared/hoc';

const {
	pagination: {orderDescending}
} = FaroConstants;

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

	constructor(props) {
		super(props);

		this._chartRef = React.createRef();
	}

	getDateRange() {
		const {data, hasSelectedPoint, selectedPoint} = this.props;

		if (!hasSelectedPoint) {
			return {
				endDate: getLastDate(data, null, 'intervalInitDate'),
				startDate: getLastDate(data, null, 'intervalInitDate')
			};
		}

		const intervalInitDate =
			get('intervalInitDate', data[selectedPoint]) || null;

		return {endDate: intervalInitDate, startDate: intervalInitDate};
	}

	@autobind
	handleInitialPoint() {
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

		const tooltipRenderRows = ({contributors}) => [
			{
				columns: [
					{
						label: Liferay.Language.get('active-members'),
						weight: 'normal'
					},
					{
						align: 'right',
						label: toThousands(contributors),
						weight: 'semibold'
					}
				]
			}
		];

		return (
			<Card.Body
				className={getCN('engagement-chart-list-root', className)}
				noPadding
			>
				<EngagementChart
					forwardedRef={this._chartRef}
					history={data}
					onAfterInit={this.handleInitialPoint}
					onPointSelect={onPointSelect}
					tooltipRenderRows={tooltipRenderRows}
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
