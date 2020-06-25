import autobind from 'autobind-decorator';
import AutocompleteInput from 'shared/components/AutocompleteInput';
import Form from 'shared/components/form';
import getCN from 'classnames';
import Input from 'shared/components/Input';
import React from 'react';
import {ClaySelectWithOption} from '@clayui/select';
import {getPropertyValue} from '../utils/custom-inputs';
import {ISegmentEditorCustomInputBase} from '../utils/types';
import {
	isKnown,
	isUnknown,
	RELATIONAL_OPERATORS,
	SUPPORTED_OPERATORS
} from '../utils/constants';
import {isOfKnownType, isValid} from '../utils/utils';
import {Map} from 'immutable';

const TEXT_OPERATORS = SUPPORTED_OPERATORS.text;

export interface ICustomStringInputProps extends ISegmentEditorCustomInputBase {
	autocomplete?: boolean;
	fieldValuesDataSourceFn?: () => Promise<string[]>;
	touched: boolean;
	valid: boolean;
}

export default class CustomStringInput extends React.Component<
	ICustomStringInputProps
> {
	static defaultProps = {
		autocomplete: true
	};

	getSelectedOperatorKey() {
		const criterionIMap = this.props.value.getIn(
			['criterionGroup', 'items', 0],
			Map()
		);

		const operatorName = criterionIMap.get('operatorName');
		const value = criterionIMap.get('value');

		let operatorKey = operatorName;

		const valueNull = value === null;

		if (operatorName === RELATIONAL_OPERATORS.EQ && valueNull) {
			operatorKey = isUnknown;
		} else if (operatorName === RELATIONAL_OPERATORS.NE && valueNull) {
			operatorKey = isKnown;
		}

		return TEXT_OPERATORS.find(({key}) => key === operatorKey).key;
	}

	@autobind
	handleBlur() {
		const {onChange, value: valueIMap} = this.props;

		onChange({
			touched: true,
			valid: isValid(getPropertyValue(valueIMap, 'value', 0)),
			value: valueIMap
		});
	}

	@autobind
	handleOperatorChange(event) {
		const {value: operator} = event.target;

		const {onChange, value: valueIMap} = this.props;

		let newVal = null;

		newVal = valueIMap.setIn(
			['criterionGroup', 'items', 0, 'operatorName'],
			TEXT_OPERATORS.find(({key}) => key === operator).name
		);

		if (isOfKnownType(operator)) {
			newVal = newVal.setIn(
				['criterionGroup', 'items', 0, 'value'],
				null
			);
		} else if (getPropertyValue(valueIMap, 'value', 0) === null) {
			newVal = newVal.setIn(['criterionGroup', 'items', 0, 'value'], '');
		}

		onChange({
			valid: isValid(
				newVal.getIn(['criterionGroup', 'items', 0, 'value'])
			),
			value: newVal
		});
	}

	@autobind
	handleValueChange(value) {
		const {onChange, value: valueIMap} = this.props;

		onChange({
			valid: isValid(value),
			value: valueIMap.setIn(
				['criterionGroup', 'items', 0, 'value'],
				value
			)
		});
	}

	render() {
		const {
			autocomplete,
			className,
			displayValue,
			fieldValuesDataSourceFn,
			property: {entityName, options = []},
			touched,
			valid,
			value: valueIMap
		} = this.props;

		const value = getPropertyValue(valueIMap, 'value', 0);

		const selectedOperatorKey = this.getSelectedOperatorKey();
		const knownType = isOfKnownType(selectedOperatorKey);

		const showError = !valid && touched;

		const sharedInputProps = {
			className: getCN(className, {
				'has-error': showError
			}),
			'data-testid': 'string-input',
			onBlur: this.handleBlur,
			value
		};

		return (
			<div className='criteria-statement'>
				<Form.Group autoFit>
					<Form.GroupItem className='entity-name' label shrink>
						{entityName}
					</Form.GroupItem>

					<Form.GroupItem className='display-value' label shrink>
						{displayValue}
					</Form.GroupItem>

					<Form.GroupItem shrink>
						<ClaySelectWithOption
							onChange={this.handleOperatorChange}
							options={TEXT_OPERATORS.map(({key, label}) => ({
								label,
								value: key
							}))}
							value={selectedOperatorKey}
						/>
					</Form.GroupItem>

					{!knownType && (
						<Form.GroupItem>
							{options.length === 0 ? (
								autocomplete ? (
									<AutocompleteInput
										{...sharedInputProps}
										dataSourceFn={fieldValuesDataSourceFn}
										onChange={this.handleValueChange}
									/>
								) : (
									<Input
										{...sharedInputProps}
										autoComplete='nope'
										onChange={event => {
											this.handleValueChange(
												event.target.value
											);
										}}
									/>
								)
							) : (
								<ClaySelectWithOption
									onBlur={this.handleBlur}
									onChange={event =>
										this.handleValueChange(
											event.target.value
										)
									}
									options={options.map(o => ({
										label: o.label,
										value: o.value
									}))}
									value={value}
								/>
							)}
						</Form.GroupItem>
					)}
				</Form.Group>
			</div>
		);
	}
}
