import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ChangeLegend from 'contacts/components/ChangeLegend';
import Constants, {LAST_30_DAYS} from 'shared/util/constants';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import Promise from 'metal-promise';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {Account} from 'shared/util/records';
import {
	ANIMATION_DURATION,
	AXIS,
	getAxisTickText,
	getChartTooltip,
	getYAxisLabel,
	getYAxisWidth
} from 'shared/util/recharts';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	Bar,
	CartesianGrid,
	Cell,
	ComposedChart,
	Line,
	ResponsiveContainer,
	Tooltip,
	XAxis,
	YAxis
} from 'recharts';
import {buildLegendItems} from 'shared/util/engagement-activity';
import {CHART_COLOR_NAMES} from 'shared/components/Chart';
import {createDateKeysIMap} from 'shared/util/intervals';
import {DEFAULT_ACTIVITY_MAX} from 'shared/api/activities';
import {DEFAULT_ENGAGEMENT_MAX} from 'shared/api/engagement';
import {
	formatEngagementAggregation,
	formatEngagementScore,
	mergeHistoryByDate
} from 'shared/util/engagement';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {formatXAxisDate, getBarColor, getIntervals} from 'shared/util/charts';
import {get, isNumber} from 'lodash';
import {getSafeChange} from 'shared/util/change';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {
	entityTypes: {account},
	timeIntervals
} = Constants;

const {mormont: CHART_ORANGE, stark: CHART_BLUE} = CHART_COLOR_NAMES;

const INTERVAL = 'D';

@hasRequest
export default class ActivitiesCard extends React.Component {
	static defaultProps = {
		height: 360
	};

	static propTypes = {
		account: PropTypes.instanceOf(Account).isRequired,
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		height: PropTypes.number
	};

	state = {
		activityChange: 0,
		engagementChange: 0,
		error: false,
		history: [],
		hoverIndex: -1,
		loading: true
	};

	componentDidMount() {
		this.handleFetchHistory();
	}

	@autoCancel
	@autobind
	getActivityHistory() {
		const {
			account: {id},
			channelId,
			groupId
		} = this.props;

		return API.activities.fetchHistory({
			channelId,
			contactsEntityId: id,
			contactsEntityType: account,
			groupId,
			interval: timeIntervals.day,
			max: DEFAULT_ACTIVITY_MAX
		});
	}

	getChartParams() {
		const {history} = this.state;

		const dateKeysIMap = createDateKeysIMap(
			INTERVAL,
			history,
			'intervalInitDate'
		);

		const intervals = getIntervals(
			LAST_30_DAYS,
			history.map(({intervalInitDate}) => intervalInitDate),
			INTERVAL,
			dateKeysIMap
		);

		return {dateKeysIMap, intervals};
	}

	@autoCancel
	@autobind
	getEngagementHistory() {
		const {
			account: {id},
			groupId
		} = this.props;

		return API.engagement.fetchHistory({
			contactsEntityId: id,
			contactsEntityType: account,
			groupId,
			interval: timeIntervals.day,
			max: DEFAULT_ENGAGEMENT_MAX
		});
	}

	@autobind
	handleFetchHistory() {
		this.setState({error: false, loading: true});

		Promise.all([this.getActivityHistory(), this.getEngagementHistory()])
			.then(([activity, engagement]) => {
				const {
					activityAggregations: activityHistory,
					change: activityChange
				} = activity;

				const {
					change: engagementChange,
					engagementAggregations
				} = engagement;

				const engagementHistory = engagementAggregations.map(
					formatEngagementAggregation
				);

				this.setState({
					activityChange: getSafeChange(activityChange),
					engagementChange: getSafeChange(engagementChange),
					history: mergeHistoryByDate(
						engagementHistory,
						activityHistory
					),
					loading: false
				});
			})
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					this.setState({error: true, loading: false});
				}
			});
	}

	renderChart() {
		const {
			props: {
				account: {activitiesCount, engagementScore},
				height
			},
			state: {
				activityChange,
				engagementChange,
				error,
				history,
				hoverIndex,
				loading
			}
		} = this;

		if (loading) {
			return <Spinner key='LOADING' spacer />;
		} else if (error) {
			return (
				<ErrorDisplay
					key='ERROR_DISPLAY'
					onReload={this.handleFetchHistory}
					spacer
				/>
			);
		} else {
			const {dateKeysIMap, intervals} = this.getChartParams();

			return (
				<>
					<ChangeLegend
						items={buildLegendItems({
							activityChange,
							activityCount: activitiesCount,
							engagementChange,
							engagementScore: formatEngagementScore(
								engagementScore
							)
						})}
					/>

					<ResponsiveContainer height={height} width='100%'>
						<ComposedChart data={history}>
							<CartesianGrid
								stroke={AXIS.gridStroke}
								strokeDasharray='3 3'
								vertical={false}
							/>

							<XAxis
								axisLine={{stroke: AXIS.borderStroke}}
								dataKey='intervalInitDate'
								domain={['dataMin', 'dataMax']}
								padding={{left: 20, right: 20}}
								tick={getAxisTickText('x', value =>
									formatXAxisDate(
										value,
										LAST_30_DAYS,
										INTERVAL,
										dateKeysIMap
									)
								)}
								tickLine={false}
								tickMargin={12}
								ticks={intervals}
							/>

							<XAxis
								axisLine={{stroke: AXIS.borderStroke}}
								dataKey='intervalInitDate'
								orientation='top'
								stroke={AXIS.gridStroke}
								tick={false}
								tickLine={false}
								xAxisId='top'
							/>

							<YAxis
								allowDecimals={false}
								axisLine={{stroke: AXIS.borderStroke}}
								dataKey='totalElements'
								label={getYAxisLabel(
									Liferay.Language.get('activities')
								)}
								name={Liferay.Language.get('activities')}
								stroke={AXIS.gridStroke}
								tick={getAxisTickText('y')}
								tickCount={6}
								tickLine={false}
								type='number'
								width={getYAxisWidth(
									history,
									'totalElements',
									40
								)}
								yAxisId='activities'
							/>

							<YAxis
								allowDecimals={false}
								axisLine={{stroke: AXIS.borderStroke}}
								dataKey='scoreAvg'
								label={getYAxisLabel(
									Liferay.Language.get('engagement'),
									'right'
								)}
								name={Liferay.Language.get('engagement')}
								orientation='right'
								stroke={AXIS.gridStroke}
								tick={getAxisTickText('y')}
								tickLine={false}
								type='number'
								width={40}
								yAxisId='engagement'
							/>

							<Tooltip
								content={this.renderTooltip}
								cursor={{stroke: CHART_BLUE}}
							/>

							<Bar
								animationDuration={ANIMATION_DURATION.bar}
								dataKey='totalElements'
								fill={CHART_BLUE}
								legendType='circle'
								onMouseEnter={(e, index) =>
									this.setState({hoverIndex: index})
								}
								onMouseLeave={() =>
									this.setState({hoverIndex: -1})
								}
								yAxisId='activities'
							>
								{history.map((entry, index) => (
									<Cell
										fill={getBarColor(
											index,
											hoverIndex,
											null,
											'blue'
										)}
										key={`cell-${index}`}
									/>
								))}
							</Bar>

							<Line
								activeDot={{r: 4, stroke: CHART_ORANGE}}
								dataKey='scoreAvg'
								dot={false}
								legendType='circle'
								stroke={CHART_ORANGE}
								strokeWidth={2}
								type='monotoneX'
								yAxisId='engagement'
							/>
						</ComposedChart>
					</ResponsiveContainer>
				</>
			);
		}
	}

	@autobind
	renderTooltip({active, payload}) {
		if (active) {
			const {intervalInitDate, scoreAvg, totalElements} = get(
				payload,
				[0, 'payload'],
				{}
			);

			return getChartTooltip({
				dateTitle: formatUTCDateFromUnix(
					intervalInitDate,
					'YYYY MMM D'
				),
				rows: [
					{
						label: Liferay.Language.get('activities'),
						value: totalElements.toLocaleString()
					},
					{
						label: Liferay.Language.get('avg-engagement'),
						value: isNumber(scoreAvg) ? scoreAvg.toFixed(2) : null
					}
				],
				title: Liferay.Language.get('activities')
			});
		}
	}

	render() {
		const {
			account: {id},
			channelId,
			className,
			groupId
		} = this.props;

		return (
			<Card className={getCN('account-activities-card-root', className)}>
				<Card.Header>
					<Card.Title>
						{Liferay.Language.get('account-activities')}
					</Card.Title>
				</Card.Header>

				<Card.Body>{this.renderChart()}</Card.Body>

				<Card.Footer>
					<Button
						display='link'
						href={toRoute(Routes.CONTACTS_ACCOUNT_ACTIVITIES, {
							channelId,
							groupId,
							id
						})}
						size='sm'
					>
						{Liferay.Language.get('view-all-activities')}

						<Icon symbol='angle-right' />
					</Button>
				</Card.Footer>
			</Card>
		);
	}
}
