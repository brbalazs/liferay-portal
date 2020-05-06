import autobind from 'autobind-decorator';
import Icon from 'shared/components/Icon';
import Input from 'shared/components/Input';
import moment from 'moment';
import PropTypes from 'prop-types';
import React from 'react';

export default class TimeSelector extends React.Component {
	static defaultProps = {
		value: ''
	};

	static propTypes = {
		onChange: PropTypes.func,
		value: PropTypes.any
	};

	@autobind
	handleChange(event) {
		const {value} = event.target;
		const {onChange} = this.props;

		onChange(value);
	}

	render() {
		const {value} = this.props;

		const timezoneOffset = moment().format('Z');

		return (
			<div className='time-selector-root'>
				<Icon symbol='time' />

				<Input
					onChange={this.handleChange}
					type='time'
					value={
						moment.isMoment(value) ? value.format('HH:mm') : value
					}
				/>

				<div className='time-zone'>{`(GMT ${timezoneOffset})`}</div>
			</div>
		);
	}
}
