/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.messaging;

/**
 * @author Marcellus Tavares
 */
public enum Channel {

	ANALYTICS_EVENTS, ANALYTICS_EVENTS_CUSTOM_ASSET, COMPOSER,
	DXP_ENTITIES_DEFAULT, DXP_ENTITIES_MESSAGE(true), DXP_ENTITIES_ORDER,
	DXP_ENTITIES_PRODUCT, IDENTITY_MESSAGE;

	public boolean isOrderingEnabled() {
		return _orderingEnabled;
	}

	private Channel() {
		_orderingEnabled = false;
	}

	private Channel(boolean orderingEnabled) {
		_orderingEnabled = orderingEnabled;
	}

	private final boolean _orderingEnabled;

}