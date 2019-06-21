import React from 'react';

import LocalizedText from './utilities/LocalizedText.es'

function BaseContainer() {
    return (
        <div className="panel panel-secondary">
            <div className="panel-body">
                <h4>
                    <LocalizedText desc="Loading">
                        loading
                    </LocalizedText>
                </h4>
            </div>
        </div>
    )
}

export default BaseContainer;
