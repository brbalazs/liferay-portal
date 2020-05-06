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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Matthew Kong
 */
public class DataSourceField {

	public DataSourceField() {
	}

	public DataSourceField(String name) {
		_name = name;
	}

	public DataSourceField(String name, List<String> values) {
		_name = name;
		_values = values;
	}

	public Date getDateCreated() {
		return _dateCreated;
	}

	public Date getDateModified() {
		return _dateModified;
	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public List<String> getValues() {
		return _values;
	}

	public void setDateCreated(Date dateCreated) {
		_dateCreated = dateCreated;
	}

	public void setDateModified(Date dateModified) {
		_dateModified = dateModified;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setValues(List<String> values) {
		_values = values;
	}

	private Date _dateCreated;
	private Date _dateModified;
	private String _id;
	private String _name;
	private List<String> _values = new ArrayList<>();

}