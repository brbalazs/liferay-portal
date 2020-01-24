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

import template from './ModalLinkCellTemplate.soy';

import 'clay-modal';

class ModalLinkCellTemplate extends Component {
	_openPopUp(e) {
		e.preventDefault();
		this._lazyLoad = true;
		this._modalVisible = true;
	}

	_handleCloseModal(e) {
		e.preventDefault();
		this._modalVisible = false;
	}
}

ModalLinkCellTemplate.STATE = {
	_lazyLoad: Config.bool().value(false),
	_modalVisible: Config.bool().value(false)
};

Soy.register(ModalLinkCellTemplate, template);

export {ModalLinkCellTemplate};
export default ModalLinkCellTemplate;
