import Card from 'shared/components/Card';
import FormsListQuery from '../queries/FormsListQuery';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import urlConstants from 'shared/util/url-constants';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {SUBMISSIONS_METRIC} from 'shared/util/pagination';
import {withBaseResults} from 'shared/hoc';

const withData = () =>
	graphql(
		FormsListQuery,
		getMetricsMapper(result => ({
			items: result.forms.assetMetrics,
			total: result.forms.total
		}))
	);

const TableWithData = withBaseResults(withData, {
	defaultOrderByField: SUBMISSIONS_METRIC,
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
			query
		}
	}) => [
		metricsListColumns.getTitleId({
			channelId,
			groupId,
			label: `${Liferay.Language.get(
				'form-name'
			)} | ${Liferay.Language.get('id').toUpperCase()}`,
			rangeSelectors: getRangeSelectorsFromQuery(query),
			route: Routes.ASSETS_FORMS_DASHBOARD
		}),
		metricsListColumns.submissionsMetric,
		metricsListColumns.viewsMetric,
		metricsListColumns.abandonmentsMetric,
		metricsListColumns.completionTimeMetric
	],
	legacyDropdownRangeKey: false,
	rowIdentifier: ['assetId', 'assetTitle']
});

const FormsListCard = props => (
	<Card className='forms-root' pageDisplay>
		<TableWithData
			entityLabel={Liferay.Language.get('forms')}
			rangeSelectors={getRangeSelectorsFromQuery(props.router.query)}
			{...props}
		/>
	</Card>
);

export default FormsListCard;
