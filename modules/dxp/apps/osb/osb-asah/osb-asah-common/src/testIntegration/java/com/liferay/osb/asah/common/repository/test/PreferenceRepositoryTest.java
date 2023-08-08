/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.repository.PreferenceRepository;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;

import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Inácio Nery
 */
@Import(JDBCTestConfiguration.class)
public class PreferenceRepositoryTest
	extends BaseRepositoryTestCase<Preference, String> {

	@BeforeEach
	public void setUp() {
		Preference preference = new Preference("id1", "value1");

		preference.setIsNew(Boolean.TRUE);

		setUpRepository(preference);

		preference.setIsNew(Boolean.FALSE);
	}

	@Override
	protected PagingAndSortingRepository<Preference, String>
		getPagingAndSortingRepository() {

		return _preferenceRepository;
	}

	@Autowired
	private PreferenceRepository _preferenceRepository;

}