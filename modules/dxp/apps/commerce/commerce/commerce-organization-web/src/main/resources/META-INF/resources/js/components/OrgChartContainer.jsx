import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {fetchData} from '../utils/utils.es';

import OrgChart from 'components/OrgChart';
import MembersPane from 'components/MembersPane';

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

		const {id} = this.props;

		fetchData(id || 0)
			.then(response => response.json())
			.then(
				data => {
					const dataset = /*!!id ?
						Object.assign({},
							data,
							{
								name: 'organizations',
								id: 0
							}) : */ data;

					this.setState(() => {
						return {
							rootData: dataset,
							selectedId: id || 0
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
		return fetchData(id)
			.then(res => res.json())
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
				/>
				}
			</div>
		);
	}
}

OrgChartContainer.defaultProps = {
	data_: null,
	loading_: true
};

OrgChartContainer.propTypes = {
	fetchChildDivisions: PropTypes.func,
	fetchHierarchy: PropTypes.func,
	hideModal: PropTypes.func,
	id: PropTypes.number,
	selectedId_: PropTypes.number
};

export default OrgChartContainer;
