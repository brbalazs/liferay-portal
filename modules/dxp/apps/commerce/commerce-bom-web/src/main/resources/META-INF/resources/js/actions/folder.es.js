export const actionDefinition = {
    GET_FOLDER_FULFILLED: 'getFolderFulfilled',
    GET_FOLDER_REJECTED: 'getFolderRejected',
    GET_FOLDER_PENDING: 'getFolderPending'
}

const getFolder = (dispatch) => (url) => {
    dispatch(
        {
            type: actionDefinition.GET_FOLDER_PENDING 
        }
    )
    return fetch(url)
        .then(
            response => response.json()
        )
        .then(
            (data) => dispatch({ 
                type: actionDefinition.GET_FOLDER_FULFILLED,
                payload: data 
            })
        )
        .catch(
            (err) => dispatch({ 
                type: actionDefinition.GET_FOLDER_REJECTED,
                payload: err 
            })
        )
}

export const actions = {
    getFolder
}

