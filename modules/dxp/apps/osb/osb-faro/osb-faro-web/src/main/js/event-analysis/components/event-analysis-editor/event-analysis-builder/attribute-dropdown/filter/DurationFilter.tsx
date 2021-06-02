import Button from 'shared/components/Button';
import Form, {validateRequired} from 'shared/components/form';
import React from 'react';
import {
	createDurationBreakdown,
	DURATION_OPERATOR_LONGHAND_LABELS_MAP,
	DURATION_OPTIONS
} from 'event-analysis/utils/utils';
import {formatTime, getMillisecondsFromTime} from 'shared/util/time';
import {IFilterProps, Operators} from 'event-analysis/utils/types';
import {sub} from 'shared/util/lang';

const DEFAULT_DURATION_BIN = 60000;
const DURATION_MASK = [/\d/, /\d/, ':', /[0-6]/, /\d/, ':', /[0-6]/, /\d/];

const DurationFilter: React.FC<IFilterProps> = ({
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
				value: [value]
			} = filter;

			return {
				bin: formatTime(bin),
				operator,
				value: formatTime(value as number)
			};
		}

		return {
			bin: formatTime(DEFAULT_DURATION_BIN),
			operator: Operators.GT,
			value: ''
		};
	};

	return (
		<Form
			initialValues={getInitialValues()}
			onSubmit={({bin, operator, value}) => {
				onFilterSubmit({
					breakdown: createDurationBreakdown({
						attributeId,
						bin: getMillisecondsFromTime(
							bin.replace(/_/g, '0') as string
						),
						type: attributeOwnerType
					}),
					filter: {
						attributeId,
						operator,
						value: [
							getMillisecondsFromTime(value.replace(/_/g, '0'))
						]
					}
				});
			}}
		>
			{({handleSubmit, isValid}) => (
				<Form.Form onSubmit={handleSubmit}>
					<div className='filter-body'>
						<Form.Group autoFit>
							<Form.GroupItem>
								<Form.Input
									label={sub(
										Liferay.Language.get('group-x-by'),
										[Liferay.Language.get('duration')]
									)}
									mask={DURATION_MASK}
									name='bin'
									placeholder='HH:MM:SS'
									type='string'
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
									{DURATION_OPTIONS.map(value => (
										<Form.Select.Item
											key={value}
											value={value}
										>
											{
												DURATION_OPERATOR_LONGHAND_LABELS_MAP[
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
									autoComplete='off'
									mask={DURATION_MASK}
									name='value'
									placeholder='HH:MM:SS'
									required
									type='string'
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

export default DurationFilter;
