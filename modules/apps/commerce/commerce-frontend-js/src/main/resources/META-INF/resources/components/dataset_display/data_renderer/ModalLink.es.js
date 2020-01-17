import ClayLink from '@clayui/link';
import PropTypes from 'prop-types';
import React, {useContext} from 'react';

import DefaultContent from './Default.es';
import DatasetDisplayContext from '../DatasetDisplayContext.es';

function ModalLink(props) {
	const {loadData, openModal} = useContext(DatasetDisplayContext) 

	function handleClickOnLink(e, payload) {
		e.preventDefault();

		return openModal(payload);
	}

	return (
		<ClayLink
			href="#"
			onClick={e => handleClickOnLink(e, {
				onSubmit: loadData,
				size: props.value.size,
				title: props.value.title,
				url: props.value.url,
			})}
		>
			<DefaultContent value={props.value.label} />
		</ClayLink>
	);
}

ModalLink.propTypes = {
	value: PropTypes.shape({
		icon: PropTypes.string,
		label: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
		url: PropTypes.string.isRequired
	}).isRequired
};

export default ModalLink;
