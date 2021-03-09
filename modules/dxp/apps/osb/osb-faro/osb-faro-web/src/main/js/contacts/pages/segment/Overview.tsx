import CompositionCard from 'contacts/components/segment/CompositionCard';
import CriteriaCard from 'contacts/components/segment/criteria-card';
import DistributionCard from 'contacts/hoc/segment/DistributionCard';
import FaroConstants from 'shared/util/constants';
import InterestsCard from 'contacts/hoc/segment/InterestsCard';
import React, {useCallback, useEffect, useRef} from 'react';
import SegmentProfileCard from 'contacts/components/segment/ProfileCard';
import {connect} from 'react-redux';
import {debounce} from 'lodash';
import {GROWTH} from 'shared/util/router';
import {Segment} from 'shared/util/records';

const {segmentTypes} = FaroConstants;

const HEADER_MARGIN = 16;

interface IOverviewProps {
	channelId: string;
	groupId: string;
	id: string;
	segment: Segment;
	tabId?: string;
	timeZoneId: string;
}

const Overview: React.FC<IOverviewProps> = ({
	channelId,
	groupId,
	id,
	segment,
	tabId = GROWTH,
	timeZoneId
}) => {
	const _sideColumnRef = useRef<any>();

	const updateHeaderVisible = useCallback(
		debounce(() => {
			const node = _sideColumnRef.current;

			if (node) {
				const {top} = node.parentElement.getBoundingClientRect();

				const headerSize = top > HEADER_MARGIN ? top : HEADER_MARGIN;

				node.style.maxHeight = `calc(100vh - ${headerSize}px)`;
			}
		}, 250),
		[]
	);

	useEffect(() => {
		updateHeaderVisible();

		window.addEventListener('scroll', updateHeaderVisible);

		return () => window.removeEventListener('scroll', updateHeaderVisible);
	}, []);

	const {
		activeIndividualCount,
		criteriaString,
		includeAnonymousUsers,
		individualCount,
		knownIndividualCount,
		segmentType
	} = segment;

	return (
		<div className='overview-layout'>
			<div className='overview-column-main'>
				<SegmentProfileCard
					channelId={channelId}
					groupId={groupId}
					id={id}
					segment={segment}
					tabId={tabId}
				/>

				<InterestsCard
					channelId={channelId}
					groupId={groupId}
					id={id}
				/>

				<DistributionCard
					channelId={channelId}
					groupId={groupId}
					id={id}
				/>
			</div>

			<div className='overview-column-side' ref={_sideColumnRef}>
				<CompositionCard
					activeIndividualCount={activeIndividualCount}
					individualCount={individualCount}
					knownIndividualCount={knownIndividualCount}
				/>

				{segmentType === segmentTypes.dynamic && (
					<CriteriaCard
						criteriaString={criteriaString}
						includeAnonymousUsers={includeAnonymousUsers}
						segment={segment}
						timeZoneId={timeZoneId}
					/>
				)}
			</div>
		</div>
	);
};

export default connect((store, {groupId}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))(Overview);
