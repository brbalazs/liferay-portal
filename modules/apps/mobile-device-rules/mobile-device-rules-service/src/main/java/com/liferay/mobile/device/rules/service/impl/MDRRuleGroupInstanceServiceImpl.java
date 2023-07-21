/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.mobile.device.rules.service.impl;

import com.liferay.mobile.device.rules.constants.MDRConstants;
import com.liferay.mobile.device.rules.model.MDRRuleGroupInstance;
import com.liferay.mobile.device.rules.service.base.MDRRuleGroupInstanceServiceBaseImpl;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionFactory;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.util.List;

/**
 * @author Edward C. Han
 */
public class MDRRuleGroupInstanceServiceImpl
	extends MDRRuleGroupInstanceServiceBaseImpl {

	@Override
	public MDRRuleGroupInstance addRuleGroupInstance(
			long groupId, String className, long classPK, long ruleGroupId,
			int priority, ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId,
			ActionKeys.ADD_RULE_GROUP_INSTANCE);

		return mdrRuleGroupInstanceLocalService.addRuleGroupInstance(
			groupId, className, classPK, ruleGroupId, priority, serviceContext);
	}

	@Override
	public MDRRuleGroupInstance addRuleGroupInstance(
			long groupId, String className, long classPK, long ruleGroupId,
			ServiceContext serviceContext)
		throws PortalException {

		_portletResourcePermission.check(
			getPermissionChecker(), groupId,
			ActionKeys.ADD_RULE_GROUP_INSTANCE);

		return mdrRuleGroupInstanceLocalService.addRuleGroupInstance(
			groupId, className, classPK, ruleGroupId, serviceContext);
	}

	@Override
	public void deleteRuleGroupInstance(long ruleGroupInstanceId)
		throws PortalException {

		MDRRuleGroupInstance ruleGroupInstance =
			mdrRuleGroupInstancePersistence.findByPrimaryKey(
				ruleGroupInstanceId);

		_mdrRuleGroupInstanceModelResourcePermission.check(
			getPermissionChecker(), ruleGroupInstance, ActionKeys.DELETE);

		mdrRuleGroupInstanceLocalService.deleteRuleGroupInstance(
			ruleGroupInstance);
	}

	@Override
	public List<MDRRuleGroupInstance> getRuleGroupInstances(
		String className, long classPK, int start, int end,
		OrderByComparator<MDRRuleGroupInstance> orderByComparator) {

		long groupId = getGroupId(className, classPK);
		long classNameId = classNameLocalService.getClassNameId(className);

		return mdrRuleGroupInstancePersistence.filterFindByG_C_C(
			groupId, classNameId, classPK, start, end, orderByComparator);
	}

	@Override
	public int getRuleGroupInstancesCount(String className, long classPK) {
		long groupId = getGroupId(className, classPK);
		long classNameId = classNameLocalService.getClassNameId(className);

		return mdrRuleGroupInstancePersistence.filterCountByG_C_C(
			groupId, classNameId, classPK);
	}

	@Override
	public MDRRuleGroupInstance updateRuleGroupInstance(
			long ruleGroupInstanceId, int priority)
		throws PortalException {

		MDRRuleGroupInstance ruleGroupInstance =
			mdrRuleGroupInstancePersistence.findByPrimaryKey(
				ruleGroupInstanceId);

		_mdrRuleGroupInstanceModelResourcePermission.check(
			getPermissionChecker(), ruleGroupInstance.getRuleGroupInstanceId(),
			ActionKeys.UPDATE);

		return mdrRuleGroupInstanceLocalService.updateRuleGroupInstance(
			ruleGroupInstanceId, priority);
	}

	protected long getGroupId(String className, long classPK) {
		long groupId = 0;

		if (className.equals(Layout.class.getName())) {
			Layout layout = layoutLocalService.fetchLayout(classPK);

			if (layout != null) {
				groupId = layout.getGroupId();
			}
		}
		else if (className.equals(LayoutSet.class.getName())) {
			LayoutSet layoutSet = layoutSetLocalService.fetchLayoutSet(classPK);

			if (layoutSet != null) {
				groupId = layoutSet.getGroupId();
			}
		}

		return groupId;
	}

	private static volatile ModelResourcePermission<MDRRuleGroupInstance>
		_mdrRuleGroupInstanceModelResourcePermission =
			ModelResourcePermissionFactory.getInstance(
				MDRRuleGroupInstanceServiceImpl.class,
				"_mdrRuleGroupInstanceModelResourcePermission",
				MDRRuleGroupInstance.class);
	private static volatile PortletResourcePermission
		_portletResourcePermission =
			PortletResourcePermissionFactory.getInstance(
				MDRRuleGroupInstanceServiceImpl.class,
				"_portletResourcePermission", MDRConstants.RESOURCE_NAME);

}