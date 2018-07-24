/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.apio.identifiers;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Rodrigo Guedes de Souza
 */
@ProviderType
public interface ClassPKExternalReferenceCode {

	public static ClassPKExternalReferenceCode create(
		long classPK, String externalReferenceCode) {

		if ((classPK == 0) && (externalReferenceCode == null)) {
			return null;
		}

		return new ClassPKExternalReferenceCode() {

			@Override
			public long getClassPK() {
				return classPK;
			}

			@Override
			public String getExternalReferenceCode() {
				return externalReferenceCode;
			}

		};
	}

	public long getClassPK();

	public String getExternalReferenceCode();

}