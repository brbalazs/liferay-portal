import Card from 'shared/components/Card';
import DocumentsAndMediaListQuery from 'shared/queries/DocumentsAndMediaListQuery';
import ListComponent from 'shared/hoc/ListComponent';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {
	createOrderIOMap,
	DOWNLOADS_METRIC,
	getGraphQLVariablesFromPagination
} from 'shared/util/pagination';
import {getSafeRangeSelectors} from 'shared/util/util';
import {mapListResultsToProps} from 'shared/util/mappers';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';
import {useQueryPagination, useQueryRangeSelectors} from 'shared/hooks';

const DocumentsAndMediaListCard: React.FC = () => {
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(DOWNLOADS_METRIC)
	});

	const {channelId, groupId} = useParams();
	const rangeSelectors = useQueryRangeSelectors();

	const response = useQuery(DocumentsAndMediaListQuery, {
		variables: {
			channelId,
			...getGraphQLVariablesFromPagination({
				delta,
				orderIOMap,
				page,
				query
			}),
			...getSafeRangeSelectors(rangeSelectors)
		}
	});

	return (
		<Card className='documents-and-media-root' pageDisplay>
			<ListComponent
				{...mapListResultsToProps(response, result => ({
					items: result.documents.assetMetrics,
					total: result.documents.total
				}))}
				columns={[
					metricsListColumns.getTitleId({
						channelId,
						groupId,
						label: `${Liferay.Language.get(
							'document-name'
						)} | ${Liferay.Language.get('id').toUpperCase()}`,
						rangeSelectors,
						route: Routes.ASSETS_DOCUMENTS_AND_MEDIA_OVERVIEW
					}),
					metricsListColumns.downloadsMetric,
					metricsListColumns.previewsMetric,
					metricsListColumns.commentsMetric,
					metricsListColumns.ratingsMetric
				]}
				delta={delta}
				entityLabel={Liferay.Language.get('documents-and-media')}
				legacyDropdownRangeKey={false}
				noResultsProps={{
					description: sub(
						Liferay.Language.get('empty-message-lists'),
						[
							<a
								href={URLConstants.DocumentationLink}
								key='DOCUMENTATION'
								target='_blank'
							>
								{Liferay.Language.get(
									'documentation'
								).toLowerCase()}
							</a>
						],
						false
					),
					title: Liferay.Language.get('empty-title-assets')
				}}
				orderIOMap={orderIOMap}
				page={page}
				query={query}
				rangeSelectors={rangeSelectors}
				rowIdentifier={['assetId', 'assetTitle']}
				showDropdownRangeKey
				showFilterAndOrder={false}
			/>
		</Card>
	);
};

export default DocumentsAndMediaListCard;
