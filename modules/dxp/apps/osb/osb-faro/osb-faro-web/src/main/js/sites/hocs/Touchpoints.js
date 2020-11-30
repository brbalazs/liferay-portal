import Card from 'shared/components/Card';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import TouchpointsQuery, {
	TOUCHPOINTS_QUERY_TEST
} from 'sites/queries/TouchpointsQuery';
import urlConstants from 'shared/util/url-constants';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {
	metricsListColumns,
	sitePagesListColumns
} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {VISITORS_METRIC} from 'shared/util/pagination';
import {withBaseResults} from 'shared/hoc';

// POC TEMP
const withData = (useDB = false) => () =>
	graphql(
		useDB ? TOUCHPOINTS_QUERY_TEST : TouchpointsQuery,
		getMetricsMapper(result => ({
			items: result.pages.assetMetrics,
			total: result.pages.total
		}))
	);

// POC TEMP
const getTableWithData = useDB => {
	const TableWithData = withBaseResults(withData(useDB), {
		defaultOrderByField: VISITORS_METRIC,
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
		emptyTitle: Liferay.Language.get('empty-title-pages'),
		getColumns: ({
			router: {
				params: {channelId, groupId},
				query
			}
		}) => {
			const rangeSelectors = getRangeSelectorsFromQuery(query);

			return [
				sitePagesListColumns.getTitleUrl({
					channelId,
					groupId,
					rangeSelectors,
					route: Routes.SITES_TOUCHPOINTS_OVERVIEW
				}),
				metricsListColumns.engagementMetric,
				metricsListColumns.visitorsMetric,
				metricsListColumns.viewsMetric,
				metricsListColumns.avgTimeOnPageMetric,
				metricsListColumns.bounceRateMetric,
				metricsListColumns.entrancesMetric,
				metricsListColumns.exitRateMetric
			];
		},
		legacyDropdownRangeKey: false,
		rowIdentifier: ['assetId', 'assetTitle']
	});

	return TableWithData;
};

const Touchpoints = ({router}) => {
	const TableWithData = getTableWithData(router.query.useDB === 'true');

	return (
		<Card className='site-touchpoints-root' pageDisplay>
			<TableWithData
				entityLabel={Liferay.Language.get('pages')}
				rangeSelectors={getRangeSelectorsFromQuery(router.query)}
				router={router}
			/>
		</Card>
	);
};

export default Touchpoints;
