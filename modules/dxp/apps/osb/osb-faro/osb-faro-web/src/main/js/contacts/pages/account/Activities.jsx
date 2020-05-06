import * as API from 'shared/api';
import AccountEngagement from 'contacts/components/account/Engagement';
import ActivitiesChartTimeline from 'contacts/components/ActivitiesChartTimeline';
import autobind from 'autobind-decorator';
import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {Account} from 'shared/util/records';
import {ACTIVITIES, ENGAGEMENT, Routes, toRoute} from 'shared/util/router';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {DEFAULT_ACTIVITY_MAX} from 'shared/api/activities';
import {
	formatEngagementAggregation,
	formatEngagementScore
} from 'shared/util/engagement';
import {getSafeChange} from 'shared/util/change';
import {isNull} from 'lodash';
import {PropTypes} from 'prop-types';
import {sub} from 'shared/util/lang';

const {
	entityTypes: {account},
	timeIntervals
} = FaroConstants;

const DEFAULT_MAX = 30;

@hasRequest
export default class Activities extends React.Component {
	static defaultProps = {
		tabId: ACTIVITIES
	};

	static propTypes = {
		account: PropTypes.instanceOf(Account).isRequired,
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		tabId: PropTypes.string
	};

	state = {
		activityChange: 0,
		activityHistory: null,
		engagementChange: 0,
		engagementHistory: null,
		engagementPrevScore: 0,
		error: false,
		loading: true
	};

	componentDidMount() {
		this.handleFetchHistory();
	}

	buildCardTabs() {
		const {
			account: {activitiesCount, engagementScore, id},
			channelId,
			groupId
		} = this.props;

		const formattedEngagementScore = formatEngagementScore(engagementScore);

		return [
			{
				secondaryInfo: sub(
					Liferay.Language.get('x-last-x-days'),
					[
						<span className='primary-content' key='TOTAL'>
							{activitiesCount.toLocaleString()}
						</span>,
						DEFAULT_ACTIVITY_MAX
					],
					false
				),
				tabId: ACTIVITIES,
				tabUrl: toRoute(Routes.CONTACTS_ACCOUNT_ACTIVITIES, {
					channelId,
					groupId,
					id,
					tabId: ACTIVITIES
				}),
				title: Liferay.Language.get('account-activities')
			},
			{
				secondaryInfo: sub(
					Liferay.Language.get('x-avg-member-score'),
					[
						<span className='primary-content' key='SCORE'>
							{isNull(formattedEngagementScore) ? (
								'--'
							) : (
								<>
									{formattedEngagementScore.toFixed(2)}

									<span className='denominator'>{'/10'}</span>
								</>
							)}
						</span>
					],
					false
				),
				tabId: ENGAGEMENT,
				tabUrl: toRoute(Routes.CONTACTS_ACCOUNT_ACTIVITIES, {
					channelId,
					groupId,
					id,
					tabId: ENGAGEMENT
				}),
				title: Liferay.Language.get('account-engagement-score')
			}
		];
	}

	@autoCancel
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
			max: DEFAULT_MAX
		});
	}

	@autoCancel
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
			max: DEFAULT_MAX
		});
	}

	@autobind
	handleFetchHistory() {
		this.setState({error: false, loading: true});

		Promise.all([this.getActivityHistory(), this.getEngagementHistory()])
			.then(([activity, engagement]) => {
				const {activityAggregations, change: activityChange} = activity;

				const {
					change: engagementChange,
					engagementAggregations,
					previousScoreAvg
				} = engagement;

				const engagementHistory = engagementAggregations.map(
					formatEngagementAggregation
				);

				this.setState({
					activityChange: getSafeChange(activityChange),
					activityHistory: activityAggregations,
					engagementChange: getSafeChange(engagementChange),
					engagementHistory,
					engagementPrevScore: formatEngagementScore(
						previousScoreAvg
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

	renderContent() {
		const {
			props: {
				account: {id, type},
				channelId,
				groupId,
				tabId
			},
			state: {
				activityHistory,
				engagementHistory,
				engagementPrevScore,
				error,
				loading
			}
		} = this;

		if (loading) {
			return <Spinner className='flex-grow-1' key='LOADING' spacer />;
		} else if (error) {
			return (
				<ErrorDisplay
					key='ERROR_DISPLAY'
					onReload={this.handleFetchHistory}
					spacer
				/>
			);
		} else {
			return (
				<>
					<CardTabs activeTabId={tabId} tabs={this.buildCardTabs()} />

					{tabId === ACTIVITIES && activityHistory && (
						<ActivitiesChartTimeline
							activitiesLabel={Liferay.Language.get(
								'accounts-activities-x'
							)}
							channelId={channelId}
							entityType={type}
							groupId={groupId}
							history={activityHistory}
							id={id}
						/>
					)}

					{tabId === ENGAGEMENT && engagementHistory && (
						<AccountEngagement
							data={engagementHistory}
							groupId={groupId}
							id={id}
							previousScore={engagementPrevScore}
						/>
					)}
				</>
			);
		}
	}

	render() {
		return <Card pageDisplay>{this.renderContent()}</Card>;
	}
}
