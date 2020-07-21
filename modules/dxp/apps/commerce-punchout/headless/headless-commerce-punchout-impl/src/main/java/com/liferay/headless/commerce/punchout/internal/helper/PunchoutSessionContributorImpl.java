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

package com.liferay.headless.commerce.punchout.internal.helper;

import com.liferay.commerce.punchout.constants.PunchoutConstants;
import com.liferay.headless.commerce.punchout.dto.v1_0.PunchoutSession;
import com.liferay.headless.commerce.punchout.helper.PunchoutContext;
import com.liferay.headless.commerce.punchout.helper.PunchoutSessionContributor;

import java.util.HashMap;

import org.osgi.service.component.annotations.Component;

/**
 * @author Jaclyn Ong
 */
@Component(immediate = true, service = PunchoutSessionContributor.class)
public class PunchoutSessionContributorImpl
	implements PunchoutSessionContributor {

	public HashMap<String, Object> getPunchoutSessionAttributes(
		PunchoutContext punchoutContext) {

		HashMap<String, Object> punchoutSessionAttributes = new HashMap<>();

		PunchoutSession punchoutSession = punchoutContext.getPunchoutSession();

		punchoutSessionAttributes.put(
			PunchoutConstants.PUNCHOUT_RETURN_URL_ATTRIBUTE_NAME,
			punchoutSession.getPunchoutReturnURL());

		return punchoutSessionAttributes;
	}

}