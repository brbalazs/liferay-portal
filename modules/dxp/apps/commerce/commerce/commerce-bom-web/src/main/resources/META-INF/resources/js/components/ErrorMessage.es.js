import React from 'react';

import LocalizedText from './utilities/LocalizedText.es'

function ErrorMessage() {
    return (
        <div className="panel panel-secondary">
            <div className="panel-body">
                <h2 className="text-center">
                    <LocalizedText desc="Unexpected error">
                        unexpected-error
                    </LocalizedText>
                </h2>
            </div>
        </div>
    )
}

export default ErrorMessage;
