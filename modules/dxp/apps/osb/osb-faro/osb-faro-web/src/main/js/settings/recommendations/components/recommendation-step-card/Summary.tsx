import Constants from 'shared/util/constants';
import React from 'react';
import RecommendationActivitiesQuery from '../../queries/RecommendationActivitiesQuery';
import RecommendationPageAssetsQuery from '../../queries/RecommendationPageAssetsQuery';
import Table from 'shared/components/table';
import {DateCell} from 'shared/components/table/cell-components';
import {
	Filter,
	getPropertiesFromItems,
	JOB_TRAINING_FREQUENCIES_LABEL_MAP,
	JOB_TRAINING_PERIODS_LABEL_MAP,
	JOB_TYPES_LABEL_MAP,
	JobProperty
} from '../../utils/utils';
import {get} from 'lodash';
import {getDate} from 'shared/util/date';
import {
	jobTrainingFrequencies,
	jobTrainingPeriods,
	jobTypes
} from 'shared/util/constants';
import {useQuery} from '@apollo/react-hooks';

const {
	pagination: {orderDescending}
} = Constants;

interface ISummaryProps {
	initialValues: any;
	itemFilters: Filter[];
	name: string;
	trainingDate: string;
	trainingFrequency: jobTrainingFrequencies;
	trainingPeriod: jobTrainingPeriods;
	type: jobTypes;
}

const Summary: React.FC<ISummaryProps> = ({
	initialValues,
	itemFilters,
	name,
	trainingDate,
	trainingFrequency,
	trainingPeriod,
	type
}) => {
	const propertyFilters: JobProperty[] = getPropertiesFromItems(itemFilters);

	const {data: pageAssetsData} = useQuery(RecommendationPageAssetsQuery, {
		variables: {
			propertyFilters,
			size: 0,
			sort: {
				column: 'title',
				type: orderDescending.toUpperCase()
			},
			start: 0
		}
	});

	const {data: activitiesData} = useQuery(RecommendationActivitiesQuery, {
		variables: {
			applicationId: 'Page',
			eventContextPropertyFilters: propertyFilters,
			eventId: 'pageUnloaded',
			size: 0,
			start: 0
		}
	});

	const trainingFrequencyChanged: boolean =
		initialValues.trainingFrequency !== trainingFrequency;

	return (
		<div className='summary-root'>
			<div className='title'>{Liferay.Language.get('summary')}</div>

			<table>
				<tbody>
					<tr>
						<td>{Liferay.Language.get('name')}</td>

						<td>
							<span className='summary-value'>{name}</span>
						</td>
					</tr>

					<tr>
						<td>{Liferay.Language.get('model-type')}</td>

						<td>
							<span className='summary-value'>
								{JOB_TYPES_LABEL_MAP[type]}
							</span>
						</td>
					</tr>

					<tr>
						<td>{Liferay.Language.get('training-frequency')}</td>

						<td>
							<span className='summary-value'>
								{
									JOB_TRAINING_FREQUENCIES_LABEL_MAP[
										trainingFrequency
									]
								}
							</span>
						</td>
					</tr>

					<tr>
						<td>{Liferay.Language.get('training-period')}</td>

						<td>
							<span className='summary-value'>
								{JOB_TRAINING_PERIODS_LABEL_MAP[trainingPeriod]}
							</span>
						</td>
					</tr>
				</tbody>
			</table>

			<Table
				columns={[
					{
						accessor: 'trainingDate',
						cellRenderer: DateCell,
						cellRendererProps: {
							datePath: 'trainingDate'
						},
						className: 'table-cell-expand',
						label: Liferay.Language.get('training-date'),
						sortable: false
					},
					{
						accessor: 'activitiesData',
						className: 'table-column-text-end',
						dataFormatter: data =>
							get(
								data,
								['activities', 'total'],
								0
							).toLocaleString(),
						label: Liferay.Language.get('events'),
						sortable: false
					},
					{
						accessor: 'pageAssetsData',
						className: 'table-column-text-end',
						dataFormatter: data =>
							get(
								data,
								['pageAssets', 'total'],
								0
							).toLocaleString(),
						label: Liferay.Language.get('items'),
						sortable: false
					}
				]}
				items={[
					{
						activitiesData,
						pageAssetsData,
						trainingDate:
							!trainingDate || trainingFrequencyChanged
								? getDate()
								: trainingDate
					}
				]}
				rowIdentifier='trainingDate'
			/>
		</div>
	);
};

export default Summary;
