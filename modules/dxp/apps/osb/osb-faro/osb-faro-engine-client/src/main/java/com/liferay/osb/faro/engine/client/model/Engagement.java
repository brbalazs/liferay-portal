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
import java.util.List;

/**
 * @author Matthew Kong
 */
public class Engagement {

	public Engagement() {
	}

	public List<String> getAccountNames() {
		return _accountNames;
	}

	public Date getDateRecorded() {
		return _dateRecorded;
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	public String getId() {
		return _id;
	}

	public List<String> getIndividualSegmentIds() {
		return _individualSegmentIds;
	}

	public String getName() {
		return _name;
	}

	public String getOwnerId() {
		return _ownerId;
	}

	public String getOwnerType() {
		return _ownerType;
	}

	public double getScore() {
		return _score;
	}

	public boolean isCurrentMember() {
		return _currentMember;
	}

	public void setAccountNames(List<String> accountNames) {
		_accountNames = accountNames;
	}

	public void setCurrentMember(boolean currentMember) {
		_currentMember = currentMember;
	}

	public void setDateRecorded(Date dateRecorded) {
		_dateRecorded = dateRecorded;
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIndividualSegmentIds(List<String> individualSegmentIds) {
		_individualSegmentIds = individualSegmentIds;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setOwnerId(String ownerId) {
		_ownerId = ownerId;
	}

	public void setOwnerType(String ownerType) {
		_ownerType = ownerType;
	}

	public void setScore(double score) {
		_score = score;
	}

	private List<String> _accountNames;
	private boolean _currentMember;
	private Date _dateRecorded;
	private String _emailAddress;
	private String _id;
	private List<String> _individualSegmentIds;
	private String _name;
	private String _ownerId;
	private String _ownerType;
	private double _score;

}