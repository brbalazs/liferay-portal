import React from 'react';

const DatasetDisplayContext = React.createContext({
	dataRenderers: [],
	formRef: null,
	loadData: () => {},
	modalId: null,
	openModal: () => {},
	openSidePanel: () => {},
	selectItems: () => {},
	selectable: false,
	selectedItemsId: [],
	sidePanelId: null,
	sorting: [],
	updateSorting: () => {},
});

export default DatasetDisplayContext;
