import * as API from 'shared/api';
import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import Promise from 'metal-promise';
import React from 'react';
import SegmentGrowthWithList from 'contacts/components/segment/Growth';
import {connect} from 'react-redux';
import {getPluralMessage} from 'shared/util/lang';
import {GROWTH, Routes, toRoute} from 'shared/util/router';
import {mapHistories} from 'shared/hoc/mappers/segment';
import {PropTypes} from 'prop-types';
import {Segment} from 'shared/util/records';
import {withRequest} from 'shared/hoc';

function fetchHistories({channelId, groupId, id}) {
	return Promise.all([
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
				groupId,
				growthHistory,
				id,
				timeZoneId
			} = this.props;

			return (
				<SegmentGrowthWithList
					{...growthHistory}
					channelId={channelId}
					groupId={groupId}
					id={id}
					timeZoneId={timeZoneId}
				/>
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
			segment: {id, individualCount}
		} = this.props;

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
