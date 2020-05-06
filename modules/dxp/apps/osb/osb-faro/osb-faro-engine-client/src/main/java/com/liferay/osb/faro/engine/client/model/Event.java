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

package com.liferay.osb.faro.engine.client.model;

import java.util.Date;

/**
 * @author Matthew Kong
 */
public class Event {

	public Event() {
	}

	public Date getEndDate() {
		return _endDate;
	}

	public PostalAddress getLocation() {
		return _location;
	}

	public String getName() {
		return _name;
	}

	public String getSameAs() {
		return _sameAs;
	}

	public Date getStartDate() {
		return _startDate;
	}

	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}

	public void setLocation(PostalAddress location) {
		_location = location;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setSameAs(String sameAs) {
		_sameAs = sameAs;
	}

	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	private Date _endDate;
	private PostalAddress _location;
	private String _name;
	private String _sameAs;
	private Date _startDate;

}