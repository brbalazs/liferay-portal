import * as API from 'shared/api';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import React from 'react';
import {mapGrowthHistory} from 'shared/hoc/mappers/segment';
import {Routes, toRoute} from 'shared/util/router';
import {SegmentGrowthChart} from './Growth';
import {withRequest} from 'shared/hoc';

interface IMembershipChartComponent extends React.Component<HTMLDivElement> {
	data: [];
	groupId: string;
	id: string;
	individualCounts: string;
	tabId: string;
}

interface ISegmentProfileCard {
	channelId: string;
	groupId: string;
	id: string;
	segment: {anonymousIndividualCount: number; knownIndividualCount: number};
	tabId: string;
}

const MembershipChartComponent: React.FC<IMembershipChartComponent> = ({
	data,
	groupId,
	id,
	individualCounts,
	tabId
}) => (
	<SegmentGrowthChart
		data={data}
		groupId={groupId}
		id={id}
		individualCounts={individualCounts}
		tabId={tabId}
	/>
);

export const MembershipChart = withRequest(
	API.individualSegment.fetchMembershipChangesAggregations,
	mapGrowthHistory,
	{
		page: false
	}
)(MembershipChartComponent);

const SegmentProfileCard: React.FC<ISegmentProfileCard> = ({
	channelId,
	groupId,
	id,
	segment: {anonymousIndividualCount, knownIndividualCount},
	tabId
}) => (
	<Card className='segment-profile-card-root'>
		<Card.Header>
			<Card.Title>
				{Liferay.Language.get('segment-membership')}
			</Card.Title>

			<div className='subtitle-segment'>
				{Liferay.Language.get(
					'segment-membership-processes-daily-and-does-not-include-todays-activities'
				)}
			</div>
		</Card.Header>

		<Card.Body>
			<MembershipChart
				channelId={channelId}
				groupId={groupId}
				id={id}
				individualCounts={{
					anonymousCount: anonymousIndividualCount,
					knownCount: knownIndividualCount
				}}
				tabId={tabId}
			/>
		</Card.Body>

		<Card.Footer>
			<Button
				display='link'
				href={toRoute(Routes.CONTACTS_SEGMENT_MEMBERSHIP, {
					channelId,
					groupId,
					id
				})}
				icon='angle-right'
				iconAlignment='right'
				size='sm'
			>
				{Liferay.Language.get('view-members')}
			</Button>
		</Card.Footer>
	</Card>
);

export default SegmentProfileCard;
