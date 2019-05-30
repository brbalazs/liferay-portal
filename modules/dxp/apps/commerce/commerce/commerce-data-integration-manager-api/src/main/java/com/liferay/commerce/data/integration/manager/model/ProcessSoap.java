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

package com.liferay.commerce.data.integration.manager.model;

import aQute.bnd.annotation.ProviderType;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.commerce.data.integration.manager.service.http.ProcessServiceSoap}.
 *
 * @author Marco Leo
 * @generated
 */
@ProviderType
public class ProcessSoap implements Serializable {

	public static ProcessSoap toSoapModel(Process model) {
		ProcessSoap soapModel = new ProcessSoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setProcessId(model.getProcessId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setName(model.getName());
		soapModel.setVersion(model.getVersion());
		soapModel.setClassName(model.getClassName());
		soapModel.setProcessType(model.getProcessType());
		soapModel.setContextPropertiesFileEntryId(
			model.getContextPropertiesFileEntryId());
		soapModel.setSrcArchiveFileEntryId(model.getSrcArchiveFileEntryId());
		soapModel.setContextProperties(model.getContextProperties());

		return soapModel;
	}

	public static ProcessSoap[] toSoapModels(Process[] models) {
		ProcessSoap[] soapModels = new ProcessSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ProcessSoap[][] toSoapModels(Process[][] models) {
		ProcessSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ProcessSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ProcessSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ProcessSoap[] toSoapModels(List<Process> models) {
		List<ProcessSoap> soapModels = new ArrayList<ProcessSoap>(
			models.size());

		for (Process model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ProcessSoap[soapModels.size()]);
	}

	public ProcessSoap() {
	}

	public long getPrimaryKey() {
		return _processId;
	}

	public void setPrimaryKey(long pk) {
		setProcessId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getProcessId() {
		return _processId;
	}

	public void setProcessId(long processId) {
		_processId = processId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getVersion() {
		return _version;
	}

	public void setVersion(String version) {
		_version = version;
	}

	public String getClassName() {
		return _className;
	}

	public void setClassName(String className) {
		_className = className;
	}

	public String getProcessType() {
		return _processType;
	}

	public void setProcessType(String processType) {
		_processType = processType;
	}

	public long getContextPropertiesFileEntryId() {
		return _contextPropertiesFileEntryId;
	}

	public void setContextPropertiesFileEntryId(
		long contextPropertiesFileEntryId) {

		_contextPropertiesFileEntryId = contextPropertiesFileEntryId;
	}

	public long getSrcArchiveFileEntryId() {
		return _srcArchiveFileEntryId;
	}

	public void setSrcArchiveFileEntryId(long srcArchiveFileEntryId) {
		_srcArchiveFileEntryId = srcArchiveFileEntryId;
	}

	public String getContextProperties() {
		return _contextProperties;
	}

	public void setContextProperties(String contextProperties) {
		_contextProperties = contextProperties;
	}

	private String _uuid;
	private long _processId;
	private long _groupId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private String _name;
	private String _version;
	private String _className;
	private String _processType;
	private long _contextPropertiesFileEntryId;
	private long _srcArchiveFileEntryId;
	private String _contextProperties;

}