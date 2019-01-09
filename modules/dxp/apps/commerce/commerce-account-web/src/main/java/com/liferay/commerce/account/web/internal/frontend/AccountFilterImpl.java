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

package com.liferay.commerce.account.web.internal.frontend;

import com.liferay.commerce.account.util.CommerceSiteType;
import com.liferay.commerce.frontend.DefaultFilterImpl;

/**
 * @author Marco Leo
 * @author Alessio Antonio Rendina
 */
public class AccountFilterImpl extends DefaultFilterImpl {

	public long getAccountId() {
		return _accountId;
	}

	public CommerceSiteType getCommerceSiteType() {
		return _commerceSiteType;
	}

	public void setAccountId(long accountId) {
		_accountId = accountId;
	}

	public void setCommerceSiteType(CommerceSiteType commerceSiteType) {
		_commerceSiteType = commerceSiteType;
	}

	private long _accountId;
	private CommerceSiteType _commerceSiteType;

}