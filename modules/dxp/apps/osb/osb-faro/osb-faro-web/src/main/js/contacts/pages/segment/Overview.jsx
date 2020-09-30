import autobind from 'autobind-decorator';
import CompositionCard from 'contacts/components/segment/CompositionCard';
import CriteriaCard from 'contacts/components/segment/criteria-card';
import debounce from 'shared/util/debounce-decorator';
import DistributionCard from 'contacts/hoc/segment/DistributionCard';
import FaroConstants from 'shared/util/constants';
import InterestsCard from 'contacts/hoc/segment/InterestsCard';
import React from 'react';
import SegmentProfileCard from 'contacts/components/segment/ProfileCard';
import {connect} from 'react-redux';
import {GROWTH} from 'shared/util/router';
import {PropTypes} from 'prop-types';
import {Segment} from 'shared/util/records';

const {segmentTypes} = FaroConstants;

const HEADER_MARGIN = 16;

@connect((store, {groupId}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))
export default class Overview extends React.Component {
	static defaultProps = {
		tabId: GROWTH
	};

	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.string.isRequired,
		id: PropTypes.string.isRequired,
		segment: PropTypes.instanceOf(Segment).isRequired,
		tabId: PropTypes.string,
		timeZoneId: PropTypes.string
	};

	_sideColumnRef = React.createRef();

	componentDidMount() {
		this.updateHeaderVisible();

		window.addEventListener('scroll', this.updateHeaderVisible);
	}

	componentWillUnmount() {
		window.removeEventListener('scroll', this.updateHeaderVisible);
	}

	@debounce(250)
	@autobind
	updateHeaderVisible() {
		const node = this._sideColumnRef.current;

		if (node) {
			const {top} = node.parentElement.getBoundingClientRect();

			const headerSize = top > HEADER_MARGIN ? top : HEADER_MARGIN;

			node.style.maxHeight = `calc(100vh - ${headerSize}px)`;
		}
	}

	render() {
		const {channelId, groupId, id, segment, tabId, timeZoneId} = this.props;

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

				<div className='overview-column-side' ref={this._sideColumnRef}>
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
	}
}
