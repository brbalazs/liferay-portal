import Button from 'shared/components/Button';
import Form, {validateRequired} from 'shared/components/form';
import React from 'react';
import {createNumberBreakdown} from 'event-analysis/utils/utils';
import {IBreakdownProps} from 'event-analysis/utils/types';

const DEFAULT_NUMBER_BIN = 10;

const NumberBreakdown: React.FC<IBreakdownProps> = ({
	attributeId,
	attributeOwnerType,
	breakdown,
	onSubmit
}) => {
	const getInitialValues = () => {
		if (breakdown) {
			const {bin} = breakdown;

			return {bin};
		}

		return {
			bin: DEFAULT_NUMBER_BIN
		};
	};

	return (
		<Form
			enableReinitialize
			initialValues={getInitialValues()}
			isInitialValid
			onSubmit={({bin}) => {
				onSubmit(
					createNumberBreakdown({
						attributeId,
						attributeType: attributeOwnerType,
						bin
					})
				);
			}}
		>
			{({handleSubmit, isValid}) => (
				<Form.Form onSubmit={handleSubmit}>
					<div className='options-body'>
						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Input
									label={Liferay.Language.get(
										'group-numbers-by'
									)}
									name='bin'
									type='number'
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

export default NumberBreakdown;
