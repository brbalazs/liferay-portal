import Card from 'shared/components/Card';
import CustomAssetsListQuery from 'shared/queries/CustomAssetsListQuery';
import ListComponent from 'shared/hoc/ListComponent';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {
	createOrderIOMap,
	getGraphQLVariablesFromPagination,
	MODIFIED_DATE
} from 'shared/util/pagination';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {mapListResultsToProps} from 'shared/util/mappers';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';
import {useQueryPagination} from 'shared/hooks';

const CustomAssetsListCard: React.FC<{timeZoneId: string}> = ({timeZoneId}) => {
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(MODIFIED_DATE)
	});

	const {channelId, groupId} = useParams();

	const response = useQuery(CustomAssetsListQuery, {
		variables: {
			channelId,
			...getGraphQLVariablesFromPagination({
				delta,
				orderIOMap,
				page,
				query
			})
		}
	});

	return (
		<Card className='custom-assets-root' pageDisplay>
			<ListComponent
				{...mapListResultsToProps(response, result => ({
					items: result.dashboards.dashboards,
					total: result.dashboards.total
				}))}
				columns={[
					metricsListColumns.getTitleId({
						channelId,
						groupId,
						label: `${Liferay.Language.get(
							'asset'
						)} | ${Liferay.Language.get('id').toUpperCase()}`,
						rangeSelectors: getRangeSelectorsFromQuery(query),
						route: Routes.ASSETS_CUSTOM_DASHBOARD
					}),
					metricsListColumns.modifiedDate,
					metricsListColumns.getCreateDate(timeZoneId)
				]}
				delta={delta}
				entityLabel={Liferay.Language.get('custom-assets')}
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
				rowIdentifier='id'
				showFilterAndOrder={false}
			/>
		</Card>
	);
};

export default CustomAssetsListCard;
