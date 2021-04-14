import Card from 'shared/components/Card';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import knownIndividualsListTouchpointQuery from 'shared/queries/knownIndividualsListTouchpointQuery';
import React from 'react';
import URLConstants from 'shared/util/url-constants';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {metricsListColumns} from 'shared/util/table-columns';
import {NAME, VIEWS_METRIC} from 'shared/util/pagination';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withBaseResults, withRangeKey} from 'shared/hoc';

const withData = () =>
	graphql(
		knownIndividualsListTouchpointQuery('page', VIEWS_METRIC),
		getMetricsMapper(result => ({
			items: result.page.viewsMetric.individuals.individuals,
			total: result.page.viewsMetric.individuals.total
		}))
	);

const TableWithData = withRangeKey(
	withBaseResults(withData, {
		defaultOrderByField: NAME,
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
		rowIdentifier: 'id',
		trackRangeKeyInState: true
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
