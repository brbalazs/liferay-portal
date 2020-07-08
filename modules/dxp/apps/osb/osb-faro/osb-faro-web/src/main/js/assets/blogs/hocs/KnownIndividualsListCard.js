import Card from 'shared/components/Card';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import knownIndividualsListAssetQuery from 'shared/queries/knownIndividualsListAssetQuery';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {NAME, VIEWS_METRIC} from 'shared/util/pagination';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withBaseResults, withRangeKey} from 'shared/hoc';

const withData = () =>
	graphql(
		knownIndividualsListAssetQuery('blog', VIEWS_METRIC),
		getMetricsMapper(result => ({
			items: result.blog.viewsMetric.individuals.individuals,
			total: result.blog.viewsMetric.individuals.total
		}))
	);

const TableWithData = withRangeKey(
	withBaseResults(withData, {
		defaultOrderByField: NAME,
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
				params: {channelId, groupId}
			}
		}) => [
			metricsListColumns.getNameEmail({
				channelId,
				groupId,
				route: Routes.CONTACTS_INDIVIDUAL
			})
		],
		legacyDropdownRangeKey: false,
		rowIdentifier: 'id'
	})
);

const KnownIndividualsListCard = props => (
	<Card className='known-individuals-root' pageDisplay>
		<TableWithData
			{...props}
			rangeSelectors={getRangeSelectorsFromQuery(props.router.query)}
		/>
	</Card>
);

export default KnownIndividualsListCard;
