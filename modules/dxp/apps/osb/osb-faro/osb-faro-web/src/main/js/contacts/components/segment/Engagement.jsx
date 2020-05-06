import EngagementWithList, {
	EngagementChart
} from 'contacts/components/Engagement';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {
	engagementListColumns,
	individualsListColumns
} from 'shared/util/table-columns';
import {PropTypes} from 'prop-types';

const {entityTypes} = FaroConstants;

function isNotCurrentMember({currentMember}) {
	return currentMember === false;
}

const tooltipLabels = {
	scoreLabel: Liferay.Language.get('segment-engagement'),
	subtitleLabel: Liferay.Language.get('x-individuals-in-segment')
};

export class SegmentEngagementChart extends React.Component {
	render() {
		return (
			<EngagementChart {...this.props} tooltipLabels={tooltipLabels} />
		);
	}
}

export default class SegmentEngagementWithList extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.any.isRequired
	};

	render() {
		const {channelId, groupId, ...otherProps} = this.props;

		return (
			<EngagementWithList
				{...omitDefinedProps(
					otherProps,
					SegmentEngagementWithList.propTypes
				)}
				checkDisabledFn={isNotCurrentMember}
				columns={[
					engagementListColumns.getName({channelId, groupId}),
					individualsListColumns.accountNames,
					engagementListColumns.emailAddress,
					engagementListColumns.currentMember,
					engagementListColumns.score
				]}
				entityType={entityTypes.individualsSegment}
				groupId={groupId}
				tooltipLabels={tooltipLabels}
			/>
		);
	}
}
