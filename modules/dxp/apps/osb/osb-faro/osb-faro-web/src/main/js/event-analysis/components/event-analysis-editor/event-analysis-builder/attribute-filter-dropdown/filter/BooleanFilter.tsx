import Button from 'shared/components/Button';
import Form from 'shared/components/form';
import React from 'react';
import {
	BOOLEAN_LABELS_MAP,
	BOOLEAN_OPTIONS,
	createBooleanBreakdown
} from 'event-analysis/utils/utils';
import {IFilterProps, Operators} from 'event-analysis/utils/types';

const BooleanFilter: React.FC<IFilterProps> = ({
	attributeId,
	attributeOwnerType,
	breakdown,
	filter,
	onFilterSubmit
}) => {
	const getInitialValues = () => {
		if (breakdown && filter) {
			const {operator, value} = filter;

			return {operator, value: String(value[0])};
		}

		return {operator: Operators.EQ, value: 'true'};
	};

	return (
		<Form
			initialValues={getInitialValues()}
			onSubmit={({operator, value}) => {
				onFilterSubmit({
					breakdown: createBooleanBreakdown({
						attributeId,
						type: attributeOwnerType
					}),
					filter: {attributeId, operator, value: [value === 'true']}
				});
			}}
		>
			{({handleSubmit}) => (
				<Form.Form onSubmit={handleSubmit}>
					<div className='filter-body'>
						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Select
									label={Liferay.Language.get('condition')}
									name='value'
								>
									{BOOLEAN_OPTIONS.map(value => (
										<Form.Select.Item
											key={value}
											value={value}
										>
											{BOOLEAN_LABELS_MAP[value]}
										</Form.Select.Item>
									))}
								</Form.Select>
							</Form.GroupItem>
						</Form.Group>
					</div>

					<div className='filter-footer'>
						<Button block display='primary' type='submit'>
							{Liferay.Language.get('done')}
						</Button>
					</div>
				</Form.Form>
			)}
		</Form>
	);
};

export default BooleanFilter;
