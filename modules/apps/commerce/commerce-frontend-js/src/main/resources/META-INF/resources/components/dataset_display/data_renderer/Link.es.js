import ClayLink from '@clayui/link';
import PropTypes from 'prop-types';
import React from 'react';

import DefaultContent from './Default.es';

function Link(props) {
	return (
		<ClayLink href={props.value.url}>
			<DefaultContent value={props.value.label} />
		</ClayLink>
	);
}

Link.propTypes = {
	value: PropTypes.shape({
		label: PropTypes.string,
		url: PropTypes.string
	})
};

export default Link;
