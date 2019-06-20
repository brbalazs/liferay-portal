import OrgChartContainer from 'components/OrgChartContainer';
import React from 'react';
import ReactDOM from 'react-dom';

import w from 'utils/window.es';

export default function(id) {
	const portletFrame = w.document.getElementById(id);

	ReactDOM.render(<OrgChartContainer />, portletFrame);
}
