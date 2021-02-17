import * as API from 'shared/api';
import Card from 'shared/components/Card';
import CardTabs from 'shared/components/CardTabs';
import Promise from 'metal-promise';
import React from 'react';
import {getPluralMessage} from 'shared/util/lang';
import {GROWTH, Routes, toRoute} from 'shared/util/router';
import {mapHistories} from 'shared/hoc/mappers/segment';
import {PropTypes} from 'prop-types';
import {Segment} from 'shared/util/records';
import {SegmentGrowthChart} from './Growth';
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
	page: false
})(
	class extends React.Component {
		static propTypes = {
			groupId: PropTypes.string.isRequired,
			growthHistory: PropTypes.shape({
				data: PropTypes.array
			}).isRequired,
			id: PropTypes.string.isRequired,
			tabId: PropTypes.string
		};

		render() {
			const {groupId, growthHistory, id} = this.props;

			return (
				<SegmentGrowthChart
					{...growthHistory}
					groupId={groupId}
					id={id}
				/>
			);
		}
	}
);

export class SegmentProfileCard extends React.Component {
	static defaultProps = {
		tabId: GROWTH
	};

	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		segment: PropTypes.instanceOf(Segment).isRequired,
		tabId: PropTypes.string
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
						<b className='primary-content' key='TOTAL'>
							{individualCount.toLocaleString()}
						</b>
					]
				),
				tabId: GROWTH,
				tabUrl: toRoute(Routes.CONTACTS_SEGMENT_OVERVIEW, {
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
			tabId
		} = this.props;

		return (
			<Card className='segment-profile-card-root'>
				<CardTabs activeTabId={tabId} tabs={this.buildCardTabs()} />

				<Card.Body>
					<ChartViews
						channelId={channelId}
						groupId={groupId}
						id={id}
						tabId={tabId}
					/>
				</Card.Body>
			</Card>
		);
	}
}

export default SegmentProfileCard;
