import Button from 'shared/components/Button';
import Form, {validateRequired} from 'shared/components/form';
import React from 'react';
import {
	createStringBreakdown,
	STRING_OPERATOR_LABELS_MAP,
	STRING_OPTIONS
} from 'event-analysis/utils/utils';
import {IFilterProps, Operators} from 'event-analysis/utils/types';

const StringFilter: React.FC<IFilterProps> = ({
	attributeId,
	attributeOwnerType,
	breakdown,
	filter,
	onFilterSubmit
}) => {
	const getInitialValues = () => {
		if (breakdown && filter) {
			const {operator, value} = filter;

			return {operator, value: value[0]};
		}

		return {operator: Operators.Contains, value: ''};
	};

	return (
		<Form
			initialValues={getInitialValues()}
			onSubmit={({operator, value}) => {
				onFilterSubmit({
					breakdown: createStringBreakdown({
						attributeId,
						type: attributeOwnerType
					}),
					filter: {attributeId, operator, value: [value]}
				});
			}}
		>
			{({handleSubmit, isValid}) => (
				<Form.Form onSubmit={handleSubmit}>
					<div className='filter-body'>
						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Select
									label={Liferay.Language.get('condition')}
									name='operator'
								>
									{STRING_OPTIONS.map(value => (
										<Form.Select.Item
											key={value}
											value={value}
										>
											{STRING_OPERATOR_LABELS_MAP[value]}
										</Form.Select.Item>
									))}
								</Form.Select>
							</Form.GroupItem>
						</Form.Group>

						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Input
									name='value'
									required
									type='text'
									validate={validateRequired}
								/>
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
			)}
		</Form>
	);
};

export default StringFilter;
