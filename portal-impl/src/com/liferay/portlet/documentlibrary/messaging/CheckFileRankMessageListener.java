/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.documentlibrary.messaging;

import com.liferay.document.library.kernel.service.DLFileRankLocalServiceUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;

/**
 * @author     Alexander Chow
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class CheckFileRankMessageListener extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		DLFileRankLocalServiceUtil.checkFileRanks();
	}

}