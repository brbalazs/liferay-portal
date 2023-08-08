/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.bot.nanite;

import com.liferay.osb.asah.dataflow.emulator.spring.OSBAsahDataflowEmulatorSpringBootApplication;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Marcellus Tavares
 */
@ExtendWith(OSBAsahSpringExtension.class)
@SpringBootTest(classes = OSBAsahDataflowEmulatorSpringBootApplication.class)
public interface OSBAsahDataflowEmulatorSpringTestContext {
}