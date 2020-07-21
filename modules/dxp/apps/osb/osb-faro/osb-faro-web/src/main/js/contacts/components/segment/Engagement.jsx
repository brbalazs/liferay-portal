import EngagementChart from 'contacts/components/EngagementChart';
import EngagementWithList from 'contacts/components/Engagement';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {
	engagementListColumns,
	individualsListColumns
} from 'shared/util/table-columns';
import {PropTypes} from 'prop-types';
import {toThousands} from 'shared/util/numbers';

const {entityTypes} = FaroConstants;

function isNotCurrentMember({currentMember}) {
	return currentMember === false;
}

const tooltipLabels = {
	scoreLabel: Liferay.Language.get('segment-engagement'),
	subtitleLabel: Liferay.Language.get('x-individuals-in-segment')
};

const tooltipRenderRows = ({contributors}) => [
	{
		columns: [
			{
				label: Liferay.Language.get('active-members'),
				weight: 'normal'
			},
			{
				align: 'right',
				label: toThousands(contributors),
				weight: 'semibold'
			}
		]
	}
];

export class SegmentEngagementChart extends React.Component {
	render() {
		const {data, ...otherProps} = this.props;

		return (
			<EngagementChart
				{...otherProps}
				history={data}
				tooltipRenderRows={tooltipRenderRows}
			/>
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
