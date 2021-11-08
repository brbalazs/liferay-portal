import Card from 'shared/components/Card';
import getCN from 'classnames';
import getMetricsMapper from 'shared/hoc/mappers/metrics';
import React from 'react';
import TouchpointsQuery from 'shared/queries/TouchpointsQuery';
import URLConstants from 'shared/util/url-constants';
import {getRangeSelectorsFromQuery} from 'shared/util/util';
import {graphql} from '@apollo/react-hoc';
import {
	metricsListColumns,
	sitePagesListColumns
} from 'shared/util/table-columns';
import {RangeSelectors, Router} from 'shared/types';
import {Routes} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {VISITORS_METRIC} from 'shared/util/pagination';
import {withBaseResults, withRangeKey} from 'shared/hoc';

const withData = () =>
	graphql(
		TouchpointsQuery,
		getMetricsMapper(result => ({
			items: result.pages.assetMetrics,
			total: result.pages.total
		}))
	);

interface ITableWithDataProps {
	rangeSelectors: RangeSelectors;
	router: Router;
}

const TableWithData: React.FC<ITableWithDataProps> = withRangeKey(
	withBaseResults(withData, {
		defaultOrderByField: VISITORS_METRIC,
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
				metricsListColumns.visitorsMetric,
				metricsListColumns.viewsMetric,
				metricsListColumns.avgTimeOnPageMetric,
				metricsListColumns.bounceRateMetric,
				metricsListColumns.entrancesMetric,
				metricsListColumns.exitRateMetric
			];
		},
		legacyDropdownRangeKey: false,
		rowIdentifier: 'assetId',
		showDropdownRangeKey: true
	})
);

interface IInterestDetailsProps {
	className?: string;
	router: Router;
}

const InterestDetails: React.FC<IInterestDetailsProps> = ({
	className,
	router
}) => {
	const {
		params: {interestId},
		query
	} = router;

	return (
		<Card className={getCN(className)} pageDisplay>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				<Card.Title>
					{sub(
						Liferay.Language.get('pages-containing-x'),
						[
							<span className='interest-title' key='INTEREST_ID'>
								{`"${interestId}"`}
							</span>
						],
						false
					)}
				</Card.Title>
			</Card.Header>

			<TableWithData
				rangeSelectors={getRangeSelectorsFromQuery(query)}
				router={router}
			/>
		</Card>
	);
};

export default InterestDetails;
