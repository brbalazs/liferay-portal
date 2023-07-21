/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.lar.bundle.stagedmodeldatahandlerregistryutil;

import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandler;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.xml.Element;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = StagedModelDataHandler.class
)
public class TestStagedModelDataHandler
	implements StagedModelDataHandler<User> {

	public static final String[] CLASS_NAMES = {
		TestStagedModelDataHandler.class.getName()
	};

	@Override
	public void deleteStagedModel(
		String uuid, long groupId, String className, String extraData) {
	}

	@Override
	public void deleteStagedModel(User user) {
	}

	@Override
	public void exportStagedModel(
		PortletDataContext portletDataContext, User user) {
	}

	@Override
	public User fetchMissingReference(String uuid, long groupId) {
		return null;
	}

	@Override
	public User fetchStagedModelByUuidAndGroupId(String uuid, long groupId) {
		return null;
	}

	@Override
	public List<User> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		return null;
	}

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	public String getDisplayName(User user) {
		return null;
	}

	@Override
	public int[] getExportableStatuses() {
		return null;
	}

	@Override
	public Map<String, String> getReferenceAttributes(
		PortletDataContext portletDataContext, User user) {

		return null;
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public void importCompanyStagedModel(
		PortletDataContext portletDataContext, Element element) {
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x)
	 */
	@Deprecated
	@Override
	public void importCompanyStagedModel(
		PortletDataContext portletDataContext, String uuid, long classPK) {
	}

	@Override
	public void importMissingReference(
		PortletDataContext portletDataContext, Element referenceElement) {
	}

	@Override
	public void importMissingReference(
		PortletDataContext portletDataContext, String uuid, long groupId,
		long classPK) {
	}

	@Override
	public void importStagedModel(
		PortletDataContext portletDataContext, User user) {
	}

	@Override
	public void restoreStagedModel(
		PortletDataContext portletDataContext, User user) {
	}

	@Override
	public boolean validateReference(
		PortletDataContext portletDataContext, Element referenceElement) {

		return false;
	}

}