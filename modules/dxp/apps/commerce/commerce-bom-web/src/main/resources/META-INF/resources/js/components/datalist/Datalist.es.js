import React, { Component } from 'react';

import Datasource from '../../utilities/data_connectors/Datasource.es';
import Connector from '../../utilities/data_connectors/Connector.es';
import BaseDatalist from './BaseDatalist.es';

class Datalist extends Component {
	constructor(props) {
		super(props);
		const { connectorSettings, datasourceSettings } = props;

		this.state = {
			datasource: this.initializeDatasource(datasourceSettings),
			connector: this.initializeConnector(connectorSettings),
			data: null
		};
	}

	initializeDatasource(generalSettings) {
		const { on, ...settings } = generalSettings;
		return new Datasource({
			...settings,
			on: Object.assign(
				{},
				{
					read: data => {
						const formattedData = data.map(el => ({
							label:
								el[
									this.props.datasourceSettings.labelField ||
										'label'
								],
							value:
								el[
									this.props.datasourceSettings.valueField ||
										'value'
								]
						}));
						this.setState({
							data: formattedData
						});
					}
				},
				on || null
			)
		});
	}

	initializeConnector(generalSettings) {
		const { on, ...settings } = generalSettings;
		return new Connector({
			...settings,
			on: Object.assign(
				{},
				{
					getValue: () => this.state.value,
					notified: values => {
						const allEmitterHadBeenSelected = values.reduce(
							(acc, el) => acc && !!el.value,
							true
						);

						this.setState({
							disabled: !allEmitterHadBeenSelected
						});
					}
				},
				on || null
			)
		});
	}

	emit(eventName, payload) {
		switch (eventName) {
			case 'selectedValuesChanged':
				this.setState(
					{
						value: payload.length ? payload : null
					},
					() => {
						this.state.connector.notify();
						if (this.props.onchange) {
							this.props.onchange(payload);
						}
					}
				);
				break;
			case 'queryUpdated':
				this.state.datasource.setFilter('keyword', payload, 'contains');
				this.state.datasource.read();
				break;
			default:
				break;
		}
	}

	render() {
		const disabled =
			typeof this.state.disabled === 'boolean'
				? this.state.disabled
				: typeof this.props.disabled === 'boolean'
				? this.props.disabled
				: false;

        const { connectorSettings, datasourceSettings, ...baseProps } = this.props;


		return (
			<BaseDatalist
				emit={(e, payload) => this.emit(e, payload)}
				data={this.state.data || this.props.data || null}
                disabled={disabled}
                {...baseProps}
			/>
		);
	}
}

export default Datalist;
