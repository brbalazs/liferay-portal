/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the FaroProjectEmailAddressDomain service. Represents a row in the &quot;OSBFaro_FaroProjectEmailAddressDomain&quot; database table, with each column mapped to a property of this class.
 *
 * @author Matthew Kong
 * @see FaroProjectEmailAddressDomainModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.osb.faro.model.impl.FaroProjectEmailAddressDomainImpl"
)
@ProviderType
public interface FaroProjectEmailAddressDomain
	extends FaroProjectEmailAddressDomainModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.osb.faro.model.impl.FaroProjectEmailAddressDomainImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<FaroProjectEmailAddressDomain, Long>
		FARO_PROJECT_EMAIL_ADDRESS_DOMAIN_ID_ACCESSOR =
			new Accessor<FaroProjectEmailAddressDomain, Long>() {

				@Override
				public Long get(
					FaroProjectEmailAddressDomain
						faroProjectEmailAddressDomain) {

					return faroProjectEmailAddressDomain.
						getFaroProjectEmailAddressDomainId();
				}

				@Override
				public Class<Long> getAttributeClass() {
					return Long.class;
				}

				@Override
				public Class<FaroProjectEmailAddressDomain> getTypeClass() {
					return FaroProjectEmailAddressDomain.class;
				}

			};

}