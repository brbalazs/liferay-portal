import DatePickerInput from './DatePickerInput';
import Form from 'shared/components/form';
import React, {useState} from 'react';
import TimePeriodInput from './TimePeriodInput';
import {ClaySelectWithOption} from '@clayui/select';
import {Criterion} from '../../utils/types';
import {
	EVER,
	FUNCTIONAL_OPERATORS,
	RelationalOperators,
	SINCE,
	TIME_CONJUNCTION_OPTIONS,
	TIME_PERIOD_OPTIONS,
	TimeSpans
} from '../../utils/constants';
import {Map} from 'immutable';

const {BETWEEN} = FUNCTIONAL_OPERATORS;
const {EQ, GT, LT} = RelationalOperators;

const TIME_PERIOD_VALUES = TIME_PERIOD_OPTIONS.map(({value}) => value);

export const getInitialConjunction = (
	conjunctionCriterion: Criterion
): string => {
	const {operatorName, value} = conjunctionCriterion;

	if (operatorName === GT && TIME_PERIOD_VALUES.includes(value)) {
		return SINCE;
	} else if (!operatorName) {
		return EVER;
	}

	return operatorName;
};

interface IDateFilterConjunctionInputProps {
	conjunctionCriterion: Criterion & {
		touched: boolean;
		valid: boolean;
	};
	onChange: (conjunctionCriterion: Criterion) => void;
}

const DateFilterConjunctionInput: React.FC<IDateFilterConjunctionInputProps> = ({
	conjunctionCriterion,
	onChange
}) => {
	const [conjunction, setConjunction] = useState(
		getInitialConjunction(conjunctionCriterion)
	);

	const handleConjunctionChange = event => {
		const {value} = event.target;

		const {propertyName, value: dateFilter} = conjunctionCriterion;

		switch (value) {
			case SINCE:
				onChange({
					operatorName: GT,
					propertyName,
					touched: false,
					valid: true,
					value: TimeSpans.Last24Hours
				});
				break;
			case BETWEEN:
				onChange({
					operatorName: BETWEEN,
					propertyName,
					touched: false,
					valid: false,
					value: Map({end: '', start: ''})
				});
				break;
			case EVER:
				onChange(null);
				break;
			default:
				onChange({
					operatorName: value,
					propertyName,
					touched: false,
					valid: ![SINCE, BETWEEN, EVER].includes(conjunction),
					value: [SINCE, BETWEEN, EVER].includes(conjunction)
						? ''
						: dateFilter
				});
				break;
		}

		setConjunction(value);
	};

	const handleDateFilterBlur = () => {
		onChange({
			...conjunctionCriterion,
			touched: true
		});
	};

	const handleDateFilterChange = dateFilter => {
		const {operatorName, propertyName} = conjunctionCriterion;

		onChange({
			operatorName,
			propertyName,
			touched: true,
			valid:
				operatorName === BETWEEN
					? !!dateFilter.end && !!dateFilter.start
					: !!dateFilter,
			value: dateFilter
		});
	};

	const {touched, valid, value} = conjunctionCriterion;

	const showDatePicker = [BETWEEN, EQ, GT, LT].includes(conjunction);
	const showTimePeriod = conjunction === SINCE;

	return (
		<>
			<Form.GroupItem shrink>
				<ClaySelectWithOption
					className='conjunction-input'
					data-testid='conjunction-input'
					onChange={handleConjunctionChange}
					options={TIME_CONJUNCTION_OPTIONS}
					value={conjunction}
				/>
			</Form.GroupItem>

			<Form.GroupItem shrink>
				{showTimePeriod && (
					<TimePeriodInput
						onChange={handleDateFilterChange}
						value={value}
					/>
				)}

				{showDatePicker && (
					<DatePickerInput
						isRange={conjunction === BETWEEN}
						onBlur={handleDateFilterBlur}
						onChange={handleDateFilterChange}
						touched={touched}
						valid={valid}
						value={value}
					/>
				)}
			</Form.GroupItem>
		</>
	);
};

export default DateFilterConjunctionInput;
