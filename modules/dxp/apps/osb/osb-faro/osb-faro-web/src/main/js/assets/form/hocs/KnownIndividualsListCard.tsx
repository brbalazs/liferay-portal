import Card from 'shared/components/Card';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import knownIndividualsListAssetQuery from 'shared/queries/knownIndividualsListAssetQuery';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {
	compose,
	withBaseResults,
	withQueryPagination,
	withQueryRangeSelectors
} from 'shared/hoc';
import {
	createOrderIOMap,
	NAME,
	SUBMISSIONS_METRIC
} from 'shared/util/pagination';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';

const withData = () =>
	graphql(
		knownIndividualsListAssetQuery('form', SUBMISSIONS_METRIC),
		getMetricsMapper(result => ({
			items: result.form.submissionsMetric.individuals.individuals,
			total: result.form.submissionsMetric.individuals.total
		}))
	);

const TableWithData = withBaseResults(withData, {
	emptyDescription: sub(
		Liferay.Language.get('empty-message-lists'),
		[
			<a
				href={URLConstants.DocumentationLink}
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
});
const KnownIndividualsListCard = props => (
	<Card className='known-individuals-root' pageDisplay>
		<TableWithData {...props} />
	</Card>
);

export default compose(
	withQueryPagination({initialOrderIOMap: createOrderIOMap(NAME)}),
	withQueryRangeSelectors({})
)(KnownIndividualsListCard);
