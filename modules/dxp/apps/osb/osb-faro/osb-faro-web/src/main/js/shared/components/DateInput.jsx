import autobind from 'autobind-decorator';
import Button from './Button';
import Card from './Card';
import DatePicker from './date-picker';
import getCN from 'classnames';
import Icon from './Icon';
import Input from './Input';
import MaskedInput from './MaskedInput';
import moment from 'moment';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Overlay from './Overlay';
import React from 'react';
import {DATE_MASK, DATE_TIME_MASK, FORMAT} from 'shared/util/date';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

class DateInput extends React.Component {
	static defaultProps = {
		format: FORMAT,
		onBlur: noop,
		onChange: noop,
		showTimeSelector: false
	};

	static propTypes = {
		className: PropTypes.string,
		displayFormat: PropTypes.string,
		format: PropTypes.string,
		onBlur: PropTypes.func,
		onChange: PropTypes.func,
		showTimeSelector: PropTypes.bool,
		value: PropTypes.string
	};

	state = {
		active: false
	};

	@autobind
	handleFocus(event) {
		event.target.blur();
	}

	@autobind
	handleKeydown(event) {
		event.preventDefault();
	}

	@autobind
	handleClick(event) {
		event.preventDefault();

		this.setState({
			active: true
		});
	}

	@autobind
	handleDateSelect(value) {
		const {format, onChange} = this.props;

		onChange(value.format(format));
	}

	@autobind
	handleChange(event) {
		const {format, onChange} = this.props;
		const {value} = event.target;

		if (moment(value, format, true).isValid()) {
			onChange(value);
		}
	}

	@autobind
	handleOutsideClick() {
		const {
			props: {onBlur},
			state: {active}
		} = this;

		if (onBlur && active) {
			onBlur();
		}

		this.setState({
			active: false
		});
	}

	render() {
		const {
			props: {
				className,
				displayFormat,
				format,
				showTimeSelector,
				value,
				...otherProps
			},
			state: {active}
		} = this;

		const date = moment(value, format);

		return (
			<Overlay
				active={active}
				className={getCN('date-input-root', className)}
				forceAlignment={false}
				onOutsideClick={this.handleOutsideClick}
			>
				<Input.Group>
					<Input.GroupItem>
						<MaskedInput
							{...omitDefinedProps(
								otherProps,
								DateInput.propTypes
							)}
							data-testid='date-input'
							inset='after'
							keepCharPositions
							mask={showTimeSelector ? DATE_TIME_MASK : DATE_MASK}
							onChange={this.handleChange}
							onClick={this.handleClick}
							placeholder={
								showTimeSelector
									? Liferay.Language.get(
											'yyyy-mm-dd-hh-mm-zz'
									  )
									: Liferay.Language.get('yyyy-mm-dd')
							}
							showMask
							value={
								displayFormat
									? moment(value).format(displayFormat)
									: value
							}
						/>

						<Input.Inset position='after'>
							<Button
								display='unstyled'
								onClick={this.handleClick}
							>
								<Icon symbol='calendar' />
							</Button>
						</Input.Inset>
					</Input.GroupItem>
				</Input.Group>

				<Card>
					<Card.Body>
						<DatePicker
							date={date.isValid() ? date : null}
							minDate={moment().subtract(100, 'years')}
							onSelect={this.handleDateSelect}
							showTimeSelector={showTimeSelector}
						/>
					</Card.Body>
				</Card>
			</Overlay>
		);
	}
}

export default DateInput;
