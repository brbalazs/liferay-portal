import Button from 'shared/components/Button';
import Card from 'shared/components/Card';
import Constants, {
	jobStatuses,
	jobTrainingFrequencies,
	jobTrainingPeriods,
	jobTypes
} from 'shared/util/constants';
import Label from 'shared/components/Label';
import Nav from 'shared/components/Nav';
import React from 'react';
import RecommendationListQuery from '../queries/RecommendationListQuery';
import {compose} from 'redux';
import {formatUTCDate} from 'shared/util/date';
import {getFormattedTitle} from 'shared/components/NoResultsDisplay';
import {
	getMapPropsToOptions,
	getMapResultToProps
} from 'shared/hoc/mappers/metrics';
import {graphql} from '@apollo/react-hoc';
import {
	JOB_STATUSES_DISPLAY_MAP,
	JOB_STATUSES_LABEL_MAP,
	JOB_TRAINING_FREQUENCIES_LABEL_MAP,
	JOB_TRAINING_PERIODS_LABEL_MAP,
	JOB_TYPES_LABEL_MAP
} from '../utils/utils';
import {NAME} from 'shared/util/pagination';
import {NameCell} from 'shared/components/table/cell-components';
import {RouterType} from 'shared/types';
import {Routes, toRoute} from 'shared/util/router';
import {User} from 'shared/util/records';
import {
	useSelectionContext,
	withSelectionProvider
} from 'shared/context/selection';
import {withCrossPageSelect, withCurrentUser} from 'shared/hoc';

const {
	pagination: {orderDescending}
} = Constants;

const withData = () =>
	graphql(RecommendationListQuery, {
		options: (props: any) => ({
			...getMapPropsToOptions(RecommendationListQuery)(props),
			fetchPolicy: 'no-cache'
		}),
		props: getMapResultToProps(({jobs: {jobs, total}}) => ({
			items: jobs,
			total
		}))
	});

const RecommendationListWithData = withCrossPageSelect(withData, {
	defaultOrderByField: NAME,
	emptyTitle: getFormattedTitle(
		Liferay.Language.get('recommendations').toLowerCase()
	),
	getColumns: ({groupId}) => [
		{
			accessor: 'name',
			cellRenderer: NameCell,
			cellRendererProps: {
				routeFn: ({data: {id}}) =>
					toRoute(Routes.SETTINGS_RECOMMENDATION_MODEL_VIEW, {
						groupId,
						jobId: id
					})
			},
			className: 'table-cell-expand',
			label: Liferay.Language.get('name')
		},
		{
			accessor: 'type',
			dataFormatter: (type: jobTypes) => JOB_TYPES_LABEL_MAP[type],
			label: Liferay.Language.get('training-model')
		},
		{
			accessor: 'trainingPeriod',
			dataFormatter: (type: jobTrainingPeriods) =>
				JOB_TRAINING_PERIODS_LABEL_MAP[type],
			label: Liferay.Language.get('training-period')
		},
		{
			accessor: 'trainingFrequency',
			dataFormatter: (type: jobTrainingFrequencies) =>
				JOB_TRAINING_FREQUENCIES_LABEL_MAP[type],
			label: Liferay.Language.get('training-frequency')
		},
		{
			accessor: 'trainingDate',
			dataFormatter: (date: string) => formatUTCDate(date, 'MM DD, YYYY'),
			label: Liferay.Language.get('last-trained')
		},
		{
			accessor: 'status',
			cellRenderer: ({data: {status}}: {data: {status: jobStatuses}}) => (
				<td>
					<Label
						className='status'
						display={JOB_STATUSES_DISPLAY_MAP[status]}
						size='lg'
						uppercase
					>
						{JOB_STATUSES_LABEL_MAP[status]}
					</Label>
				</td>
			),
			label: Liferay.Language.get('status')
		}
	],
	page: false,
	primary: true,
	rowIdentifier: 'id',
	showDropdownRangeKey: false
});

interface IRecommendationListProps {
	currentUser: User;
	groupId: string;
	router: RouterType;
}

const RecommendationList: React.FC<IRecommendationListProps> = ({
	currentUser,
	groupId,
	router,
	...otherProps
}) => {
	const {selectedItems} = useSelectionContext();

	const renderNav = () => {
		if (!currentUser.isAdmin()) {
			return null;
		}

		if (selectedItems.size) {
			return (
				<Nav>
					<Nav.Item>
						{
							<Button
								borderless
								display='secondary'
								onClick={() => {
									// TODO LRAC-6083 Add delete functionality
								}}
								outline
							>
								{Liferay.Language.get('delete')}
							</Button>
						}
					</Nav.Item>
				</Nav>
			);
		}

		return (
			<Nav>
				<Nav.Item>
					{
						<Button
							className='nav-btn'
							display='primary'
							href={toRoute(
								Routes.SETTINGS_RECOMMENDATIONS_CREATE_ITEM_SIMILARITY_MODEL,
								{groupId}
							)}
						>
							{Liferay.Language.get('new-model')}
						</Button>
					}
				</Nav.Item>
			</Nav>
		);
	};

	return (
		<Card className='recommendations-list-root' pageDisplay>
			<RecommendationListWithData
				{...otherProps}
				defaultOrderBy={orderDescending}
				defaultOrderByField={NAME}
				defaultSort={{field: NAME, sortOrder: orderDescending}}
				entityLabel={Liferay.Language.get('recommendations')}
				groupId={groupId}
				renderNav={renderNav}
				router={router}
			/>
		</Card>
	);
};

export default compose<any>(
	withCurrentUser,
	withSelectionProvider
)(RecommendationList);
