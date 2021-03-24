import BasePage from 'settings/components/BasePage';
import BlockListCard from '../components/BlockListCard';
import React from 'react';
import {connect} from 'react-redux';
import {getDefinitions} from 'shared/util/breadcrumbs';
import {Routes, toRoute} from 'shared/util/router';

interface IBlockListProps {
	groupId: string;
	timeZoneId: string;
}

const BlockList: React.FC<IBlockListProps> = ({
	groupId,
	timeZoneId,
	...otherProps
}) => (
	<BasePage
		breadcrumbItems={[
			getDefinitions({groupId}),
			{
				href: toRoute(Routes.SETTINGS_DEFINITIONS_EVENTS_CUSTOM, {
					groupId
				}),
				label: Liferay.Language.get('events')
			},
			{active: true, label: Liferay.Language.get('block-list')}
		]}
		groupId={groupId}
		pageDescription={Liferay.Language.get(
			'blocked-events-are-not-processed-by-analytics-cloud'
		)}
		pageTitle={Liferay.Language.get('block-list')}
	>
		<BlockListCard
			groupId={groupId}
			timeZoneId={timeZoneId}
			{...otherProps}
		/>
	</BasePage>
);

export default connect((store, {groupId}) => ({
	timeZoneId: store.getIn([
		'projects',
		groupId,
		'data',
		'timeZone',
		'timeZoneId'
	])
}))(BlockList);
