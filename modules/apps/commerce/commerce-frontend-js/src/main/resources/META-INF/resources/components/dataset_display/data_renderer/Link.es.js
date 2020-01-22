import ClayLink from '@clayui/link';
import React from 'react';
import PropTypes from 'prop-types';
import DefaultContent from './Default.es';

function Link(props) {
	return (
		<ClayLink href={props.value.url}>
			<DefaultContent {...props} />
		</ClayLink>
	);
}

Link.propTypes = {
	label: PropTypes.string,
	url: PropTypes.string,
}

export default Link;
