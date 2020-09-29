import * as API from 'shared/api';
import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import FaroConstants from 'shared/util/constants';
import Promise from 'metal-promise';
import React from 'react';
import SegmentEngagementWithList from 'contacts/components/segment/Engagement';
import SegmentGrowthWithList from 'contacts/components/segment/Growth';
import {connect} from 'react-redux';
import {ENGAGEMENT, GROWTH, Routes, toRoute} from 'shared/util/router';
import {formatEngagementScore} from 'shared/util/engagement';
import {getPluralMessage, sub} from 'shared/util/lang';
import {isNull} from 'lodash';
import {mapHistories} from 'shared/hoc/mappers/segment';
import {PropTypes} from 'prop-types';
import {Segment} from 'shared/util/records';
import {withRequest} from 'shared/hoc';

const {
	entityTypes: {individualsSegment}
} = FaroConstants;

function fetchHistories({channelId, groupId, id}) {
	return Promise.all([
		API.engagement.fetchHistory({
			contactsEntityId: id,
			contactsEntityType: individualsSegment,
			groupId
		}),
		API.individualSegment.fetchMembershipChangesAggregations({
			channelId,
			groupId,
			id
		})
	]);
}

export const ChartViews = withRequest(fetchHistories, mapHistories, {
	alignCenter: true,
	page: false
})(
	class extends React.Component {
		static propTypes = {
			channelId: PropTypes.string,
			engagementHistory: PropTypes.shape({
				data: PropTypes.array,
				previousScore: PropTypes.number
			}).isRequired,
			groupId: PropTypes.string.isRequired,
			growthHistory: PropTypes.shape({
				data: PropTypes.array
			}).isRequired,
			id: PropTypes.string.isRequired,
			tabId: PropTypes.string,
			timeZoneId: PropTypes.string
		};

		render() {
			const {
				channelId,
				engagementHistory,
				groupId,
				growthHistory,
				id,
				tabId,
				timeZoneId
			} = this.props;

			return (
				<>
					{tabId === ENGAGEMENT ? (
						<SegmentEngagementWithList
							{...engagementHistory}
							channelId={channelId}
							groupId={groupId}
							id={id}
						/>
					) : (
						<SegmentGrowthWithList
							{...growthHistory}
							channelId={channelId}
							groupId={groupId}
							id={id}
							timeZoneId={timeZoneId}
						/>
					)}
				</>
			);
		}
	}
);

@connect((store, {groupId}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))
export default class Membership extends React.Component {
	static defaultProps = {
		tabId: GROWTH
	};

	static propTypes = {
		groupId: PropTypes.string.isRequired,
		segment: PropTypes.instanceOf(Segment).isRequired,
		tabId: PropTypes.string,
		timeZoneId: PropTypes.string
	};

	buildCardTabs() {
		const {
			channelId,
			groupId,
			segment: {engagementScore, id, individualCount}
		} = this.props;

		const formattedEngagementScore = formatEngagementScore(engagementScore);

		return [
			{
				secondaryInfo: getPluralMessage(
					Liferay.Language.get('x-individual-in-segment'),
					Liferay.Language.get('x-individuals-in-segment'),
					individualCount,
					false,
					[
						<span className='primary-content' key='TOTAL'>
							{individualCount.toLocaleString()}
						</span>
					]
				),
				tabId: GROWTH,
				tabUrl: toRoute(Routes.CONTACTS_SEGMENT_MEMBERSHIP, {
					channelId,
					groupId,
					id,
					tabId: GROWTH
				}),
				title: Liferay.Language.get('membership')
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
				tabUrl: toRoute(Routes.CONTACTS_SEGMENT_MEMBERSHIP, {
					channelId,
					groupId,
					id,
					tabId: ENGAGEMENT
				}),
				title: Liferay.Language.get('segment-engagement-score')
			}
		];
	}

	render() {
		const {
			channelId,
			groupId,
			segment: {id},
			tabId,
			timeZoneId
		} = this.props;

		return (
			<Card className='segment-membership-root' pageDisplay>
				<CardTabs activeTabId={tabId} tabs={this.buildCardTabs()} />

				<ChartViews
					channelId={channelId}
					groupId={groupId}
					id={id}
					tabId={tabId}
					timeZoneId={timeZoneId}
				/>
			</Card>
		);
	}
}
