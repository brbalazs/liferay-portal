/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.util;

import java.util.Collection;

/**
 * @author     Brian Wing Shun Chan
 * @author     Shuyang Zhou
 * @deprecated As of Newton (6.2.x), replaced by {@link
 *             com.liferay.portal.kernel.util.UniqueList}
 */
@Deprecated
public class UniqueList<E>
	extends com.liferay.portal.kernel.util.UniqueList<E> {

	public UniqueList() {
	}

	public UniqueList(Collection<E> c) {
		super(c);
	}

	public UniqueList(int initialCapacity) {
		super(initialCapacity);
	}

}