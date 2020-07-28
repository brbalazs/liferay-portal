import * as API from 'shared/api';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ChangeLegend from 'contacts/components/ChangeLegend';
import Chart, {COMBINED_CHART} from 'shared/components/Chart';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FaroConstants from 'shared/util/constants';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import Promise from 'metal-promise';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {Account} from 'shared/util/records';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	buildEngagementActivityAxes,
	buildLegendItems,
	CHART_ACTIVITY_ID,
	CHART_ID
} from 'shared/util/engagement-activity';
import {
	convertHistoryInitDateToDate,
	createDateKeysIMap,
	renderTooltip
} from 'shared/util/engagement-activity';
import {DEFAULT_ACTIVITY_MAX} from 'shared/api/activities';
import {DEFAULT_ENGAGEMENT_MAX} from 'shared/api/engagement';
import {
	formatEngagementAggregation,
	formatEngagementScore,
	mergeHistoryByDate
} from 'shared/util/engagement';
import {
	formatXAxisDate,
	getIntervals,
	MetricValueType
} from 'shared/util/charts';
import {getMaxActivitiesValue} from 'shared/util/activities';
import {getSafeChange} from 'shared/util/change';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {
	entityTypes: {account},
	timeIntervals
} = FaroConstants;

@hasRequest
export default class ActivitiesCard extends React.Component {
	static propTypes = {
		account: PropTypes.instanceOf(Account).isRequired,
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired
	};

	state = {
		activityChange: 0,
		engagementChange: 0,
		error: false,
		history: [],
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

	handleChartParams() {
		const {history} = this.state;

		const parsedHistory = convertHistoryInitDateToDate(history);
		const dateKeysIMap = createDateKeysIMap('D', parsedHistory);

		const intervals = getIntervals(
			'30',
			parsedHistory.map(({intervalInitDate}) => intervalInitDate),
			'D',
			dateKeysIMap
		);

		const addTooltipInfo = ({scoreAvg}) => [
			{
				columns: [
					{
						label: Liferay.Language.get('avg-engagement'),
						weight: 'normal'
					},
					{
						align: 'right',
						label: scoreAvg.toFixed(2),
						weight: 'semibold'
					}
				]
			}
		];

		const getTooltipContents = renderTooltip({
			dateKeysIMap,
			history: parsedHistory,
			interval: 'D',
			name: Liferay.Language.get('activities'),
			rangeSelectors: {
				rangeKey: '30'
			},
			title: Liferay.Language.get('activities'),
			tooltipRenderRows: addTooltipInfo,
			type: MetricValueType.Number
		});

		return {dateKeysIMap, getTooltipContents, intervals};
	}

	renderChart() {
		const {
			props: {
				account: {activitiesCount, engagementScore}
			},
			state: {activityChange, engagementChange, error, history, loading}
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
			const {
				dateKeysIMap,
				getTooltipContents,
				intervals
			} = this.handleChartParams();

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

					<Chart
						axisX={{
							tick: {
								centered: false,
								format: date =>
									formatXAxisDate(
										date,
										'30',
										'D',
										dateKeysIMap
									),
								values: intervals
							},
							type: 'timeseries'
						}}
						axisY={{
							max: getMaxActivitiesValue(history),
							min: 0,
							padding: {bottom: 0}
						}}
						axisY2={{
							min: 0,
							padding: {bottom: 0},
							show: true
						}}
						bar={{width: {ratio: 0.9}}}
						chartType={COMBINED_CHART}
						data={buildEngagementActivityAxes(history)}
						dataId={CHART_ACTIVITY_ID}
						id={CHART_ID}
						splineInterpolationType='monotone-x'
						tooltip={{
							contents: getTooltipContents
						}}
						x='date'
						y2Label={Liferay.Language.get('engagement')}
						yLabel={Liferay.Language.get('activities')}
					/>
				</>
			);
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
