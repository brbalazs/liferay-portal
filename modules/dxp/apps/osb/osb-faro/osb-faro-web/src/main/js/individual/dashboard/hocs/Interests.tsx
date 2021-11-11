import Card from 'shared/components/Card';
import Constants, {CompositionTypes} from 'shared/util/constants';
import InterestsQuery from '../queries/InterestsQuery';
import React from 'react';
import {compositionListColumns} from 'shared/util/table-columns';
import {
	getMapResultToProps,
	mapPropsToOptions
} from 'contacts/hoc/mappers/interests-query';
import {graphql} from '@apollo/react-hoc';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withBaseResults} from 'shared/hoc';

const {
	pagination: {cur: defaultPage, delta: defaultDelta}
} = Constants;

const withData = () =>
	graphql(InterestsQuery, {
		options: mapPropsToOptions,
		props: getMapResultToProps(CompositionTypes.IndividualInterests)
	});

const TableWithData = withBaseResults(withData, {
	defaultOrderByField: 'count',
	emptyPrimary: false,
	emptyTitle: sub(Liferay.Language.get('there-are-no-x-found'), [
		Liferay.Language.get('interests')
	]),
	getColumns: ({
		maxCount,
		router: {
			params: {channelId, groupId}
		},
		totalCount
	}) => [
		compositionListColumns.getName({
			label: Liferay.Language.get('topic'),
			maxWidth: 200,
			routeFn: ({data: {name}}) =>
				name &&
				toRoute(Routes.CONTACTS_INDIVIDUALS_INTEREST_DETAILS, {
					channelId,
					groupId,
					interestId: name
				}),
			sortable: true
		}),
		compositionListColumns.getRelativeMetricBar({
			label: Liferay.Language.get('total-individuals'),
			maxCount,
			sortable: true,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('total-individuals'),
			totalCount
		})
	],
	rowIdentifier: 'name',
	showDropdownRangeKey: false
});

const Interests = ({router}) => {
	const {
		query: {delta = defaultDelta, page = defaultPage}
	} = router;

	return (
		<Card pageDisplay>
			<Card.Header className='align-items-center d-flex justify-content-between'>
				<Card.Title>
					{Liferay.Language.get('interest-topics')}
				</Card.Title>
			</Card.Header>

			<TableWithData
				delta={delta}
				page={page}
				router={router}
				rowBordered={false}
			/>
		</Card>
	);
};

export default Interests;
