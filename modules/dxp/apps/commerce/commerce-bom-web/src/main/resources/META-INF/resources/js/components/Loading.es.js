import React from "react";

import LocalizedText from "./utilities/LocalizedText.es";

export default function Loading(props) {
    return (
        <div 
            className="panel panel-secondary"
        >
            <div className="panel-body">
                <h3 className="text-center">
                    <LocalizedText desc="Loading">
                        loading
                    </LocalizedText>
                </h3>
            </div>
        </div>
    )
}

