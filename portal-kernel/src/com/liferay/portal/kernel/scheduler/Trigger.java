/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.scheduler;

import aQute.bnd.annotation.ProviderType;

import java.io.Serializable;

import java.util.Date;

/**
 * @author Shuyang Zhou
 */
@ProviderType
public interface Trigger extends Serializable {

	public Date getEndDate();

	public Date getFireDateAfter(Date date);

	public String getGroupName();

	public String getJobName();

	public Date getStartDate();

	public Serializable getWrappedTrigger();

}