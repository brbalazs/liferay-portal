/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import Component from 'metal-component';
import Soy from 'metal-soy';
import {Config} from 'metal-state';

import templates from './CPDefinitionOptionValuesEditor.soy';

/**
 * CPDefinitionOptionValuesEditor
 *
 */

class CPDefinitionOptionValuesEditor extends Component {
	constructor(opt_config, opt_parentElement) {
		super(opt_config, opt_parentElement);

		this.on('showChanged', this._handleShowChange);
		this.on('cpDefinitionOptionRelIdChanged', this._handleShowChange);
	}

	_handleClose() {
		this.emit('close');
	}

	_handleAddOptionValue() {
		this._newCPDefinitionOptionValueRelName = '';
		this._currentCPDefinitionOptionValueRelId = '0';
	}

	loadOptionValues() {
		this._updateCPDefinitionId();

		if (!this.cpDefinitionOptionRelId) {
			this._newCPDefinitionOptionValueRelName = '';
			this._currentCPDefinitionOptionValueRelId = '0';

			return;
		}

		var url = new URL(this.cpDefinitionOptionValueRelsURL);

		url.searchParams.append(
			this.namespace + 'cpDefinitionOptionRelId',
			this.cpDefinitionOptionRelId
		);
		url.searchParams.set('p_auth', Liferay.authToken);

		fetch(
			url,
			{
				credentials: 'include',
				headers: new Headers({ 'x-csrf-token': Liferay.authToken }),
				method: 'GET'
			}
		).then(
			response => response.json()
		).then(
			(jsonResponse) => {
				this._cpDefinitionOptionValueRels = jsonResponse;

				if (
					this._cpDefinitionOptionValueRels &&
					this._cpDefinitionOptionValueRels.length > 0
				) {
					if (
						!this._currentCPDefinitionOptionValueRelId ||
						this._currentCPDefinitionOptionValueRelId == null
					) {
						this._currentCPDefinitionOptionValueRelId = this._cpDefinitionOptionValueRels[0].cpDefinitionOptionValueRelId;
					}
				} else if (
					this._cpDefinitionOptionValueRels &&
					this._cpDefinitionOptionValueRels.length == 0
				) {
					this._newCPDefinitionOptionValueRelName = '';
					this._currentCPDefinitionOptionValueRelId = '0';
				}
			});
	}

	_handleShowChange(_event) {
		this.loadOptionValues();
	}

	_handleOptionValueSelected(cpOptionValueId) {
		this._currentCPDefinitionOptionValueRelId = cpOptionValueId;
	}

	_handleOptionValueSaved(event) {
		this.cpDefinitionId = event.cpDefinitionId.toString();
		this.cpDefinitionOptionRelId = event.cpDefinitionOptionRelId.toString();
		this._currentCPDefinitionOptionValueRelId =
			event.cpDefinitionOptionValueRelId;

		this.loadOptionValues();

		if (event.success) {
			this._showNotification(this.successMessage, 'success');
		} else {
			this._showNotification(event.message, 'danger');
		}
	}

	_handleOptionValueDeleted(event) {
		this.cpDefinitionId = event.cpDefinitionId.toString();
		this.cpDefinitionOptionRelId = event.cpDefinitionOptionRelId.toString();
		this._currentCPDefinitionOptionValueRelId = null;

		this.loadOptionValues();
	}

	_handleCancelEditing(_event) {
		this._currentCPDefinitionOptionValueRelId = null;

		this.loadOptionValues();
	}

	_handleNameChange(newName) {
		if (this._currentCPDefinitionOptionValueRelId == '0') {
			this._newCPDefinitionOptionValueRelName = newName;
		} else {
			this._newCPDefinitionOptionValueRelName = '';
		}
	}

	_showNotification(message, type) {
		AUI().use('liferay-notification', () => {
			new Liferay.Notification({
				closeable: true,
				delay: {
					hide: 5000,
					show: 0
				},
				duration: 500,
				message,
				render: true,
				title: '',
				type
			});
		});
	}

	_updateCPDefinitionId() {
		const instance = this;

		var url = new URL(window.location.href);

		var cpDefinitionId = url.searchParams.get(
			instance.namespace + 'cpDefinitionId'
		);

		if (
			instance.cpDefinitionId &&
			cpDefinitionId != instance.cpDefinitionId
		) {
			var cpDefinitionOptionValueRelURL = new URL(
				instance.cpDefinitionOptionValueRelURL
			);

			cpDefinitionOptionValueRelURL.searchParams.set(
				instance.namespace + 'cpDefinitionId',
				instance.cpDefinitionId
			);

			instance.cpDefinitionOptionValueRelURL =
				cpDefinitionOptionValueRelURL.href;

			var cpDefinitionOptionValueRelsURL = new URL(
				instance.cpDefinitionOptionValueRelsURL
			);

			cpDefinitionOptionValueRelsURL.searchParams.set(
				instance.namespace + 'cpDefinitionId',
				instance.cpDefinitionId
			);

			instance.cpDefinitionOptionValueRelsURL =
				cpDefinitionOptionValueRelsURL.href;

			url.searchParams.set(
				instance.namespace + 'cpDefinitionId',
				instance.cpDefinitionId
			);

			window.history.pushState({}, '', url);
		}
	}
}

/**
 * State definition.
 * @type {!Object}
 * @static
 */

CPDefinitionOptionValuesEditor.STATE = {
	_cpDefinitionOptionValueRels: Config.array().value([]),
	_newCPDefinitionOptionValueRelName: Config.string().value(''),
	cpDefinitionId: Config.string(),
	cpDefinitionOptionRelId: Config.string().required(),
	cpDefinitionOptionValueRelURL: Config.string().required(),
	cpDefinitionOptionValueRelsURL: Config.string().required(),
	namespace: Config.string().required(),
	pathThemeImages: Config.string().required(),
	show: Config.bool().value(false),
	successMessage: Config.string().required()
};

// Register component

Soy.register(CPDefinitionOptionValuesEditor, templates);

export default CPDefinitionOptionValuesEditor;
