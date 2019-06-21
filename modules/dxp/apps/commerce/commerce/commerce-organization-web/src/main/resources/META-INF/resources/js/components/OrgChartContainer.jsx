import React, {Component} from 'react';
import {callApi} from '../utils/utils.es';

import OrgChart from './OrgChart';
import MembersPane from './MembersPane';

function sanitizeAtStart(data) {
    const sanitizedData = Object.assign({}, data);

    sanitizedData.organizations.length &&
    (sanitizedData['total'] = sanitizedData.organizations.length) &&
    sanitizedData.organizations.forEach(orgObject => {
        delete orgObject['organizations'];
    });

    return sanitizedData;
}

class OrgChartContainer extends Component {
    constructor(props) {
        super(props);

        _.bindAll(
            this,
            'handleNodeClick',
            'setSelection',
            'handleInitialLoad',
            'setVisualizationMode'
        );

        const apiParameters = {
            baseURL: props.apiURL
        };

        callApi(apiParameters)
            .then(data => {
                    const dataset = Object.assign({},
                        data,
                        {
                            name: 'root',
                            organizationId: 0
                        });

                    this.setState(() => {
                        return {
                            rootData: sanitizeAtStart(dataset),
                            selectedId: 0
                        };
                    });
                }
            );
    }

    handleInitialLoad() {
        this.setState(() => {
            return {loading_: false};
        });
    }

    handleNodeClick(id) {
        return callApi({ baseURL: this.props.apiURL, id })
            .then(({organizations}) =>
                organizations.length ? organizations : null
            );
    }

    setSelection(id) {
        this.setState(() => {
            return {selectedId: id}
        });
    }

    setVisualizationMode(mode) {
        this.setState(() => {
            return {currentMode: 'chart'}
        })
    }

    render() {
        const {
            apiURL,
            spritemap,
            imagesPath
        } = this.props;

        const {
            selectedId,
            rootData
        } = this.state || {};

        return (
            <div className="org-chart-modal-container">
                {!!rootData &&
                <OrgChart
                    data={rootData}
                    onNodeClick={this.setSelection}
                    requestChildren={this.handleNodeClick}
                    selectedId={selectedId}
                />
                }

                {!!selectedId &&
                <MembersPane
                    id={selectedId}
                    apiURL={apiURL}
                    spritemap={spritemap}
                    imagesPath={imagesPath}
                />
                }
            </div>
        );
    }
}

export default OrgChartContainer;
