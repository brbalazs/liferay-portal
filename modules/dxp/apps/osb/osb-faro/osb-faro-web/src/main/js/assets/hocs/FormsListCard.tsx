import Card from 'shared/components/Card';
import FormsListQuery from 'shared/queries/FormsListQuery';
import ListComponent from 'shared/hoc/ListComponent';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {
	createOrderIOMap,
	getGraphQLVariablesFromPagination,
	SUBMISSIONS_METRIC
} from 'shared/util/pagination';
import {getSafeRangeSelectors} from 'shared/util/util';
import {mapListResultsToProps} from 'shared/util/mappers';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';
import {useQueryPagination, useQueryRangeSelectors} from 'shared/hooks';

const FormsListCard: React.FC = () => {
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(SUBMISSIONS_METRIC)
	});

	const {channelId, groupId} = useParams();
	const rangeSelectors = useQueryRangeSelectors();

	const response = useQuery(FormsListQuery, {
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
		<Card className='forms-root' pageDisplay>
			<ListComponent
				{...mapListResultsToProps(response, result => ({
					items: result.forms.assetMetrics,
					total: result.forms.total
				}))}
				columns={[
					metricsListColumns.getTitleId({
						channelId,
						groupId,
						label: `${Liferay.Language.get(
							'form-name'
						)} | ${Liferay.Language.get('id').toUpperCase()}`,
						rangeSelectors,
						route: Routes.ASSETS_FORMS_OVERVIEW
					}),
					metricsListColumns.submissionsMetric,
					metricsListColumns.viewsMetric,
					metricsListColumns.abandonmentsMetric,
					metricsListColumns.completionTimeMetric
				]}
				delta={delta}
				entityLabel={Liferay.Language.get('forms')}
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

export default FormsListCard;
