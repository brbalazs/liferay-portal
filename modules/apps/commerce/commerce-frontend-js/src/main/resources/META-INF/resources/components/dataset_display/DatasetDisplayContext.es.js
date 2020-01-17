import React from 'react';

const DatasetDisplayContext = React.createContext({
	formRef: null,
	loadData: () => {},
	modalId: null,
	openModal: () => {},
	openSidePanel: () => {},
	sidePanelId: null,
	sorting: [],
	updateSorting: () => {},
});

export default DatasetDisplayContext;
