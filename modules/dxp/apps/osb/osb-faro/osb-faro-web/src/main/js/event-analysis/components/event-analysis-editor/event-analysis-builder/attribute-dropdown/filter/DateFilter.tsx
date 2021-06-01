import Button from 'shared/components/Button';
import Form, {
	validateDateRangeRequired,
	validateRequired
} from 'shared/components/form';
import React from 'react';
import {
	createDateBreakdown,
	DATE_GROUPING_LABELS_MAP,
	DATE_GROUPING_OPTIONS,
	DATE_OPERATOR_LONGHAND_LABELS_MAP,
	DATE_OPTIONS
} from 'event-analysis/utils/utils';
import {
	DateGroupings,
	IFilterProps,
	Operators
} from 'event-analysis/utils/types';
import {sub} from 'shared/util/lang';

const DateFilter: React.FC<IFilterProps> = ({
	attributeId,
	attributeType,
	breakdown,
	filter,
	onFilterSubmit
}) => {
	const getInitialValues = () => {
		if (breakdown && filter) {
			const {dateGrouping} = breakdown;
			const {
				operator,
				value: [start, end]
			} = filter;

			return {
				date: operator === Operators.Between ? '' : start,
				dateGrouping,
				dateRange:
					operator === Operators.Between
						? {end, start}
						: {end: '', start: ''},
				operator
			};
		}

		return {
			date: '',
			dateGrouping: DateGroupings.Months,
			dateRange: {end: '', start: ''},
			operator: Operators.EQ
		};
	};

	return (
		<Form
			initialValues={getInitialValues()}
			onSubmit={({date, dateGrouping, dateRange, operator}) => {
				let dateValue: string[] = [date as string];

				if (operator === Operators.Between) {
					const {end, start} = dateRange;

					dateValue = [start as string, end as string];
				}

				onFilterSubmit({
					breakdown: createDateBreakdown({
						attributeId,
						dateGrouping,
						type: attributeType
					}),
					filter: {
						attributeId,
						operator,
						value: dateValue
					}
				});
			}}
		>
			{({
				handleSubmit,
				isValid,
				setFieldValue,
				values: {dateGrouping, operator}
			}) => {
				const filteredConditions =
					dateGrouping === DateGroupings.Dates
						? DATE_OPTIONS
						: DATE_OPTIONS.filter(
								option => option !== Operators.Between
						  );

				return (
					<Form.Form onSubmit={handleSubmit}>
						<div className='filter-body'>
							<Form.Group autoFit>
								<Form.GroupItem>
									<Form.Select
										label={sub(
											Liferay.Language.get('group-x-by'),
											[Liferay.Language.get('dates')]
										)}
										name='dateGrouping'
										onChange={event => {
											if (
												operator ===
													Operators.Between &&
												event.target.value !==
													DateGroupings.Dates
											) {
												setFieldValue(
													'operator',
													Operators.EQ
												);
											}
										}}
										type='string'
										validate={validateRequired}
									>
										{DATE_GROUPING_OPTIONS.map(value => (
											<Form.Select.Item
												key={value}
												value={value}
											>
												{
													DATE_GROUPING_LABELS_MAP[
														value
													]
												}
											</Form.Select.Item>
										))}
									</Form.Select>
								</Form.GroupItem>
							</Form.Group>

							<Form.Group autoFit>
								<Form.GroupItem>
									<Form.Select
										label={Liferay.Language.get(
											'condition'
										)}
										name='operator'
									>
										{filteredConditions.map(value => (
											<Form.Select.Item
												key={value}
												value={value}
											>
												{
													DATE_OPERATOR_LONGHAND_LABELS_MAP[
														value
													]
												}
											</Form.Select.Item>
										))}
									</Form.Select>
								</Form.GroupItem>
							</Form.Group>

							<Form.Group autoFit>
								<Form.GroupItem>
									{operator !== Operators.Between && (
										<Form.DateInput
											name='date'
											overlayAlignment='rightCenter'
											usePortal={false}
											validate={validateRequired}
										/>
									)}

									{operator === Operators.Between && (
										<Form.DateRangeInput
											name='dateRange'
											overlayAlignment='rightCenter'
											usePortal={false}
											validate={validateDateRangeRequired}
										/>
									)}
								</Form.GroupItem>
							</Form.Group>
						</div>

						<div className='filter-footer'>
							<Button
								block
								disabled={!isValid}
								display='primary'
								type='submit'
							>
								{Liferay.Language.get('done')}
							</Button>
						</div>
					</Form.Form>
				);
			}}
		</Form>
	);
};

export default DateFilter;
