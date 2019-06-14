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

package com.liferay.commerce.organization.web.internal.organization.model;

import java.util.List;

/**
 * @author Alessio Antonio Rendina
 */
public class OrganizationList {

	public OrganizationList(List<Organization> organizations, int total) {
		_organizations = organizations;
		_total = total;
		_success = true;
	}

	public OrganizationList(String[] errorMessages) {
		_errorMessages = errorMessages;
		_success = false;
	}

	public String[] getErrorMessages() {
		return _errorMessages;
	}

	public List<Organization> getOrganizations() {
		return _organizations;
	}

	public boolean getSuccess() {
		return _success;
	}

	public int getTotal() {
		return _total;
	}

	public void setErrorMessages(String[] errorMessages) {
		_errorMessages = errorMessages;
	}

	public void setSuccess(boolean success) {
		_success = success;
	}

	private String[] _errorMessages;
	private List<Organization> _organizations;
	private boolean _success;
	private int _total;

}