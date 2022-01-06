import autobind from 'autobind-decorator';
import getCN from 'classnames';
import Icon from './Icon';
import Label from 'shared/components/Label';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {BACKSPACE, COMMA, ENTER, SPACE} from 'shared/util/key-constants';
import {noop, partition} from 'lodash';
import {PropTypes} from 'prop-types';

const KEYS = [COMMA, ENTER, SPACE];

class InputList extends React.Component {
	static defaultProps = {
		disabled: false,
		errorMessage: Liferay.Language.get('error'),
		inputValue: '',
		items: [],
		keyCodesToSplit: KEYS,
		onInputChange: noop,
		onItemsChange: noop,
		onValidationFail: noop,
		placeholder: '',
		validateOnBlur: false,
		validationFn: () => true
	};

	static propTypes = {
		className: PropTypes.string,
		disabled: PropTypes.bool,
		errorMessage: PropTypes.string,
		inputValue: PropTypes.string,
		items: PropTypes.array,
		keyCodesToSplit: PropTypes.array,
		onInputChange: PropTypes.func,
		onItemsChange: PropTypes.func,
		onValidationFail: PropTypes.func,
		placeholder: PropTypes.string,
		validateOnBlur: PropTypes.bool,
		validationFn: PropTypes.func
	};

	state = {
		focused: false,
		valid: true
	};

	getCharCode(keyCode) {
		if (keyCode >= 96) {
			return keyCode - 48 * Math.floor(keyCode / 48);
		}

		return keyCode;
	}

	getStringFromKeyCode(keycode) {
		return String.fromCharCode(this.getCharCode(keycode));
	}

	@autobind
	handleBlur(event) {
		const {
			items,
			onInputChange,
			onItemsChange,
			onValidationFail,
			validateOnBlur,
			validationFn
		} = this.props;

		this.setState({
			focused: false
		});

		if (event.target.value && validateOnBlur) {
			if (validationFn(event.target.value)) {
				onItemsChange([...items, event.target.value]);

				event.target.value = '';

				onInputChange('');
			} else {
				this.setState({
					valid: false
				});

				onValidationFail();
			}
		}
	}

	@autobind
	handleFocus() {
		this.setState({
			focused: true
		});
	}

	@autobind
	handleKeyDown(event) {
		const {
			keyCode,
			target: {value}
		} = event;

		const {
			items,
			keyCodesToSplit,
			onInputChange,
			onItemsChange,
			onValidationFail,
			validationFn
		} = this.props;

		if (value && keyCodesToSplit.includes(keyCode)) {
			event.preventDefault();

			if (validationFn(value)) {
				onItemsChange([...items, value]);

				onInputChange('');
			} else {
				this.setState({
					valid: false
				});

				onValidationFail();
			}
		} else if (!value && keyCode === BACKSPACE && items.length) {
			event.preventDefault();

			onItemsChange(items.splice(0, items.length - 1));
		} else {
			this.setState({
				valid: true
			});
		}
	}

	@autobind
	handleInputChange({target: {value}}) {
		this.props.onInputChange(value);
	}

	@autobind
	handleRemoveItem(index) {
		const {items, onItemsChange} = this.props;

		onItemsChange([...items.slice(0, index), ...items.slice(index + 1)]);
	}

	@autobind
	handlePaste(event) {
		const pastedText = event.clipboardData.getData('Text');

		const {keyCodesToSplit} = this.props;

		const keysToSplit = keyCodesToSplit.map(keycode =>
			this.getStringFromKeyCode(keycode)
		);

		const pastedItems = pastedText
			.split(new RegExp(`\\n|\\t|[${keysToSplit.join('')}]`))
			.filter(item => item.length);

		if (pastedItems.length) {
			event.preventDefault();

			const {
				items,
				onInputChange,
				onItemsChange,
				onValidationFail,
				validationFn
			} = this.props;

			const [valid, invalid] = partition(
				pastedItems,
				item => item && validationFn(item)
			);

			if (valid.length) {
				onItemsChange([...items, ...valid]);
			}

			if (invalid.length) {
				onInputChange(invalid.join(''));

				this.setState({
					valid: false
				});

				onValidationFail();
			}
		}
	}

	render() {
		const {
			props: {
				className,
				disabled,
				errorMessage,
				inputValue,
				items,
				name,
				placeholder,
				...otherProps
			},
			state: {focused, valid}
		} = this;

		const classes = getCN('input-group-item input-list-root', className, {
			disabled,
			focus: focused && valid,
			['has-error']: !valid
		});

		return (
			<div className={classes}>
				<div className='form-control form-control-tag-group'>
					{items &&
						items.length > 0 &&
						items.map((item, i) => (
							<Label
								display='secondary'
								index={i}
								key={i}
								onRemove={
									disabled ? undefined : this.handleRemoveItem
								}
							>
								{item}
							</Label>
						))}

					<input
						{...omitDefinedProps(otherProps, InputList.propTypes)}
						className='form-control-inset'
						disabled={disabled}
						name={name}
						onBlur={this.handleBlur}
						onChange={this.handleInputChange}
						onFocus={this.handleFocus}
						onKeyDown={this.handleKeyDown}
						onPaste={this.handlePaste}
						placeholder={placeholder}
						type='text'
						value={inputValue}
					/>
				</div>

				{!valid && errorMessage && (
					<div className='form-feedback-group'>
						<div className='form-feedback-item'>
							<span className='form-feedback-indicator'>
								<Icon size='sm' symbol='info-circle' />
							</span>

							{errorMessage}
						</div>
					</div>
				)}
			</div>
		);
	}
}

export default InputList;
