import {actionTypes} from '../actions/projects';
import {createReducer} from 'redux-toolbox';
import {handleError, handleLoading} from '../util/redux';
import {Map} from 'immutable';
import {RemoteData} from 'shared/util/records';

const actionHandlers = {
	[actionTypes.FETCH_PROJECT_STATE_FAILURE]: handleError,
	[actionTypes.FETCH_PROJECT_STATE_REQUEST]: handleLoading,
	[actionTypes.UPDATE_PROJECT_SUCCESS]: (
		state,
		{meta: {newId, prevId}, payload}
	) => {
		const {groupId} = payload.entities.projects[prevId].data;

		if (newId && prevId !== newId) {
			return state
				.set(
					newId || String(groupId),
					new RemoteData({
						data: state.getIn([prevId, 'data']),
						loading: false
					})
				)
				.delete(prevId);
		}

		return state;
	}
};

export default createReducer(new Map(), actionHandlers);
