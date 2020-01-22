import React from 'react';

const DatasetDisplayContext = React.createContext({
	formId: null,
	formRef: null,
	loadData: () => {},
	modalId: null,
	openModal: () => {},
	openSidePanel: () => {},
	selectItems: () => {},
	selectable: false,
	selectedItemsValue: [],
	sidePanelId: null,
	sorting: [],
	updateSorting: () => {},
});

export default DatasetDisplayContext;
