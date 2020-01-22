import React from 'react';
import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';

function Default(props) {
	switch (true) {
		case !(props.value instanceof Object):
			return <React.Fragment>{props.value}</React.Fragment>
		case !!props.value.icon:
			return <ClayIcon symbol={props.value.icon} />
		case !!props.value.pictureUrl:
			return (<img alt={props.value.label} src={props.value.pictureUrl} />)
		case !!props.value.label:
			return <React.Fragment>{props.value.label}</React.Fragment>
		default:
			throw new Error(`The object ${JSON.stringify(props.value)} doesn't match the template schema`)
	}
}

Default.propTypes = {
	value: PropTypes.oneOfType([
		PropTypes.string,
		PropTypes.number,
		PropTypes.shape(PropTypes.oneOf([
			{
				icon: PropTypes.string.isRequired
			},
			{
				label: PropTypes.string.isRequired
			},
			{
				label: PropTypes.string.isRequired,
				pictureUrl: PropTypes.string.isRequired,
			},
		]))
	])
}

export default Default;
