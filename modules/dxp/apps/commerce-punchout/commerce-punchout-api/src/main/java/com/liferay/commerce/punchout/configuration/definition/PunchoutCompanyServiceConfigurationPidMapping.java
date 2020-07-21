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

package com.liferay.commerce.punchout.configuration.definition;

<<<<<<< HEAD:modules/dxp/apps/commerce-punchout/commerce-punchout-api/src/main/java/com/liferay/commerce/punchout/configuration/definition/PunchoutCompanyServiceConfigurationPidMapping.java
import com.liferay.commerce.punchout.configuration.PunchoutConfiguration;
=======
>>>>>>> COMMERCE-4113 SF:modules/dxp/apps/commerce-punchout/commerce-punchout-service/src/main/java/com/liferay/commerce/punchout/configuration/PunchoutCompanyServiceConfigurationPidMapping.java
import com.liferay.commerce.punchout.constants.PunchoutConstants;
import com.liferay.portal.kernel.settings.definition.ConfigurationPidMapping;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jaclyn Ong
 */
@Component(service = ConfigurationPidMapping.class)
public class PunchoutCompanyServiceConfigurationPidMapping
	implements ConfigurationPidMapping {

	@Override
	public Class<?> getConfigurationBeanClass() {
		return PunchoutConfiguration.class;
	}

	@Override
	public String getConfigurationPid() {
		return PunchoutConstants.SERVICE_NAME;
	}

}