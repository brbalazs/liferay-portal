import OrgChartContainer from 'components/OrgChartContainer';
import React from 'react';
import ReactDOM from 'react-dom';

export default function(componentId, id, props) {
	const portletFrame = window.document.getElementById(id);
	let instance = null;

	ReactDOM.render(
		<OrgChartContainer
			ref={component => {
				instance = component;
			}}
			{...props}
		/>, portletFrame);

	if (window.Liferay) {
		window.Liferay.component(componentId, instance);
	}

	return instance;
}
