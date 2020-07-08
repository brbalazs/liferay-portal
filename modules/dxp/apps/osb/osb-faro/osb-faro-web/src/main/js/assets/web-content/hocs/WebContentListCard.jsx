import Card from 'shared/components/Card';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import WebContentListQuery from '../queries/WebContentListQuery';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {VIEWS_METRIC} from 'shared/util/pagination';
import {withBaseResults} from 'shared/hoc';

const withData = () =>
	graphql(
		WebContentListQuery,
		getMetricsMapper(result => ({
			items: result.journals.assetMetrics,
			total: result.journals.total
		}))
	);

const TableWithData = withBaseResults(withData, {
	defaultOrderByField: VIEWS_METRIC,
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
		}
	}) => [
		metricsListColumns.getTitleId({
			channelId,
			groupId,
			label: `${Liferay.Language.get('title')} | ${Liferay.Language.get(
				'id'
			).toUpperCase()}`,
			rangeKey,
			route: Routes.ASSETS_WEB_CONTENT_DASHBOARD
		}),
		metricsListColumns.viewsMetric
	],
	legacyDropdownRangeKey: false,
	rowIdentifier: ['assetId', 'assetTitle']
});

const WebContentListCard = props => (
	<Card className='web-content-root' pageDisplay>
		<TableWithData
			entityLabel={Liferay.Language.get('web-content')}
			rangeSelectors={getRangeSelectorsFromQuery(props.router.query)}
			{...props}
		/>
	</Card>
);

export default WebContentListCard;
