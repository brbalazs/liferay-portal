/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.stream.curator.bot.nanite;

/**
 * @author Brian Wing Shun Chan
 */
public interface Nanite extends Runnable {

	public long getInterval();

	@Override
	public void run();

}