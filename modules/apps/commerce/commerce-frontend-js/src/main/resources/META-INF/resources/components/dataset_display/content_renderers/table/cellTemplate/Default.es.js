import React from 'react';
import ClayIcon from '@clayui/icon';

function Default(props) {
	switch (true) {
		case !!props.value.icon:
			return <ClayIcon symbol={props.value.icon} />
		case !!props.value.pictureUrl:
			return (<img alt={props.value.label} src={props.value.pictureUrl} />)
		case !!props.value.label:
			return (<React.Fragment>{props.value.label}</React.Fragment>)
		default:
			return <React.Fragment>{props.value}</React.Fragment>
	}
}

export default Default;
