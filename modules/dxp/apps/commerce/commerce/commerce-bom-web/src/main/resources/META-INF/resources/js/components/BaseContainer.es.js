import React from 'react';

import LocalizedText from './utilities/LocalizedText.es';

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
                    <LocalizedText desc="Select above: the Carmaker, Model, Type and Car Parts to start your research!">
                        select-above
                    </LocalizedText>
                </h4>
            </div>
        </div>
    );
}

export default BaseContainer;
