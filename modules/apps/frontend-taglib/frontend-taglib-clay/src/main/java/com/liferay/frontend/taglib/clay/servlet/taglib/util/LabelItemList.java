/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.frontend.taglib.clay.servlet.taglib.util;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Carlos Lancha
 */
public class LabelItemList extends ArrayList<LabelItem> {

	public void add(Consumer<LabelItem> consumer) {
		LabelItem labelItem = new LabelItem();

		consumer.accept(labelItem);

		add(labelItem);
	}

}