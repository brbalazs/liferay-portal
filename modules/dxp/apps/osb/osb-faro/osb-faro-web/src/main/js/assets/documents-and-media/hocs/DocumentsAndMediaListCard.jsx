import Card from 'shared/components/Card';
import DocumentsAndMediaListQuery from '../queries/DocumentsAndMediaListQuery';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import {DOWNLOADS_METRIC} from 'shared/util/pagination';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withBaseResults} from 'shared/hoc';

const withData = () =>
	graphql(
		DocumentsAndMediaListQuery,
		getMetricsMapper(result => ({
			items: result.documents.assetMetrics,
			total: result.documents.total
		}))
	);

const TableWithData = withBaseResults(withData, {
	defaultOrderByField: DOWNLOADS_METRIC,
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
			label: `${Liferay.Language.get(
				'document-name'
			)} | ${Liferay.Language.get('id').toUpperCase()}`,
			rangeKey,
			route: Routes.ASSETS_DOCUMENTS_AND_MEDIA_DASHBOARD
		}),
		metricsListColumns.downloadsMetric,
		metricsListColumns.previewsMetric,
		metricsListColumns.commentsMetric,
		metricsListColumns.ratingsMetric
	],
	legacyDropdownRangeKey: false,
	rowIdentifier: ['assetId', 'assetTitle']
});

const DocumentsAndMediaListCard = props => (
	<Card className='documents-and-media-root' pageDisplay>
		<TableWithData
			entityLabel={Liferay.Language.get('documents-and-media')}
			rangeSelectors={getRangeSelectorsFromQuery(props.router.query)}
			{...props}
		/>
	</Card>
);

export default DocumentsAndMediaListCard;
