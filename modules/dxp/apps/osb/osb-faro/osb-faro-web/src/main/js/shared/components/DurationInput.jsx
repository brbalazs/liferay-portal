import autobind from 'autobind-decorator';
import Dropdown from 'shared/components/Dropdown';
import Input from 'shared/components/Input';
import React from 'react';
import {
	formatDuration,
	getLargestNaturalUnit,
	getMilliseconds,
	UNITS
} from 'shared/util/time';
import {isFinite} from 'lodash';
import {isValid} from 'contacts/components/segment-editor/dynamic/utils/utils';
import {PropTypes} from 'prop-types';

export default class DurationInput extends React.Component {
	static props = {
		onBlur: PropTypes.func,
		onChange: PropTypes.func,
		value: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
	};

	constructor(props) {
		super(props);

		const {value} = props;

		this.state = {
			unit: getLargestNaturalUnit(Number(value))
		};
	}

	getDisplayValue() {
		const {
			props: {value},
			state: {unit}
		} = this;

		if (isFinite(value)) {
			return formatDuration(value, unit);
		}

		return '';
	}

	@autobind
	handleChange(event) {
		const {value} = event.target;

		const {
			props: {onChange},
			state: {unit}
		} = this;

		let numberVal = '';

		if (isValid(value)) {
			numberVal = getMilliseconds(parseFloat(value), unit);
		}

		onChange(numberVal);
	}

	@autobind
	handleUnitClick(val) {
		this.setState({
			unit: val
		});
	}

	render() {
		const {
			props: {onBlur},
			state: {unit}
		} = this;

		return (
			<Input.Group>
				<Input
					onBlur={onBlur}
					onChange={this.handleChange}
					type='number'
					value={this.getDisplayValue()}
				/>

				<Input.GroupItem position='append'>
					<Dropdown label={UNITS[unit]}>
						{UNITS.map((unitLabel, i) => (
							<Dropdown.Item
								active={i === unit}
								hideOnClick
								key={i}
								onClick={() => this.handleUnitClick(i)}
							>
								{unitLabel}
							</Dropdown.Item>
						))}
					</Dropdown>
				</Input.GroupItem>
			</Input.Group>
		);
	}
}
