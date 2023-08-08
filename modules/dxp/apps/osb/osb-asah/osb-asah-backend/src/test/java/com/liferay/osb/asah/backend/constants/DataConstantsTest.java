/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.constants;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class DataConstantsTest extends BaseBeanTestCase<DataConstants> {

	@Override
	@Test
	public void testEqualsAndHashCode() {
		EqualsVerifier<? extends Object> equalsVerifier =
			EqualsVerifier.forClass(DataConstants.class);

		equalsVerifier.suppress(
			Warning.INHERITED_DIRECTLY_FROM_OBJECT, Warning.NONFINAL_FIELDS,
			Warning.STRICT_INHERITANCE);

		equalsVerifier.verify();
	}

	@Override
	protected DataConstants newInstance() {
		return new DataConstants();
	}

}