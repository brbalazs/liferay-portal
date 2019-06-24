import React, {Component} from 'react';
import {
    callApi,
    setupDataset
} from '../utils/utils.es';

import OrgChart from './OrgChart';
import MembersPane from './MembersPane';

class OrgChartContainer extends Component {
    constructor(props) {
        super(props);

        _.bindAll(
            this,
            'handleNodeClick',
            'setSelection',
            'handleInitialLoad'
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
                            rootData: setupDataset(dataset),
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

    setSelection(id, colorIdentifier) {
        this.setState(() => {
            return {selectedId: id, colorIdentifier}
        });
    }

    render() {
        const {
            apiURL,
            spritemap,
            imagesPath,
        } = this.props;

        const {
            selectedId,
            rootData,
            colorIdentifier
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
                    colorIdentifier={colorIdentifier}
                />
                }
            </div>
        );
    }
}

export default OrgChartContainer;
