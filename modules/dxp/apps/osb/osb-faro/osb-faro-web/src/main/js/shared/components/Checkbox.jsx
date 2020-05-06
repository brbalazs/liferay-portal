import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class Checkbox extends React.Component {
	static propTypes = {
		checked: PropTypes.bool,
		className: PropTypes.string,
		displayInline: PropTypes.bool,
		indeterminate: PropTypes.bool,
		label: PropTypes.node,
		name: PropTypes.string,
		onChange: PropTypes.func.isRequired
	};

	constructor(props) {
		super(props);

		this._checkboxRef = React.createRef();
	}

	componentDidMount() {
		this._checkboxRef.current.indeterminate = this.props.indeterminate;
	}

	componentDidUpdate() {
		this._checkboxRef.current.indeterminate = this.props.indeterminate;
	}

	handleEventPropagation(event) {
		event.stopPropagation();
	}

	render() {
		const {
			checked,
			className,
			displayInline,
			label,
			name,
			onChange,
			...otherProps
		} = this.props;

		const classes = getCN('custom-control', 'custom-checkbox', className, {
			['custom-control-inline']: displayInline
		});

		return (
			// We are disabling the following rules as we don't actually want
			// this elment to be explicitly interactable. It only serves to
			// stop the propagation of the event to prevent the row from being
			// select.

			/* eslint-disable jsx-a11y/no-noninteractive-element-interactions, jsx-a11y/click-events-have-key-events, jsx-a11y/no-static-element-interactions */
			<div className={classes} onClick={this.handleEventPropagation}>
				{/* eslint-enable jsx-a11y/no-noninteractive-element-interactions, jsx-a11y/click-events-have-key-events, jsx-a11y/no-static-element-interactions */}
				<label>
					<input
						{...omitDefinedProps(otherProps, Checkbox.propTypes)}
						checked={checked}
						className='custom-control-input'
						name={name}
						onChange={onChange}
						ref={this._checkboxRef}
						type='checkbox'
						value={checked}
					/>

					<span className='custom-control-label'>
						{label && (
							<span className='custom-control-label-text'>
								{label}
							</span>
						)}
					</span>
				</label>
			</div>
		);
	}
}
