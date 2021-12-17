import Button from 'shared/components/Button';
import Form, {validateRequired} from 'shared/components/form';
import React from 'react';
import {DataTypes, IFilterProps, Operators} from 'event-analysis/utils/types';
import {
	STRING_OPERATOR_LABELS_MAP,
	STRING_OPTIONS
} from 'event-analysis/utils/utils';

const StringFilter: React.FC<IFilterProps> = ({
	attributeId,
	attributeOwnerType,
	filter,
	onSubmit
}) => {
	const getInitialValues = () => {
		if (filter) {
			const {operator, values} = filter;

			return {operator, value: values[0]};
		}

		return {operator: Operators.Contains, value: ''};
	};

	return (
		<Form
			enableReinitialize
			initialValues={getInitialValues()}
			onSubmit={({operator, value}) => {
				onSubmit({
					attributeId,
					attributeType: attributeOwnerType,
					dataType: DataTypes.String,
					operator,
					values: [value]
				});
			}}
		>
			{({handleSubmit, isValid}) => (
				<Form.Form
					onSubmit={event => {
						event.stopPropagation();

						handleSubmit(event);
					}}
				>
					<div className='options-body'>
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

					<div className='options-footer'>
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
