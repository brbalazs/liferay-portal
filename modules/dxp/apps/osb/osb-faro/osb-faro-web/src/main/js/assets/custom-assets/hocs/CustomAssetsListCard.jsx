import Card from 'shared/components/Card';
import CustomAssetsListQuery from '../queries/CustomAssetsListQuery';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {MODIFIED_DATE} from 'shared/util/pagination';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withBaseResults} from 'shared/hoc';

const withData = () =>
	graphql(
		CustomAssetsListQuery,
		getMetricsMapper(result => ({
			items: result.dashboards.dashboards,
			total: result.dashboards.total
		}))
	);

const TableWithData = withBaseResults(withData, {
	defaultOrderByField: MODIFIED_DATE,
	emptyDescription: sub(
		Liferay.Language.get('empty-message-lists'),
		[
			<a
				href={urlConstants.DOCUMENTATION_LINK}
				key='DOCUMENTATION'
				target='_blank'
			>
				{Liferay.Language.get('documentation').toLowerCase()}
			</a>
		],
		false
	),
	emptyTitle: Liferay.Language.get('empty-title-assets'),
	getColumns: ({
		router: {
			params: {channelId, groupId},
			query: {rangeKey}
		},
		timeZoneId
	}) => [
		metricsListColumns.getTitleId({
			channelId,
			groupId,
			label: `${Liferay.Language.get('asset')} | ${Liferay.Language.get(
				'id'
			).toUpperCase()}`,
			rangeKey,
			route: Routes.ASSETS_CUSTOM_DASHBOARD
		}),
		metricsListColumns.modifiedDate,
		metricsListColumns.getCreateDate(timeZoneId)
	],
	rowIdentifier: 'id',
	showDropdownRangeKey: false
});

const CustomAssetsListCard = props => (
	<Card className='custom-assets-root' pageDisplay>
		<TableWithData
			entityLabel={Liferay.Language.get('custom-assets')}
			{...props}
		/>
	</Card>
);

export default CustomAssetsListCard;
