import EngagementWithList from 'contacts/components/Engagement';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {engagementListColumns} from 'shared/util/table-columns';
import {PropTypes} from 'prop-types';

const {entityTypes} = FaroConstants;

export default class AccountEngagement extends React.Component {
	static propTypes = {
		channelId: PropTypes.string,
		groupId: PropTypes.oneOfType([PropTypes.number, PropTypes.string])
			.isRequired
	};

	render() {
		const {channelId, groupId, ...otherProps} = this.props;

		return (
			<EngagementWithList
				{...omitDefinedProps(otherProps, AccountEngagement.propTypes)}
				columns={[
					engagementListColumns.getName({channelId, groupId}),
					engagementListColumns.emailAddress,
					engagementListColumns.activitiesCount,
					engagementListColumns.score
				]}
				entityType={entityTypes.account}
				groupId={groupId}
				tooltipLabels={{
					scoreLabel: Liferay.Language.get('account-engagement'),
					subtitleLabel: Liferay.Language.get('x-active-members')
				}}
			/>
		);
	}
}
