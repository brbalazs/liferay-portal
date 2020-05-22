import React from 'react';

const Context: React.Context<BasePage.Context> = React.createContext({
	filters: {},
	router: {
		params: {},
		query: {}
	}
});

Context.displayName = 'BasePageContext';

export default Context;
