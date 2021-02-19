import * as API from 'shared/api';
import ActivitiesChart from '../ActivitiesChart';
import autobind from 'autobind-decorator';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ChangeLegend from 'contacts/components/ChangeLegend';
import Constants from 'shared/util/constants';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import getCN from 'classnames';
import React from 'react';
import Spinner from 'shared/components/Spinner';
import {Account} from 'shared/util/records';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {buildLegendItems} from 'shared/util/activities';
import {DEFAULT_ACTIVITY_MAX} from 'shared/api/activities';
import {getSafeChange} from 'shared/util/change';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';

const {
	entityTypes: {account},
	timeIntervals
} = Constants;

@hasRequest
export default class ActivitiesCard extends React.Component {
	static propTypes = {
		account: PropTypes.instanceOf(Account).isRequired,
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired
	};

	state = {
		activityChange: 0,
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

	@autobind
	handleFetchHistory() {
		const {
			account: {id},
			channelId,
			groupId
		} = this.props;

		this.setState({error: false, loading: true});

		API.activities
			.fetchHistory({
				channelId,
				contactsEntityId: id,
				contactsEntityType: account,
				groupId,
				interval: timeIntervals.day,
				max: DEFAULT_ACTIVITY_MAX
			})
			.then(
				({
					activityAggregations: activityHistory,
					change: activityChange
				}) => {
					this.setState({
						activityChange: getSafeChange(activityChange),
						history: activityHistory,
						loading: false
					});
				}
			)
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					this.setState({error: true, loading: false});
				}
			});
	}

	renderChart() {
		const {
			props: {
				account: {activitiesCount}
			},
			state: {activityChange, error, history, loading}
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
			return (
				<>
					<ChangeLegend
						items={buildLegendItems({
							activityChange,
							activityCount: activitiesCount
						})}
					/>

					<ActivitiesChart
						history={history}
						interval={timeIntervals.day}
						rangeSelectors={{rangeKey: DEFAULT_ACTIVITY_MAX}}
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
						icon='angle-right'
						iconAlignment='right'
						size='sm'
					>
						{Liferay.Language.get('view-all-activities')}
					</Button>
				</Card.Footer>
			</Card>
		);
	}
}
