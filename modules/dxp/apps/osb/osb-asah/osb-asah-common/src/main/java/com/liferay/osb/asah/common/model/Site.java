/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author Marcos Martins
 */
public class Site {

	public Site() {
	}

	public Site(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public Long getCounts() {
		return _counts;
	}

	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public String getGroupId() {
		return _groupId;
	}

	public Date getLastModifiedDate() {
		if (_lastModifiedDate == null) {
			return null;
		}

		return new Date(_lastModifiedDate.getTime());
	}

	public void setCounts(Long counts) {
		_counts = counts;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setGroupId(String groupId) {
		_groupId = groupId;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		if (lastModifiedDate != null) {
			_lastModifiedDate = new Date(lastModifiedDate.getTime());
		}
	}

	private Long _counts;
	private Date _createDate;
	private String _groupId;
	private Date _lastModifiedDate;

}