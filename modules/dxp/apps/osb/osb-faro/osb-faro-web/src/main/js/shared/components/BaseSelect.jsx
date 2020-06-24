import autobind from 'autobind-decorator';
import Button from './Button';
import debounce from '../util/debounce-decorator';
import getCN from 'classnames';
import Icon from './Icon';
import Input from './Input';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import Overlay from './Overlay';
import React from 'react';
import Spinner from './Spinner';
import {ARROW_DOWN, ARROW_UP, ENTER} from '../util/key-constants';
import {autoCancel, hasRequest} from 'shared/util/request-decorator';
import {hasChanges} from 'shared/util/react';
import {identity, noop} from 'lodash';
import {PropTypes} from 'prop-types';

const SELECT_KEYS = [ARROW_DOWN, ARROW_UP, ENTER];

export class Item extends React.Component {
	static propTypes = {
		item: PropTypes.any.isRequired,
		itemRenderer: PropTypes.func.isRequired,
		onSelect: PropTypes.func.isRequired
	};

	@autobind
	handleClick() {
		const {item, onSelect} = this.props;

		onSelect(item);
	}

	render() {
		const {className, item, itemRenderer, ...otherProps} = this.props;

		return (
			<li className={className}>
				<Button
					{...omitDefinedProps(otherProps, Item.propTypes)}
					className='dropdown-item text-truncate'
					display='unstyled'
					onClick={this.handleClick}
				>
					{itemRenderer(item)}
				</Button>
			</li>
		);
	}
}

@hasRequest
export default class BaseSelect extends React.Component {
	static defaultProps = {
		alwaysFetchOnFocus: false,
		disabled: false,
		emptyInputOnInactive: false,
		focusOnInit: false,
		inputValue: '',
		inset: false,
		onInputValueChange: noop,
		onSelect: noop,
		placeholder: ''
	};

	static propTypes = {
		alwaysFetchOnFocus: PropTypes.bool,
		dataSourceFn: PropTypes.func.isRequired,
		disabled: PropTypes.bool,
		emptyInputOnInactive: PropTypes.bool,
		focusOnInit: PropTypes.bool,
		id: PropTypes.string,
		inputSize: PropTypes.string,
		inputValue: PropTypes.string,
		inset: PropTypes.bool,
		itemRenderer: PropTypes.func,
		menuTitle: PropTypes.string,
		onBlur: PropTypes.func,
		onFocus: PropTypes.func,
		onInputValueChange: PropTypes.func,
		onSelect: PropTypes.func,
		placeholder: PropTypes.string,
		selectedItem: PropTypes.any
	};

	state = {
		active: false,
		focusIndex: 0,
		items: [],
		loading: false
	};

	constructor(props) {
		super(props);

		this._inputRef = React.createRef();
	}

	componentDidMount() {
		const {focusOnInit} = this.props;

		if (focusOnInit) {
			this._inputRef.current.focus();
		}
	}

	componentDidUpdate(prevProps) {
		if (
			hasChanges(prevProps, this.props, 'inputValue') &&
			this.state.active
		) {
			this.fetchItems();
		}
	}

	componentWillUnmount() {
		this.requestItems.cancel();
	}

	fetchItems() {
		this.setState({
			loading: true
		});

		this.requestItems();
	}

	@debounce(250)
	@autoCancel
	requestItems() {
		const {dataSourceFn, inputValue} = this.props;

		return dataSourceFn(inputValue)
			.then(items =>
				this.setState({
					items,
					loading: false
				})
			)
			.catch(err => {
				if (!err.IS_CANCELLATION_ERROR) {
					this.setState({
						items: [],
						loading: false
					});
				}
			});
	}

	@autobind
	handleBlur(event) {
		const {onBlur} = this.props;

		if (onBlur) {
			onBlur(event);
		}

		this.setState({
			loading: false
		});
	}

	@autobind
	handleFocus() {
		const {
			props: {alwaysFetchOnFocus, onFocus},
			state: {active, items}
		} = this;

		if (!active) {
			if (!items.length || alwaysFetchOnFocus) {
				this.fetchItems();
			}

			if (onFocus) {
				onFocus();
			}

			this.setState(
				{
					active: true,
					focusIndex: 0
				},
				() => this._inputRef.current.focus()
			);
		}
	}

	@autobind
	handleInput(event) {
		this.props.onInputValueChange(event.target.value);
	}

	@autobind
	handleKeyDown(event) {
		const {focusIndex} = this.state;
		const {keyCode} = event;

		if (!SELECT_KEYS.includes(keyCode)) {
			return;
		}

		event.preventDefault();

		switch (keyCode) {
			case ARROW_DOWN:
				this.setFocusIndex(focusIndex + 1);
				break;
			case ARROW_UP:
				this.setFocusIndex(focusIndex - 1);
				break;
			case ENTER:
				this.selectFocusedItem();
				break;
			default:
				break;
		}
	}

	@autobind
	handleSelect(item) {
		this.props.onSelect(item);

		this.handleOutsideClick();
	}

	@autobind
	handleOutsideClick() {
		this.setState({
			active: false,
			items: []
		});
	}

	selectFocusedItem() {
		const {focusIndex, items} = this.state;

		this.handleSelect(items[focusIndex]);

		this._inputRef.current.blur();
	}

	setFocusIndex(val) {
		const {length} = this.state.items;

		this.setState({focusIndex: (val + length) % length || 0});
	}

	render() {
		const {
			props: {
				className,
				containerClass,
				disabled,
				emptyInputOnInactive,
				id,
				inputSize,
				inputValue,
				inset,
				itemRenderer,
				menuTitle,
				placeholder,
				selectedItem,
				...otherProps
			},
			state: {active, focusIndex, items, loading}
		} = this;

		return (
			<Overlay
				{...omitDefinedProps(otherProps, BaseSelect.propTypes)}
				active={active}
				alignment='bottomLeft'
				containerClass={getCN('base-select-container', containerClass)}
				onOutsideClick={this.handleOutsideClick}
			>
				<Input.Group
					className={getCN(
						'base-select-input-root select-input-root',
						className,
						{inset}
					)}
					onClick={disabled ? null : this.handleFocus}
				>
					<Input.GroupItem>
						<Input
							autoComplete='nope'
							disabled={disabled}
							id={id}
							inset='after'
							onBlur={this.handleBlur}
							onChange={this.handleInput}
							onFocus={this.handleFocus}
							onKeyDown={this.handleKeyDown}
							placeholder={placeholder}
							ref={this._inputRef}
							size={inputSize}
							value={
								active || !emptyInputOnInactive
									? inputValue
									: ''
							}
						/>

						<Input.Inset position='after'>
							{loading ? (
								<Spinner size='sm' />
							) : (
								<Icon symbol='caret-bottom' />
							)}
						</Input.Inset>
					</Input.GroupItem>

					{!active && selectedItem && itemRenderer && (
						<div className='selected-item-container'>
							{itemRenderer(selectedItem)}
						</div>
					)}
				</Input.Group>

				{!!items.length && (
					<div className='dropdown-root'>
						<ul className='base-select-menu dropdown-menu show'>
							{!!menuTitle && (
								<>
									<li className='dropdown-header'>
										{menuTitle}
									</li>
								</>
							)}

							{items.map((item, i) => (
								<Item
									active={i === focusIndex}
									disabled={loading}
									item={item}
									itemRenderer={itemRenderer || identity}
									key={i}
									onSelect={this.handleSelect}
								/>
							))}
						</ul>
					</div>
				)}
			</Overlay>
		);
	}
}
