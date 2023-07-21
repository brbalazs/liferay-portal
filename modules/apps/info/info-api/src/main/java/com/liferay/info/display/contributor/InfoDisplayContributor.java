/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.info.display.contributor;

import com.liferay.asset.kernel.model.ClassType;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Jürgen Kappler
 */
@ProviderType
public interface InfoDisplayContributor<T> {

	public String getClassName();

	public List<InfoDisplayField> getClassTypeInfoDisplayFields(
			long classTypeId, Locale locale)
		throws PortalException;

	public List<ClassType> getClassTypes(long groupId, Locale locale)
		throws PortalException;

	public Set<InfoDisplayField> getInfoDisplayFields(
			long classTypeId, Locale locale)
		throws PortalException;

	public Map<String, Object> getInfoDisplayFieldsValues(T t, Locale locale)
		throws PortalException;

	public Object getInfoDisplayFieldValue(T t, String fieldName, Locale locale)
		throws PortalException;

	public InfoDisplayObjectProvider getInfoDisplayObjectProvider(long classPK)
		throws PortalException;

	public InfoDisplayObjectProvider<T> getInfoDisplayObjectProvider(
			long groupId, String urlTitle)
		throws PortalException;

	public String getInfoURLSeparator();

	public String getLabel(Locale locale);

	public default Map<String, Object> getVersionInfoDisplayFieldsValues(
			T t, long versionClassPK, Locale locale)
		throws PortalException {

		return getInfoDisplayFieldsValues(t, locale);
	}

}