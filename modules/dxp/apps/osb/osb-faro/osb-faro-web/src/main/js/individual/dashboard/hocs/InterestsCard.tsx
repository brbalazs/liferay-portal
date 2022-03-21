import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import ErrorDisplay from 'shared/components/ErrorDisplay';
import IndividualInterestsQuery, {
	IIndividualInterestsData,
	IIndividualInterestsVariables
} from 'shared/queries/IndividualInterestsQuery';
import React from 'react';
import StatesRenderer from 'shared/components/states-renderer/StatesRenderer';
import Table, {Column} from 'shared/components/table';
import {compositionListColumns} from 'shared/util/table-columns';
import {COUNT} from 'shared/util/pagination';
import {OrderByDirections} from 'shared/util/constants';
import {Routes, toRoute} from 'shared/util/router';
import {useParams} from 'react-router-dom';
import {useQuery} from '@apollo/react-hooks';

const EMPTY_STATE_DATA = new Array(6).fill({count: 0, name: ''});

const InterestsCard: React.FC<React.HTMLAttributes<HTMLElement>> = () => {
	const {channelId, groupId} = useParams();
	const {
		data = {
			individualInterests: {compositions: [], maxCount: 0, totalCount: 0}
		},
		error,
		loading
	} = useQuery<IIndividualInterestsData, IIndividualInterestsVariables>(
		IndividualInterestsQuery,
		{
			variables: {
				active: true,
				channelId,
				id: groupId,
				size: 5,
				sort: {
					column: COUNT,
					type: OrderByDirections.Descending
				},
				start: 0
			}
		}
	);

	const {
		individualInterests: {compositions: items, maxCount, totalCount}
	} = data;

	const getColumn: () => Column[] = () => [
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
	];

	const getEmptyColumns: () => Column[] = () => [
		compositionListColumns.getName({
			label: Liferay.Language.get('topic'),
			maxWidth: 200,
			sortable: false
		}),
		compositionListColumns.getRelativeMetricBar({
			empty: true,
			label: Liferay.Language.get('total-individuals'),
			maxCount: 1,
			totalCount: 1
		}),
		compositionListColumns.getPercentOf({
			metricName: Liferay.Language.get('total-individuals'),
			totalCount: 1
		})
	];

	return (
		<Card className='interests-card-root' minHeight={536}>
			<Card.Header>
				<Card.Title>
					{Liferay.Language.get('top-interests-as-of-today')}
				</Card.Title>
			</Card.Header>

			<StatesRenderer
				empty={!items.length}
				error={!!error}
				loading={loading}
			>
				<StatesRenderer.Empty>
					<Table
						columns={getEmptyColumns()}
						empty
						items={EMPTY_STATE_DATA}
						rowBordered={false}
						rowIdentifier='name'
					/>
				</StatesRenderer.Empty>

				<StatesRenderer.Error>
					<ErrorDisplay spacer />
				</StatesRenderer.Error>

				<StatesRenderer.Success>
					<Table
						columns={getColumn()}
						items={items}
						rowBordered={false}
						rowIdentifier='name'
					/>
				</StatesRenderer.Success>
			</StatesRenderer>

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
};

export default InterestsCard;
