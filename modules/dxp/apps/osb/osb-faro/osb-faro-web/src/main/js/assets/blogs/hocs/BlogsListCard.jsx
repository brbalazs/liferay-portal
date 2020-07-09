import BlogsListQuery from '../queries/BlogsListQuery';
import Card from 'shared/components/Card';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {VIEWS_METRIC} from 'shared/util/pagination';
import {withBaseResults} from 'shared/hoc';

const withData = () =>
	graphql(
		BlogsListQuery,
		getMetricsMapper(result => ({
			items: result.blogs.assetMetrics,
			total: result.blogs.total
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
	entityLabel: Liferay.Language.get('blogs'),
	getColumns: ({
		router: {
			params: {channelId, groupId},
			query
		}
	}) => [
		metricsListColumns.getTitleId({
			channelId,
			groupId,
			label: `${Liferay.Language.get(
				'blog-name'
			)} | ${Liferay.Language.get('id').toUpperCase()}`,
			rangeSelectors: getRangeSelectorsFromQuery(query),
			route: Routes.ASSETS_BLOGS_DASHBOARD
		}),
		metricsListColumns.viewsMetric,
		metricsListColumns.readingTimeMetric,
		metricsListColumns.commentsMetric,
		metricsListColumns.ratingsMetric
	],
	legacyDropdownRangeKey: false,
	rowIdentifier: ['assetId', 'assetTitle']
});

const BlogsListCard = props => (
	<Card className='blogs-root' pageDisplay>
		<TableWithData
			entityLabel={Liferay.Language.get('blogs')}
			rangeSelectors={getRangeSelectorsFromQuery(props.router.query)}
			{...props}
		/>
	</Card>
);

export default BlogsListCard;
