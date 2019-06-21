import React from 'react';

import LocalizedText from './utilities/LocalizedText.es'

function BaseContainer() {
    return (
        <div className="panel panel-secondary">
            <div className="panel-body">
                <h2>
                    <LocalizedText desc="Select Car &amp; Parts">
                        select-car-and-parts
                    </LocalizedText>
                </h2>
                <h4>
                    <LocalizedText desc="Please fulfill the form above to start your research!">
                        please-fill-the-form-select-above-to-start-your-research
                    </LocalizedText>
                </h4>
            </div>
        </div>
    )
}

export default BaseContainer;
