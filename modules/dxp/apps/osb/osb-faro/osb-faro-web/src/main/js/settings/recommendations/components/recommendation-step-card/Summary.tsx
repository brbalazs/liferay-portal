import Constants from 'shared/util/constants';
import getCN from 'classnames';
import Icon from 'shared/components/Icon';
import React from 'react';
import RecommendationActivitiesQuery from '../../queries/RecommendationActivitiesQuery';
import RecommendationPageAssetsQuery from '../../queries/RecommendationPageAssetsQuery';
import {
	Filter,
	getPropertiesFromItems,
	JOB_TRAINING_FREQUENCIES_LABEL_MAP,
	JOB_TRAINING_PERIODS_LABEL_MAP,
	JOB_TRAINING_PERIODS_RANGE_KEY_MAP,
	JOB_TYPES_LABEL_MAP,
	JobProperty
} from '../../utils/utils';
import {get} from 'lodash';
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
	includePreviousPeriod: boolean;
	itemFilters: Filter[];
	name: string;
	trainingFrequency: jobTrainingFrequencies;
	trainingPeriod: jobTrainingPeriods;
	type: jobTypes;
}

const Summary: React.FC<ISummaryProps> = ({
	includePreviousPeriod,
	itemFilters,
	name,
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
			rangeKey: JOB_TRAINING_PERIODS_RANGE_KEY_MAP[trainingPeriod],
			size: 0,
			start: 0
		}
	});

	const {data: activitiesDataWithPrevious} = useQuery(
		RecommendationActivitiesQuery,
		{
			variables: {
				applicationId: 'Page',
				eventContextPropertyFilters: propertyFilters,
				eventId: 'pageUnloaded',
				rangeKey:
					Number(JOB_TRAINING_PERIODS_RANGE_KEY_MAP[trainingPeriod]) *
					2,
				size: 0,
				start: 0
			}
		}
	);

	const activitiesTotal: number = get(
		activitiesData,
		['activities', 'total'],
		0
	);

	const activitiesWithPreviousTotal: number = get(
		activitiesDataWithPrevious,
		['activities', 'total'],
		0
	);

	const notEnoughActivities: boolean = activitiesTotal < 1000;
	const notEnoughActivitiesWithPrevious: boolean =
		activitiesWithPreviousTotal < 1000;

	return (
		<div className='summary-root'>
			<div className='title'>{Liferay.Language.get('summary')}</div>

			<table className='summary-table table table-autofit table-nowrap table-row-no-bordered'>
				<tbody>
					<tr>
						<td className='summary-name table-cell-expand'>
							{Liferay.Language.get('name')}
						</td>

						<td className='summary-value'>{name}</td>
					</tr>

					<tr>
						<td className='summary-name table-cell-expand'>
							{Liferay.Language.get('model-type')}
						</td>

						<td className='summary-value'>
							{JOB_TYPES_LABEL_MAP[type]}
						</td>
					</tr>

					<tr>
						<td className='summary-name table-cell-expand'>
							{Liferay.Language.get('training-frequency')}
						</td>

						<td className='summary-value'>
							{
								JOB_TRAINING_FREQUENCIES_LABEL_MAP[
									trainingFrequency
								]
							}
						</td>
					</tr>

					<tr>
						<td className='summary-name table-cell-expand'>
							{Liferay.Language.get('training-period')}
						</td>

						<td className='summary-value'>
							{JOB_TRAINING_PERIODS_LABEL_MAP[trainingPeriod]}
						</td>
					</tr>

					<tr
						className={getCN({
							'insufficient-events': notEnoughActivities
						})}
					>
						<td className='summary-name table-cell-expand'>
							{notEnoughActivities && (
								<Icon symbol='warning-full' />
							)}

							{`${Liferay.Language.get(
								'events'
							)} (${Liferay.Language.get('as-of-today')})`}
						</td>

						<td className='summary-value'>
							{activitiesTotal.toLocaleString()}
						</td>
					</tr>

					{includePreviousPeriod && (
						<tr
							className={getCN({
								'insufficient-events': notEnoughActivitiesWithPrevious
							})}
						>
							<td className='summary-name table-cell-expand including-previous-period'>
								{notEnoughActivitiesWithPrevious && (
									<Icon symbol='warning-full' />
								)}

								{`${Liferay.Language.get(
									'events'
								)} (${Liferay.Language.get(
									'including-previous-period'
								)})`}
							</td>

							<td className='summary-value'>
								{activitiesWithPreviousTotal.toLocaleString()}
							</td>
						</tr>
					)}

					<tr>
						<td className='summary-name table-cell-expand'>
							{`${Liferay.Language.get(
								'items'
							)} (${Liferay.Language.get('as-of-today')})`}
						</td>

						<td className='summary-value'>
							{get(
								pageAssetsData,
								['pageAssets', 'total'],
								0
							).toLocaleString()}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	);
};

export default Summary;
