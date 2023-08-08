/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.model;

import java.util.Date;

/**
 * @author Marcos Martins
 */
public class AnalyticsDeleteMessage {

	public String getClassName() {
		return _className;
	}

	public Long getClassPK() {
		return _classPK;
	}

	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	public void setClassName(String className) {
		_className = className;
	}

	public void setClassPK(Long classPK) {
		_classPK = classPK;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	private String _className;
	private Long _classPK;
	private Date _modifiedDate;

}