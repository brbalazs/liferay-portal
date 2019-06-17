import { actionDefinition } from '../actions/app.es'

export const initialState = {
    breadcrumbs: null,
    loading: false,
    error: null,
    spritemap: null,
    basename: '/'
}

export default function reducer(state = initialState, action) {
    switch (action.type) {
        case actionDefinition.SET_ERROR:
            return {
                ...state,
                error: action.payload 
            }
        case actionDefinition.UPDATE_BREADCRUMBS:
            return {
                ...state,
                breadcrumbs: action.payload
            }
        case actionDefinition.SET_LOADING:
            return {
                ...state,
                loading: action.payload
            }
        case actionDefinition.SET_SPRITEMAP:
            return {
                ...state,
                spritemap: action.payload
            }
        case actionDefinition.SET_BASENAME:
            return {
                ...state,
                basename: action.payload
            }
        default:
            return state;
    }
};