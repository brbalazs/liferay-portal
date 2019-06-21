export const actionDefinition = {
	UPDATE_BREADCRUMBS: 'updateBreadcrumbs',
	SET_ERROR: 'setError',
	SET_LOADING: 'setLoading',
	SET_SPRITEMAP: 'setSpritemap',
	SET_BASENAME: 'setBasename',
	SET_BASE_PATH_URL: 'setBasePathUrl',
};

const updateBreadcrumbs = dispatch => breadcrumbs => dispatch({
	type: actionDefinition.UPDATE_BREADCRUMBS,
	payload: breadcrumbs,
});

const setBasePathUrl = dispatch => path => dispatch({
	type: actionDefinition.SET_BASE_PATH_URL,
	payload: path,
});

const setError = dispatch => error =>
	dispatch({
		type: actionDefinition.SET_ERROR,
		payload: error,
	});

const setLoading = dispatch => loading =>
	dispatch({
		type: actionDefinition.SET_LOADING,
		payload: loading,
	});

const setSpritemap = dispatch => spritemap =>
	dispatch({
		type: actionDefinition.SET_SPRITEMAP,
		payload: spritemap,
	});

const setBasename = dispatch => basename =>
	dispatch({
		type: actionDefinition.SET_BASENAME,
		payload: basename,
	});

export const actions = {
	updateBreadcrumbs,
	setError,
	setLoading,
	setSpritemap,
	setBasename,
	setBasePathUrl,
};
