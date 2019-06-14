import React from 'react';

import LocalizedText from './utilities/LocalizedText.es'

function ErrorMessage(props) {
    return (
        <div className="alert-container container">
            <div className="alert-notifications alert-notifications-absolute">
                <div className="alert alert-dismissible alert-danger" role="alert">
                    <LocalizedText desc="Unexpected error">
                        unexpected-error
                    </LocalizedText>
                    {props.closeIcon && (
                        <button 
                            aria-label="Close" 
                            className="close" 
                            data-dismiss="alert" 
                            type="button"
                            onClick={props.onClose}
                        >
                            {props.closeIcon}
                        </button>
                    )}
                </div>
            </div>
        </div>
    )
}

export default ErrorMessage;
