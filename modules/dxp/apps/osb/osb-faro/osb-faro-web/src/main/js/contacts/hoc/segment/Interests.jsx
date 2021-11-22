import Card from 'shared/components/Card';
import getInterestsQuery from 'contacts/queries/InterestsQuery';
import React from 'react';
import {compositionListColumns} from 'shared/util/table-columns';
import {CompositionTypes} from 'shared/util/constants';
import {COUNT, createOrderIOMap} from 'shared/util/pagination';
import {
	getMapResultToProps,
	mapPropsToOptions
} from 'contacts/hoc/mappers/interests-query';
import {graphql} from '@apollo/react-hoc';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {useParams} from 'react-router-dom';
import {useQueryPagination} from 'shared/hooks';
import {withBaseResults} from 'shared/hoc';

const withData = () =>
	graphql(getInterestsQuery(CompositionTypes.SegmentInterests), {
		options: mapPropsToOptions,
		props: getMapResultToProps(CompositionTypes.SegmentInterests)
	});

const TableWithData = withBaseResults(withData, {
	emptyPrimary: false,
	emptyTitle: sub(Liferay.Language.get('there-are-no-x-found'), [
		Liferay.Language.get('interests')
	]),
	getColumns: ({
		maxCount,
		router: {
			params: {channelId, groupId, id}
		},
		totalCount
	}) => [
		compositionListColumns.getName({
			label: Liferay.Language.get('topic'),
			routeFn: ({data: {name}}) =>
				name &&
				toRoute(Routes.CONTACTS_SEGMENT_INTEREST_DETAILS, {
					channelId,
					groupId,
					id,
					interestId: name
				}),
			sortable: true
		}),
		compositionListColumns.getRelativeMetricBar({
			label: Liferay.Language.get('segment-members'),
			maxCount,
			sortable: true,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('active-members'),
			totalCount
		})
	],
	rowIdentifier: 'name',
	showDropdownRangeKey: false
});

const Interests = props => {
	const {channelId, id} = useParams();
	const {delta, orderIOMap, page, query} = useQueryPagination({
		initialOrderIOMap: createOrderIOMap(COUNT)
	});

	return (
		<Card pageDisplay>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				<Card.Title>
					{Liferay.Language.get('interest-topics')}
				</Card.Title>
			</Card.Header>

			<TableWithData
				{...props}
				channelId={channelId}
				delta={delta}
				id={id}
				orderIOMap={orderIOMap}
				page={page}
				query={query}
				rowBordered={false}
			/>
		</Card>
	);
};

export default Interests;
