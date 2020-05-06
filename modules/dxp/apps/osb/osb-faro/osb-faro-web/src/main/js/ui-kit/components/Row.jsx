import getCN from 'classnames';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {PropTypes} from 'prop-types';

export default class Row extends React.Component {
	static defaultProps = {
		flex: true
	};

	static propTypes = {
		flex: PropTypes.bool
	};

	render() {
		const {className, flex, ...otherProps} = this.props;

		const classes = getCN('kit-row-root', className, {
			['d-flex flex-wrap']: flex
		});

		return (
			<div
				className={classes}
				{...omitDefinedProps(otherProps, Row.propTypes)}
			>
				{this.props.children}
			</div>
		);
	}
}
