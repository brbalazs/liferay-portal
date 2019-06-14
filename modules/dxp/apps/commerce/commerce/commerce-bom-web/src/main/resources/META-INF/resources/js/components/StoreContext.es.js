import React, {createContext, useReducer} from 'react';

import {combineReducers} from 'redux';

import applyMiddleware from '../middleware/index.es';

import appReducer, {initialState as initialAppState} from '../reducers/app.es';
import areaReducer, {initialState as initialAreaState} from '../reducers/area.es';
import folderReducer, {initialState as initialFolderState} from '../reducers/folder.es';

import {actions as appActions} from '../actions/app.es';
import {actions as areaActions} from '../actions/area.es';
import {actions as folderActions} from '../actions/folder.es';

export const StoreContext = createContext();

export function initializeActions(actions, dispatch) {
    return Object.keys(actions).reduce(
        (curriedActions, actionName) => ({
            ...curriedActions,
            [actionName]: actions[actionName](dispatch)
        }),
        {}
    );
}

const reducers = combineReducers({
    app: appReducer,
    folder: folderReducer,
    area: areaReducer
});

export function StoreProvider(props) {
    const [state, dispatch] = useReducer(
        reducers,
        Object.assign(
            {},
            {
                app: initialAppState,
                area: initialAreaState,
                folder: initialFolderState
            }
        )
    );

	const actions = initializeActions(
        Object.assign(
            {},
            appActions,
            areaActions,
            folderActions
        ),
		applyMiddleware(dispatch)
    );

    const value = {
        state,
        actions
    };

	return (
        <StoreContext.Provider value={value}>
            {props.children}
        </StoreContext.Provider>
    );
}

export const StoreConsumer = StoreContext.Consumer;
