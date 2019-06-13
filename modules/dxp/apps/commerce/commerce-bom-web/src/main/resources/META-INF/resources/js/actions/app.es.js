export const actionDefinition = {
	UPDATE_BREADCRUMBS: 'updateBreadcrumbs',
	SET_ERROR: 'setError',
	SET_LOADING: 'setLoading',
	SET_SPRITEMAP: 'setSpritemap',
};

const updateBreadcrumbs = dispatch => breadcrumbs => {
	return dispatch({
		type: actionDefinition.UPDATE_BREADCRUMBS,
		payload: breadcrumbs,
	});
};

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

export const actions = {
	updateBreadcrumbs,
	setError,
	setLoading,
	setSpritemap,
};
