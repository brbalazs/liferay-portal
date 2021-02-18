import * as API from 'shared/api';
import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Promise from 'metal-promise';
import React from 'react';
import {mapHistories} from 'shared/hoc/mappers/segment';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
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

export const MembershipChart = withRequest(fetchHistories, mapHistories, {
	page: false
})(
	class extends React.Component {
		static propTypes = {
			groupId: PropTypes.string.isRequired,
			growthHistory: PropTypes.shape({
				data: PropTypes.array
			}).isRequired,
			id: PropTypes.string.isRequired,
			individualCounts: PropTypes.shape({
				anonymousCount: PropTypes.number,
				knownCount: PropTypes.number
			})
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
	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		individualCounts: PropTypes.shape({
			anonymousCount: PropTypes.number,
			knownCount: PropTypes.number
		}),
		segment: PropTypes.instanceOf(Segment).isRequired
	};

	render() {
		const {
			channelId,
			groupId,
			segment: {anonymousIndividualCount, id, knownIndividualCount}
		} = this.props;

		return (
			<Card className='segment-profile-card-root'>
				<Card.Header>
					<Card.Title>
						{Liferay.Language.get('segment-membership')}
					</Card.Title>
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
	}
}

export default SegmentProfileCard;
