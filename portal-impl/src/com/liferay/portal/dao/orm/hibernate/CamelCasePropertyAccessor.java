/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.dao.orm.hibernate;

import org.hibernate.PropertyNotFoundException;
import org.hibernate.property.Getter;
import org.hibernate.property.Setter;

/**
 * @author Brian Wing Shun Chan
 */
@SuppressWarnings("rawtypes")
public class CamelCasePropertyAccessor extends LiferayPropertyAccessor {

	@Override
	public Getter getGetter(Class clazz, String propertyName)
		throws PropertyNotFoundException {

		propertyName = fixPropertyName(propertyName);

		return super.getGetter(clazz, propertyName);
	}

	@Override
	public Setter getSetter(Class clazz, String propertyName)
		throws PropertyNotFoundException {

		propertyName = fixPropertyName(propertyName);

		return super.getSetter(clazz, propertyName);
	}

	protected String fixPropertyName(String propertyName) {
		if (propertyName.length() < 3) {
			return propertyName;
		}

		char c0 = propertyName.charAt(0);
		char c1 = propertyName.charAt(1);
		char c2 = propertyName.charAt(2);

		if (Character.isLowerCase(c0) && Character.isUpperCase(c1) &&
			Character.isLowerCase(c2)) {

			return Character.toUpperCase(c0) + propertyName.substring(1);
		}

		return propertyName;
	}

}