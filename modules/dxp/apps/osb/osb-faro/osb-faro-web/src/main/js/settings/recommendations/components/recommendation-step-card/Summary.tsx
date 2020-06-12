import React from 'react';
import Table from 'shared/components/table';
import {DateCell} from 'shared/components/table/cell-components';
import {getDate} from 'shared/util/date';
import {
	JOB_TRAINING_FREQUENCIES_LABEL_MAP,
	JOB_TRAINING_PERIODS_LABEL_MAP,
	JOB_TYPES_LABEL_MAP
} from '../../utils/utils';
import {
	jobTrainingFrequencies,
	jobTrainingPeriods,
	jobTypes
} from 'shared/util/constants';

interface ISummaryProps {
	initialValues: any;
	name: string;
	trainingDate: string;
	trainingFrequency: jobTrainingFrequencies;
	trainingPeriod: jobTrainingPeriods;
	type: jobTypes;
}

const Summary: React.FC<ISummaryProps> = ({
	initialValues,
	name,
	trainingDate,
	trainingFrequency,
	trainingPeriod,
	type
}) => {
	const trainingFrequencyChanged =
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
						label: Liferay.Language.get('training-date')
					},
					{
						accessor: 'eventsCount',
						className: 'table-column-text-end',
						dataFormatter: data => data.toLocaleString(),
						label: Liferay.Language.get('events')
					},
					{
						accessor: 'itemsCount',
						className: 'table-column-text-end',
						dataFormatter: data => data.toLocaleString(),
						label: Liferay.Language.get('items')
					}
				]}
				items={[
					{
						eventsCount: 321, // TODO: LRAC-5936 replace with actual count provided to form in step 3 modal
						itemsCount: 123, // TODO: LRAC-5936 replace with count
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
