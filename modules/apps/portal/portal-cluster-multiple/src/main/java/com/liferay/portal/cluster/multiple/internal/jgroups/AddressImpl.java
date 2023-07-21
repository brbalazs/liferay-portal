/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.cluster.multiple.internal.jgroups;

import org.jgroups.Address;

/**
 * @author Shuyang Zhou
 */
public class AddressImpl implements com.liferay.portal.kernel.cluster.Address {

	public AddressImpl(Address address) {
		_address = address;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AddressImpl)) {
			return false;
		}

		AddressImpl addressImpl = (AddressImpl)obj;

		if (_address.equals(addressImpl._address)) {
			return true;
		}

		return false;
	}

	@Override
	public String getDescription() {
		return _address.toString();
	}

	@Override
	public Address getRealAddress() {
		return _address;
	}

	@Override
	public int hashCode() {
		return _address.hashCode();
	}

	@Override
	public String toString() {
		return _address.toString();
	}

	private static final long serialVersionUID = 7969878022424426497L;

	private final Address _address;

}