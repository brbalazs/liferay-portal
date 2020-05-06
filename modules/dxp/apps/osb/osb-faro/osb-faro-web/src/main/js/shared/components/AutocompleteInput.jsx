import autobind from 'autobind-decorator';
import BaseSelect from './BaseSelect';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {noop} from 'lodash';
import {PropTypes} from 'prop-types';

class AutocompleteInput extends React.Component {
	static defaultProps = {
		onChange: noop,
		value: ''
	};

	static propTypes = {
		onBlur: PropTypes.func,
		onChange: PropTypes.func,
		value: PropTypes.string
	};

	@autobind
	handleSelect(value) {
		this.props.onChange(value);
	}

	render() {
		const {onBlur, value, ...otherProps} = this.props;

		return (
			<BaseSelect
				{...omitDefinedProps(otherProps, AutocompleteInput.propTypes)}
				inputValue={value}
				onBlur={onBlur}
				onInputValueChange={this.handleSelect}
				onSelect={this.handleSelect}
			/>
		);
	}
}

export default AutocompleteInput;
