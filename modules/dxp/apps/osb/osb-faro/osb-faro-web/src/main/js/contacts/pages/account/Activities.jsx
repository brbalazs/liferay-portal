import * as API from 'shared/api';
import ActivitiesChartTimeline from 'contacts/components/ActivitiesChartTimeline';
import autobind from 'autobind-decorator';
import Card from 'shared/components/Card';
import CardTabs, {ButtonDisplayMode} from 'shared/components/CardTabs';
import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import {Account} from 'shared/util/records';
import {ACTIVITIES, Routes} from 'shared/util/router';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {
	buildTabItems,
	getSafeRangeKey,
	INTERVAL_MAP
} from 'shared/util/engagement-activity';
import {connect} from 'react-redux';
import {getSafeChange} from 'shared/util/change';
import {PropTypes} from 'prop-types';
import {WrapSafeResults} from 'shared/hoc/util';

const {
	entityTypes: {account}
} = FaroConstants;

@hasRequest
export class Activities extends React.Component {
	static defaultProps = {
		tabId: ACTIVITIES
	};

	static propTypes = {
		account: PropTypes.instanceOf(Account).isRequired,
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		tabId: PropTypes.string,
		timeZoneId: PropTypes.string.isRequired
	};

	state = {
		activityChange: 0,
		activityHistory: null,
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

	@autobind
	handleFetchHistory() {
		this.setState({error: false, loading: true});

		Promise.all([this.getActivityHistory()])
			.then(([activity]) => {
				const {activityAggregations, change: activityChange} = activity;

				this.setState({
					activityChange: getSafeChange(activityChange),
					activityHistory: activityAggregations,
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
				account: {activitiesCount, id, type},
				channelId,
				groupId,
				interval,
				rangeSelectors,
				tabId,
				timeZoneId
			},
			state: {activityChange, activityHistory, error, loading}
		} = this;

		return (
			<Card.Body noPadding>
				<WrapSafeResults
					className='flex-grow-1'
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
						buttonsDisplayMode={ButtonDisplayMode.SPACED_BUTTONS}
						tabs={buildTabItems({
							activityChange,
							activityCount: activitiesCount,
							channelId,
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
							timeZoneId={timeZoneId}
						/>
					)}
				</WrapSafeResults>
			</Card.Body>
		);
	}
}

export default connect((store, {groupId}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))(Activities);
