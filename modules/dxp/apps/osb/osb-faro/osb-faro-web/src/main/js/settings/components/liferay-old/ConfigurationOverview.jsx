import * as API from 'shared/api';
import BaseConfigurationOverview from 'settings/components/data-source/BaseConfigurationOverview';
import FaroConstants from 'shared/util/constants';
import omitDefinedProps from 'shared/util/omitDefinedProps';
import React from 'react';
import {addAlert} from 'shared/actions/alerts';
import {compose, withHistory, withPolling} from 'shared/hoc';
import {connect} from 'react-redux';
import {DataSource} from 'shared/util/records';
import {get} from 'lodash';
import {getServiceAlertConfig} from 'shared/util/data-sources';
import {getServiceError} from 'shared/util/request';
import {hasChanges} from 'shared/util/react';
import {PropTypes} from 'prop-types';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';

const {dataSourceProgressStatuses, dataSourceStatuses} = FaroConstants;

const getButtonParams = configuration => ({
	display: configuration ? 'secondary' : 'primary',
	label: configuration
		? Liferay.Language.get('edit')
		: Liferay.Language.get('configure')
});

const stopPollingCondition = ({individuals}, {dataSource}) =>
	dataSourceStatuses.active !== dataSource.status ||
	[
		dataSourceProgressStatuses.completed,
		dataSourceProgressStatuses.failed
	].includes(get(individuals, 'status'));

export class ConfigurationOverview extends React.Component {
	static propTypes = {
		addAlert: PropTypes.func.isRequired,
		dataSource: PropTypes.instanceOf(DataSource).isRequired,
		groupId: PropTypes.string.isRequired,
		history: PropTypes.object.isRequired,
		id: PropTypes.string.isRequired,
		pollingError: PropTypes.instanceOf(Error),
		progress: PropTypes.object
	};

	componentDidUpdate(prevProps) {
		if (hasChanges(prevProps, this.props, 'pollingError')) {
			this.handleServicePermissionError();
		}
	}

	buildConfigurationItems() {
		const {
			dataSource: {provider},
			groupId,
			id,
			progress
		} = this.props;

		const analyticsConfiguration = provider.get('analyticsConfiguration');

		const contactsConfiguration = provider.get('contactsConfiguration');

		return [
			{
				buttonParams: getButtonParams(contactsConfiguration),
				configuration: contactsConfiguration,
				description: Liferay.Language.get(
					'import-and-map-out-contacts-from-your-liferay-dxp-instance'
				),
				href: toRoute(Routes.SETTINGS_LIFERAY_CONTACTS, {
					groupId,
					id
				}),
				label: Liferay.Language.get('contacts'),
				progress: get(progress, 'individuals'),
				title: sub(Liferay.Language.get('configure-x'), [
					Liferay.Language.get('contacts')
				])
			},
			{
				buttonParams: getButtonParams(analyticsConfiguration),
				configuration: analyticsConfiguration,
				description: Liferay.Language.get(
					'select-your-liferay-dxp-sites-and-their-assets-to-start-tracking-user-behavior-data'
				),
				href: toRoute(Routes.SETTINGS_LIFERAY_ANALYTICS, {
					groupId,
					id
				}),
				label: Liferay.Language.get('analytics'),
				title: sub(Liferay.Language.get('configure-x'), [
					Liferay.Language.get('analytics')
				])
			}
		];
	}

	handleServicePermissionError() {
		const {addAlert, groupId, history, id, pollingError} = this.props;

		const serviceError = getServiceError(pollingError);

		if (serviceError) {
			addAlert(getServiceAlertConfig(serviceError.status));

			history.push(
				toRoute(Routes.SETTINGS_DATA_SOURCE, {
					groupId,
					id
				})
			);
		}
	}

	render() {
		const {
			dataSource: {status},
			...otherProps
		} = this.props;

		return (
			<BaseConfigurationOverview
				{...omitDefinedProps(
					otherProps,
					ConfigurationOverview.propTypes
				)}
				configurationItems={this.buildConfigurationItems()}
				status={status}
			/>
		);
	}
}

export default compose(
	withHistory,
	connect(
		null,
		{addAlert}
	),
	withPolling(API.dataSource.fetchProgress, stopPollingCondition, {
		propName: 'progress',
		requestProps: ['id']
	})
)(ConfigurationOverview);
