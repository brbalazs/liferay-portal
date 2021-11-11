import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import InterestsQuery from '../queries/InterestsQuery';
import React from 'react';
import {compositionListColumns} from 'shared/util/table-columns';
import {CompositionTypes} from 'shared/util/constants';
import {
	getMapResultToProps,
	mapCardPropsToOptions
} from 'contacts/hoc/mappers/interests-query';
import {graphql} from '@apollo/react-hoc';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {withTableData} from 'shared/hoc';

const withData = () =>
	graphql(InterestsQuery, {
		options: mapCardPropsToOptions,
		props: getMapResultToProps(CompositionTypes.IndividualInterests)
	});

const TableWithData = withTableData(withData, {
	emptyTitle: sub(Liferay.Language.get('there-are-no-x-found'), [
		Liferay.Language.get('interests')
	]),
	getColumns: ({channelId, groupId, maxCount, totalCount}) => [
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
			sortable: false
		}),
		compositionListColumns.getRelativeMetricBar({
			label: Liferay.Language.get('total-individuals'),
			maxCount,
			totalCount
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('total-individuals'),
			totalCount
		})
	],
	rowIdentifier: 'name'
});

interface IInterestsCardProps {
	channelId: string;
	groupId: string;
}

const InterestsCard: React.FC<IInterestsCardProps> = ({channelId, groupId}) => (
	<Card className='interests-card-root' minHeight={536}>
		<Card.Header>
			<Card.Title>
				{Liferay.Language.get('top-interests-as-of-today')}
			</Card.Title>
		</Card.Header>

		<TableWithData
			channelId={channelId}
			groupId={groupId}
			rowBordered={false}
		/>

		<Card.Footer>
			<Button
				display='link'
				href={toRoute(Routes.CONTACTS_INDIVIDUALS_INTERESTS, {
					channelId,
					groupId
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
