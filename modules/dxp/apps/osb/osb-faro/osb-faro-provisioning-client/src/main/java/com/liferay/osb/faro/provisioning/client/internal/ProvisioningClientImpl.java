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

package com.liferay.osb.faro.provisioning.client.internal;

import com.fasterxml.jackson.core.type.TypeReference;

import com.liferay.osb.faro.provisioning.client.BaseProvisioningClient;
import com.liferay.osb.faro.provisioning.client.ProvisioningClient;
import com.liferay.osb.faro.provisioning.client.model.OSBAccountEntry;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.ArrayUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

/**
 * @author Matthew Kong
 */
@Component(immediate = true, service = ProvisioningClient.class)
public class ProvisioningClientImpl
	extends BaseProvisioningClient implements ProvisioningClient {

	@Override
	public void addCorpProjectUsers(String corpProjectUuid, String[] userUuids)
		throws Exception {

		Map<String, String> parameterMap = new HashMap<>();

		parameterMap.put("corpProjectUuid", corpProjectUuid);
		parameterMap.put(
			"userUuids", ArrayUtil.toString(userUuids, StringPool.BLANK));

		post("corpproject/add-corp-project-users", parameterMap);
	}

	@Override
	public void addUserCorpProjectRoles(
			String corpProjectUuid, String[] userUuids, String roleName)
		throws Exception {

		Map<String, String> parameterMap = new HashMap<>();

		parameterMap.put("corpProjectUuid", corpProjectUuid);
		parameterMap.put("roleName", roleName);
		parameterMap.put(
			"userUuids", ArrayUtil.toString(userUuids, StringPool.BLANK));

		post("corpproject/add-user-corp-project-roles", parameterMap);
	}

	@Override
	public void deleteUserCorpProjectRoles(
			String corpProjectUuid, String[] userUuids, String roleName)
		throws Exception {

		Map<String, String> parameterMap = new HashMap<>();

		parameterMap.put("corpProjectUuid", corpProjectUuid);
		parameterMap.put("roleName", roleName);
		parameterMap.put(
			"userUuids", ArrayUtil.toString(userUuids, StringPool.BLANK));

		post("corpproject/delete-user-corp-project-roles", parameterMap);
	}

	@Override
	public List<OSBAccountEntry> getOSBAccountEntries(
		String userUuid, Long[] productEntryIds) {

		Map<String, String> parameterMap = new HashMap<>();

		parameterMap.put(
			"productEntryIds",
			ArrayUtil.toString(productEntryIds, StringPool.BLANK));
		parameterMap.put("userUuid", userUuid);

		List<OSBAccountEntry> osbAccountEntries = get(
			"accountentry/get-account-entries",
			new TypeReference<List<OSBAccountEntry>>() {
			},
			parameterMap);

		if (osbAccountEntries != null) {
			return osbAccountEntries;
		}

		return Collections.emptyList();
	}

	@Override
	public OSBAccountEntry getOSBAccountEntry(String corpProjectUuid) {
		Map<String, String> parameterMap = new HashMap<>();

		parameterMap.put("corpProjectUuid", corpProjectUuid);

		return get(
			"accountentry/get-corp-project-account-entry",
			new TypeReference<OSBAccountEntry>() {
			},
			parameterMap);
	}

	@Override
	public void unsetCorpProjectUsers(
			String corpProjectUuid, String[] userUuids)
		throws Exception {

		Map<String, String> parameterMap = new HashMap<>();

		parameterMap.put("corpProjectUuid", corpProjectUuid);
		parameterMap.put(
			"userUuids", ArrayUtil.toString(userUuids, StringPool.BLANK));

		post("corpproject/unset-corp-project-users", parameterMap);
	}

}