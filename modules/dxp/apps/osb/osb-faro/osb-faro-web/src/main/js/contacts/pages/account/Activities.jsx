import * as API from 'shared/api';
import AccountEngagement from 'contacts/components/account/Engagement';
import ActivitiesChartTimeline from 'contacts/components/ActivitiesChartTimeline';
import autobind from 'autobind-decorator';
import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import {Account} from 'shared/util/records';
import {ACTIVITIES, ENGAGEMENT, Routes} from 'shared/util/router';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	buildTabItems,
	getSafeRangeKey,
	INTERVAL_MAP
} from 'shared/util/engagement-activity';
import {
	formatEngagementAggregation,
	formatEngagementScore
} from 'shared/util/engagement';
import {getSafeChange} from 'shared/util/change';
import {PropTypes} from 'prop-types';
import {WrapSafeResults} from 'shared/hoc/util';

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

	componentDidUpdate(prevProps) {
		const {interval, rangeSelectors} = this.props;

		if (
			prevProps.rangeSelectors !== rangeSelectors ||
			prevProps.interval !== interval
		) {
			this.handleFetchHistory();
		}
	}

	@autoCancel
	getActivityHistory() {
		const {
			account: {id},
			channelId,
			groupId,
			interval,
			rangeSelectors
		} = this.props;

		return API.activities.fetchHistory({
			channelId,
			contactsEntityId: id,
			contactsEntityType: account,
			groupId,
			interval: INTERVAL_MAP[interval],
			max: getSafeRangeKey(rangeSelectors.rangeKey),
			...rangeSelectors
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
	render() {
		const {
			props: {
				account: {activitiesCount, engagementScore, id, type},
				channelId,
				groupId,
				interval,
				rangeSelectors,
				tabId
			},
			state: {
				activityChange,
				activityHistory,
				engagementChange,
				engagementHistory,
				engagementPrevScore,
				error,
				loading
			}
		} = this;

		return (
			<Card.Body noPadding>
				<WrapSafeResults
					className={'flex-grow-1'}
					error={error}
					errorProps={{
						className: 'flex-grow-1',
						onReload: this.handleFetchHistory
					}}
					loading={loading}
					page={false}
					pageDisplay={false}
				>
					<CardTabs
						activeTabId={tabId}
						tabs={buildTabItems({
							activityChange,
							activityCount: activitiesCount,
							channelId,
							engagementChange,
							engagementLabel: Liferay.Language.get(
								'avg-member-score'
							),
							engagementScore: formatEngagementScore(
								engagementScore
							),
							groupId,
							id,
							route: Routes.CONTACTS_ACCOUNT_ACTIVITIES
						})}
					/>

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
							interval={interval}
							rangeSelectors={rangeSelectors}
						/>
					)}

					{tabId === ENGAGEMENT && engagementHistory && (
						<AccountEngagement
							data={engagementHistory}
							groupId={groupId}
							id={id}
							interval={interval}
							previousScore={engagementPrevScore}
							rangeSelectors={rangeSelectors}
						/>
					)}
				</WrapSafeResults>
			</Card.Body>
		);
	}
}
