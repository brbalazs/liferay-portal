import PropTypes from 'prop-types';
import React, {useContext} from 'react';

import DatasetDisplayContext from '../DatasetDisplayContext.es';
import DefaultContent from './Default.es';

function SidePanelLink(props) {
	const {loadData, openSidePanel} = useContext(DatasetDisplayContext);

	function handleClickOnLink(e, payload) {
		e.preventDefault();

		openSidePanel(payload);
	}

	return (
		<button
			className="btn btn-link btn-sm p-0"
			onClick={e =>
				handleClickOnLink(e, {
					onSubmit: loadData,
					size: props.value.size,
					url: props.value.url
				})
			}
		>
			<DefaultContent value={props.value.label} />
		</button>
	);
}

SidePanelLink.propTypes = {
	value: PropTypes.shape({
		label: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
			.isRequired,
		url: PropTypes.string.isRequired
	}).isRequired
};

export default SidePanelLink;
