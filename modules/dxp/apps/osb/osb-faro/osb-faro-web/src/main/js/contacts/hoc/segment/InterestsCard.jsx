import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import FaroConstants from 'shared/util/constants';
import getInterestsQuery from 'contacts/queries/InterestsQuery';
import React from 'react';
import {compositionListColumns} from 'shared/util/table-columns';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from 'contacts/hoc/mappers/interests-query';
import {graphql} from '@apollo/react-hoc';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withTableData} from 'shared/hoc';

const {
	compositionTypes: {segmentInterests}
} = FaroConstants;

const withData = () =>
	graphql(getInterestsQuery(segmentInterests), {
		options: mapCardPropsToOptions,
		props: getMapResultToProps(segmentInterests)
	});

const TableWithData = withTableData(withData, {
	emptyTitle: sub(Liferay.Language.get('there-are-no-x-found'), [
		Liferay.Language.get('interests')
	]),
	getColumns: ({channelId, groupId, id, maxCount, totalCount}) => [
		compositionListColumns.getName({
			label: Liferay.Language.get('topic'),
			maxWidth: 200,
			routeFn: ({data: {name}}) =>
				name &&
				toRoute(Routes.CONTACTS_SEGMENT_INTEREST_DETAILS, {
					channelId,
					groupId,
					id,
					interestId: name
				}),
			sortable: false
		}),
		compositionListColumns.getRelativeMetricBar({
			label: sub(Liferay.Language.get('x-members'), [
				Liferay.Language.get('segment')
			]),
			maxCount,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('active-members'),
			totalCount
		})
	],
	rowIdentifier: 'name'
});

const InterestsCard = ({channelId, groupId, id}) => (
	<Card className='interests-card-root'>
		<Card.Header>
			<Card.Title>{Liferay.Language.get('top-interests')}</Card.Title>
		</Card.Header>

		<TableWithData
			channelId={channelId}
			groupId={groupId}
			id={id}
			rowBordered={false}
		/>

		<Card.Footer>
			<Button
				display='link'
				href={toRoute(Routes.CONTACTS_SEGMENT_INTERESTS, {
					channelId,
					groupId,
					id
				})}
				icon='angle-right'
				iconAlignment='right'
				size='sm'
			>
				{Liferay.Language.get('view-all-interests')}
			</Button>
		</Card.Footer>
	</Card>
);

export default InterestsCard;
