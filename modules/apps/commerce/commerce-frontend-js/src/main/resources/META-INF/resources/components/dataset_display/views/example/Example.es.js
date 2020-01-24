import ClayList from '@clayui/list';
import PropTypes from 'prop-types';
import React from 'react';

function Example(props) {
	return (
		<ClayList className="mb-0 p-3 bg-white">
			<pre className="text-wrap mb-0">{JSON.stringify(props.items)}</pre>
		</ClayList>
	);
}

Example.propTypes = {
	dataRenderers: PropTypes.object,
	datasetDisplayContext: PropTypes.any,
	items: PropTypes.array
};

Example.defaultProps = {
	items: []
};

export default Example;
