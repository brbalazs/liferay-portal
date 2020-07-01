import * as API from 'shared/api';
import FaroConstants from 'shared/util/constants';
import React from 'react';
import {alertTypes} from 'shared/actions/alerts';
import {DataSource} from 'shared/util/records';
import {formatUTCDateFromUnix} from 'shared/util/date';
import {Routes, toRoute} from 'shared/util/router';
import {sub} from 'shared/util/lang';
import {toPromise} from 'shared/components/form';

const {
	credentialTypes: {token},
	dataSourceStates: {
		actionNeeded,
		analyticsClientConfigurationFailure,
		credentialsInvalid,
		credentialsValid,
		disconnected,
		inProgressDeleting,
		liferayVersionInvalid,
		ready,
		undefinedError,
		urlInvalid
	},
	dataSourceStatuses,
	dataSourceTypes: {csv, liferay, salesforce},
	entityTypes: {dataSource}
} = FaroConstants;

export const LIFERAY_SITE_TYPE = `${dataSource}-site`;

/**
 * Default timeout for a dataSource warning alert
 */
export const WARNING_TIMEOUT = 7000;

export const SERVICE_ERROR_MESSAGE_MAP = {
	403: Liferay.Language.get(
		'data-source-credentials-invalid-please-contact-your-oauth-administrator'
	),
	404: Liferay.Language.get(
		'there-was-an-error-with-the-servers-connection.-please-try-again-when-data-sources-status-is-active'
	)
};

/**
 * Return config to use in the alert for a service error.
 * @param {Number} code - The http response error code.
 * @returns {Object} The props to use for the alert.
 */
export function getServiceAlertConfig(code) {
	const message = (code => {
		switch (code) {
			case 401:
			case 403:
				return SERVICE_ERROR_MESSAGE_MAP[403];
			case 404:
			default:
				return SERVICE_ERROR_MESSAGE_MAP[404];
		}
	})(code);

	return {
		alertType: alertTypes.WARNING,
		message,
		timeout: WARNING_TIMEOUT
	};
}

/**
 * Object map for displaying a DataSource status/state
 */
export const STATUS_DISPLAY = {
	[actionNeeded]: {
		display: 'warning',
		label: Liferay.Language.get('action-needed'),
		message: Liferay.Language.get('action-needed')
	},
	active: {
		display: 'success',
		label: Liferay.Language.get('active'),
		message: Liferay.Language.get(
			'all-data-coming-from-this-data-source-is-up-to-date.-there-are-no-errors-to-report'
		)
	},
	[credentialsInvalid]: {
		display: 'warning',
		label: Liferay.Language.get('invalid-credentials'),
		message: Liferay.Language.get(
			'the-authorization-for-this-data-source-has-expired.-please-reauthorize-the-token-in-the-oauth-tab'
		)
	},
	[credentialsValid]: {
		display: 'info',
		label: Liferay.Language.get('authenticated'),
		message: Liferay.Language.get(
			'you-have-successfully-authenticated.-you-can-now-configure-your-data'
		)
	},
	default: {
		display: 'secondary',
		label: Liferay.Language.get('not-configured'),
		message: Liferay.Language.get(
			'data-source-has-not-been-created.-please-authorize-and-save-to-get-started'
		)
	},
	[disconnected]: {
		display: 'secondary',
		label: Liferay.Language.get('disconnected'),
		message: Liferay.Language.get(
			'the-data-source-is-disconnected.-data-is-no-longer-being-synced-from-dxp,-but-you-can-reconnect-to-resume-syncing'
		)
	},
	[inProgressDeleting]: {
		display: 'info',
		label: Liferay.Language.get('deletion-in-progress'),
		message: Liferay.Language.get(
			'this-data-source-and-its-related-data-are-currently-being-deleted'
		)
	},
	[liferayVersionInvalid]: {
		display: 'warning',
		label: Liferay.Language.get('unsupported-version'),
		message: Liferay.Language.get(
			'this-method-of-connection-does-not-support-the-data-source-liferay-version'
		)
	},
	tokenCredentialsValid: {
		display: 'success',
		label: Liferay.Language.get('connected'),
		message: Liferay.Language.get(
			'you-have-successfully-authenticated-your-token-with-your-data-source.-you-can-now-configure-your-data-in-dxp'
		)
	},
	[undefinedError]: {
		display: 'warning',
		label: Liferay.Language.get('inactive'),
		message: Liferay.Language.get(
			'a-server-error-occurred-while-connected-to-your-data-source.-analytics-cloud-will-attempt-to-automatically-reconnect-during-its-next-regularly-scheduled-sync'
		)
	}
};

/**
 * @param {Object} props
 * @param {DataSource} props.dataSource - A DataSource record.
 * @param {string} props.groupId
 * @returns {string} - Returns a route to the data source authorization page if not valid.
 */
export function dataSourceRedirectFn({dataSource, groupId}) {
	if (isDataSourceValid(dataSource.state)) {
		return null;
	} else {
		return toRoute(Routes.SETTINGS_DATA_SOURCE, {
			groupId,
			id: dataSource.id
		});
	}
}

/**
 * Check if the dataSource state is valid.
 * @param {string} state - The dataSource state.
 * @returns {boolean} - True if this state is valid or false if not valid.
 */
export function isDataSourceValid(state) {
	return [credentialsValid, ready].includes(state);
}

/**
 * Form validator to check if the dataSource name is already taken
 * @param {string} value - The current input value for the dataSource name.
 * @returns {Promise.<Object>} - A Promise resolving with an assertion object
 */
export function validateUniqueName({groupId, value}) {
	return API.dataSource
		.search({
			cur: 1,
			delta: 1,
			groupId,
			name: value
		})
		.then(result => {
			let error = '';
			if (result.total) {
				error = Liferay.Language.get(
					'a-data-source-already-exists-with-that-name.-please-enter-a-different-name'
				);
			}

			return toPromise(error);
		});
}

/**
 * Utility for getting the display object for a dataSource state in order
 * to display its status.
 */
export function getDataSourceDisplayObject(
	dataSource: DataSource,
	showActionNeededStatus: boolean = false
) {
	if (!dataSource) {
		return STATUS_DISPLAY.default;
	}
	if (showActionNeededStatus && hasLegacyDXPConnection(dataSource)) {
		return STATUS_DISPLAY[actionNeeded];
	}

	const {lastSyncDate, state, status} = dataSource;

	const active = status === dataSourceStatuses.active;

	const credentialsType = dataSource.getIn(['credentials', 'type']);

	switch (state) {
		case analyticsClientConfigurationFailure:
		case credentialsInvalid:
		case urlInvalid:
			return STATUS_DISPLAY[credentialsInvalid];
		case disconnected:
			return STATUS_DISPLAY[disconnected];
		case credentialsValid:
			if (
				(validContactsConfig(dataSource) && active) ||
				validAnalyticsConfig(dataSource)
			) {
				return STATUS_DISPLAY.active;
			}

			return credentialsType === token
				? STATUS_DISPLAY.tokenCredentialsValid
				: STATUS_DISPLAY[credentialsValid];
		case inProgressDeleting:
			return STATUS_DISPLAY[inProgressDeleting];
		case liferayVersionInvalid:
			return STATUS_DISPLAY[liferayVersionInvalid];
		case ready:
			return STATUS_DISPLAY.active;
		case undefinedError:
			return {
				...STATUS_DISPLAY[undefinedError],
				message: [
					STATUS_DISPLAY[undefinedError].message,
					lastSyncDate && (
						<LastSyncDate
							date={lastSyncDate}
							key='LAST_SYNC_DATE'
						/>
					)
				]
			};
		default:
			return STATUS_DISPLAY.default;
	}
}

interface ILastSyncDateProps extends React.HTMLAttributes<HTMLElement> {
	date: number;
}

class LastSyncDate extends React.Component<ILastSyncDateProps> {
	render() {
		const {className, date} = this.props;

		return (
			<p className={className}>
				<b>
					{`${sub(Liferay.Language.get('last-successful-sync-x'), [
						formatUTCDateFromUnix(date, 'l - LT')
					])} GMT`}
				</b>
			</p>
		);
	}
}

/**
 * Get an array of ids from a DataSource configuration Map.
 * @param {Map} configIMap - An ImmutableMap of List.<Map>.
 * @param {string} key - The Map key that contains the List of objects.
 * @returns {array} - The List of objects converted to an array of numerical ids.
 */
export function getIdsFromConfiguration(configIMap, key) {
	return configIMap
		.get(key, new Map())
		.map(itemIMap => Number(itemIMap.get('id')))
		.toArray();
}

/**
 * Check if a DataSource is considered legacy (DXP and not token authentication)
 */
export const hasLegacyDXPConnection = (dataSource: DataSource) =>
	dataSource.providerType === liferay &&
	dataSource.getIn(['credentials', 'type']) !== token;

/**
 * Helper function for checking validity of a DataSource's analyticsConfiguration.
 */
export function validAnalyticsConfig(dataSource: DataSource): boolean {
	const {provider, providerType} = dataSource;

	if (providerType === liferay && !hasLegacyDXPConnection(dataSource)) {
		return dataSource.sitesSelected;
	}

	const analyticsConfiguration =
		provider && provider.get('analyticsConfiguration');

	if (!analyticsConfiguration) {
		return false;
	}

	switch (providerType) {
		case liferay:
			return Boolean(
				analyticsConfiguration.get('enableAllSites') ||
					analyticsConfiguration.get('sites').size
			);
		// TODO: Add validation on salesforce anlayticsConfiguration
		case salesforce:
		default:
			return Boolean(analyticsConfiguration);
	}
}

/**
 * Helper function for checking validity of a DataSource's contactsConfiguration.
 */
export function validContactsConfig(dataSource: DataSource): boolean {
	const {provider, providerType, status} = dataSource;

	if (providerType === liferay && !hasLegacyDXPConnection(dataSource)) {
		return dataSource.contactsSelected;
	}

	const contactsConfiguration =
		provider && provider.get('contactsConfiguration');

	if (!contactsConfiguration && providerType !== csv) {
		return false;
	}

	switch (providerType) {
		case csv:
			return status === dataSourceStatuses.active;
		case liferay:
			return Boolean(
				contactsConfiguration.get('enableAllContacts') ||
					contactsConfiguration.get('organizations').size ||
					contactsConfiguration.get('userGroups').size
			);
		// TODO: Add validation on salesforce contactsConfiguration
		case salesforce:
		default:
			return Boolean(contactsConfiguration);
	}
}
