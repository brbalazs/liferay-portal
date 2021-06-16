import Button from 'shared/components/Button';
import Form, {validateRequired} from 'shared/components/form';
import React from 'react';
import {
	createNumberBreakdown,
	NUMBER_OPERATOR_LONGHAND_LABELS_MAP,
	NUMBER_OPTIONS
} from 'event-analysis/utils/utils';
import {IFilterProps, Operators} from 'event-analysis/utils/types';
import {sub} from 'shared/util/lang';

const DEFAULT_NUMBER_BIN = 10;

const NumberFilter: React.FC<IFilterProps> = ({
	attributeId,
	attributeOwnerType,
	breakdown,
	filter,
	onFilterSubmit
}) => {
	const getInitialValues = () => {
		if (breakdown && filter) {
			const {bin} = breakdown;
			const {
				operator,
				value: [startValue = '', endValue = '']
			} = filter;

			return {bin, endValue, operator, startValue};
		}

		return {
			bin: DEFAULT_NUMBER_BIN,
			endValue: '',
			operator: Operators.GT,
			startValue: ''
		};
	};

	return (
		<Form
			initialValues={getInitialValues()}
			onSubmit={({bin, endValue, operator, startValue}) => {
				let value = [startValue];

				if (operator === Operators.Between) {
					value = [...value, endValue];
				}

				onFilterSubmit({
					breakdown: createNumberBreakdown({
						attributeId,
						bin,
						type: attributeOwnerType
					}),
					filter: {
						attributeId,
						operator,
						value
					}
				});
			}}
		>
			{({handleSubmit, isValid, values: {operator}}) => (
				<Form.Form onSubmit={handleSubmit}>
					<div className='filter-body'>
						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Input
									label={sub(
										Liferay.Language.get('group-x-by'),
										[Liferay.Language.get('numbers')]
									)}
									name='bin'
									type='number'
									validate={validateRequired}
								/>
							</Form.GroupItem>
						</Form.Group>

						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Select
									label={Liferay.Language.get('condition')}
									name='operator'
								>
									{NUMBER_OPTIONS.map(value => (
										<Form.Select.Item
											key={value}
											value={value}
										>
											{
												NUMBER_OPERATOR_LONGHAND_LABELS_MAP[
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
								<Form.Input
									name='startValue'
									required
									type='number'
									validate={validateRequired}
								/>
							</Form.GroupItem>

							{operator === Operators.Between && (
								<Form.GroupItem>
									<Form.Input
										name='endValue'
										required
										type='number'
										validate={validateRequired}
									/>
								</Form.GroupItem>
							)}
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
			)}
		</Form>
	);
};

export default NumberFilter;
