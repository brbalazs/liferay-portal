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
public class DataSource {

	public DataSource() {
	}

	public String getAbout() {
		return _about;
	}

	public Author getAuthor() {
		return _author;
	}

	public Details getContactsSyncDetails() {
		return _contactsSyncDetails;
	}

	public Credentials getCredentials() {
		return _credentials;
	}

	public Date getDateCreated() {
		if (_dateCreated == null) {
			return null;
		}

		return new Date(_dateCreated.getTime());
	}

	public Date getDateModified() {
		if (_dateModified == null) {
			return null;
		}

		return new Date(_dateModified.getTime());
	}

	public String getDescription() {
		return _description;
	}

	public String getFaroBackendSecuritySignature() {
		return _faroBackendSecuritySignature;
	}

	public String getId() {
		return _id;
	}

	public ConfigChange getLastConfigChange() {
		return _lastConfigChange;
	}

	public String getName() {
		return _name;
	}

	public Provider getProvider() {
		return _provider;
	}

	public Details getSitesSyncDetails() {
		return _sitesSyncDetails;
	}

	public String getState() {
		return _state;
	}

	public String getStatus() {
		return _status;
	}

	public Event getSubjectOf() {
		return _subjectOf;
	}

	public String getUrl() {
		return _url;
	}

	public String getWorkspaceURL() {
		return _workspaceURL;
	}

	public void setAbout(String about) {
		_about = about;
	}

	public void setAuthor(Author author) {
		_author = author;
	}

	public void setContactsSyncDetails(Details contactsSyncDetails) {
		_contactsSyncDetails = contactsSyncDetails;
	}

	public void setCredentials(Credentials credentials) {
		_credentials = credentials;
	}

	public void setDateCreated(Date dateCreated) {
		if (dateCreated != null) {
			_dateCreated = new Date(dateCreated.getTime());
		}
	}

	public void setDateModified(Date dateModified) {
		if (dateModified != null) {
			_dateModified = new Date(dateModified.getTime());
		}
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setFaroBackendSecuritySignature(
		String faroBackendSecuritySignature) {

		_faroBackendSecuritySignature = faroBackendSecuritySignature;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setLastConfigChange(ConfigChange lastConfigChange) {
		_lastConfigChange = lastConfigChange;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setProvider(Provider provider) {
		_provider = provider;
	}

	public void setSitesSyncDetails(Details sitesSyncDetails) {
		_sitesSyncDetails = sitesSyncDetails;
	}

	public void setState(String state) {
		_state = state;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public void setSubjectOf(Event subjectOf) {
		_subjectOf = subjectOf;
	}

	public void setUrl(String url) {
		_url = url;
	}

	public void setWorkspaceURL(String workspaceURL) {
		_workspaceURL = workspaceURL;
	}

	public static class ConfigChange {

		public Status getStatus() {
			return _status;
		}

		public String getTransactionId() {
			return _transactionId;
		}

		public void setStatus(Status status) {
			_status = status;
		}

		public void setTransactionId(String transactionId) {
			_transactionId = transactionId;
		}

		public enum Status {

			ERROR_RECEIVED, OK_RECEIVED, SENT

		}

		private Status _status;
		private String _transactionId;

	}

	public static class Details {

		public Details() {
		}

		public Boolean isSelected() {
			return _selected;
		}

		public void setSelected(Boolean selected) {
			_selected = selected;
		}

		private Boolean _selected;

	}

	public enum State {

		CONFIGURING, DELETE_ERROR, DISCONNECTED, IN_DELETION, READY

	}

	public enum Status {

		ACTIVE, INACTIVE

	}

	private String _about;
	private Author _author;
	private Details _contactsSyncDetails;
	private Credentials _credentials;
	private Date _dateCreated;
	private Date _dateModified;
	private String _description;
	private String _faroBackendSecuritySignature;
	private String _id;
	private ConfigChange _lastConfigChange;
	private String _name;
	private Provider _provider;
	private Details _sitesSyncDetails;
	private String _state;
	private String _status;
	private Event _subjectOf;
	private String _url;
	private String _workspaceURL;

}