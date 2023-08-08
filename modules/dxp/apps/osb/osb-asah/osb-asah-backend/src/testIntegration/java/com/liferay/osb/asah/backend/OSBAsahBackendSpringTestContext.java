/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend;

import com.liferay.osb.asah.backend.spring.OSBAsahBackendSpringBootApplication;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Alejo Ceballos
 */
@ExtendWith(OSBAsahSpringExtension.class)
@SpringBootTest(classes = OSBAsahBackendSpringBootApplication.class)
public interface OSBAsahBackendSpringTestContext {
}