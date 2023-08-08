/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.findbugs.SuppressFBWarnings;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Inácio Nery
 */
@SuppressFBWarnings("NP_BOOLEAN_RETURN_NULL")
@Table
public class DataSource implements Persistable<Long> {

	public DataSource() {
	}

	public DataSource(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public DataSource(String name) {
		_name = name;
	}

	public void addDataSourceOrganization(
		DataSourceOrganization dataSourceOrganization) {

		if (dataSourceOrganization == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			contactsConfiguration = new ContactsConfiguration();
		}

		contactsConfiguration.addDataSourceOrganization(dataSourceOrganization);

		_provider.setContactsConfiguration(contactsConfiguration);
	}

	public void addDataSourceSite(DataSourceSite dataSourceSite) {
		if (dataSourceSite == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		AnalyticsConfiguration analyticsConfiguration =
			_provider.getAnalyticsConfiguration();

		if (analyticsConfiguration == null) {
			analyticsConfiguration = new AnalyticsConfiguration();
		}

		analyticsConfiguration.addDataSourceSite(dataSourceSite);

		_provider.setAnalyticsConfiguration(analyticsConfiguration);
	}

	public void addDataSourceUserGroup(
		DataSourceUserGroup dataSourceUserGroup) {

		if (dataSourceUserGroup == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			contactsConfiguration = new ContactsConfiguration();
		}

		contactsConfiguration.addDataSourceUserGroup(dataSourceUserGroup);

		_provider.setContactsConfiguration(contactsConfiguration);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DataSource)) {
			return false;
		}

		DataSource dataSource = (DataSource)obj;

		if (Objects.equals(_author, dataSource._author) &&
			Objects.equals(_createDate, dataSource._createDate) &&
			Objects.equals(_credential, dataSource._credential) &&
			Objects.equals(_deletionDate, dataSource._deletionDate) &&
			Objects.equals(_detail, dataSource._detail) &&
			Objects.equals(
				_faroBackendSecuritySignature,
				dataSource._faroBackendSecuritySignature) &&
			Objects.equals(_id, dataSource._id) &&
			Objects.equals(_modifiedDate, dataSource._modifiedDate) &&
			Objects.equals(_name, dataSource._name) &&
			Objects.equals(_provider, dataSource._provider) &&
			Objects.equals(_state, dataSource._state) &&
			Objects.equals(_status, dataSource._status) &&
			Objects.equals(_url, dataSource._url) &&
			Objects.equals(_workspaceURL, dataSource._workspaceURL)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getAccountsSelected() {
		if (_detail == null) {
			return null;
		}

		return _detail.getAccountsSelected();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Long getAuthorId() {
		if (_author == null) {
			return null;
		}

		return _author.getId();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getAuthorName() {
		if (_author == null) {
			return null;
		}

		return _author.getName();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getCommerceChannelsSelected() {
		if (_detail == null) {
			return null;
		}

		return _detail.getCommerceChannelsSelected();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getContactsSelected() {
		if (_detail == null) {
			return null;
		}

		return _detail.getContactsSelected();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonAlias("createDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateCreated")
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getCredentialType() {
		if (_credential == null) {
			return null;
		}

		return _credential.getType();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	@MappedCollection(idColumn = "datasourceid")
	public Set<DataSourceOrganization> getDataSourceOrganizations() {
		if (_provider == null) {
			return null;
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			return null;
		}

		return contactsConfiguration.getDataSourceOrganizations();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	@MappedCollection(idColumn = "datasourceid")
	public Set<DataSourceSite> getDataSourceSites() {
		if (_provider == null) {
			return null;
		}

		AnalyticsConfiguration analyticsConfiguration =
			_provider.getAnalyticsConfiguration();

		if (analyticsConfiguration == null) {
			return null;
		}

		return analyticsConfiguration.getDataSourceSites();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	@MappedCollection(idColumn = "datasourceid")
	public Set<DataSourceUserGroup> getDataSourceUserGroups() {
		if (_provider == null) {
			return null;
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			return null;
		}

		return contactsConfiguration.getDataSourceUserGroups();
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Date getDeletionDate() {
		if (_deletionDate == null) {
			return null;
		}

		return new Date(_deletionDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getEnableAllAccounts() {
		if (_provider == null) {
			return null;
		}

		AccountsConfiguration accountsConfiguration =
			_provider.getAccountsConfiguration();

		if (accountsConfiguration == null) {
			return null;
		}

		return accountsConfiguration.getEnableAllAccounts();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getEnableAllContacts() {
		if (_provider == null) {
			return null;
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			return null;
		}

		return contactsConfiguration.getEnableAllContacts();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getEnableAllLeads() {
		if (_provider == null) {
			return null;
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			return null;
		}

		return contactsConfiguration.getEnableAllLeads();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getEnableAllSites() {
		if (_provider == null) {
			return null;
		}

		AnalyticsConfiguration analyticsConfiguration =
			_provider.getAnalyticsConfiguration();

		if (analyticsConfiguration == null) {
			return null;
		}

		return analyticsConfiguration.getEnableAllSites();
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getFaroBackendSecuritySignature() {
		return _faroBackendSecuritySignature;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getLogin() {
		if (_credential == null) {
			return null;
		}

		return _credential.getLogin();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonAlias("modifiedDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateModified")
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getName() {
		return _name;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthAccessSecret() {
		if (_credential == null) {
			return null;
		}

		return _credential.getOAuthAccessSecret();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthAccessToken() {
		if (_credential == null) {
			return null;
		}

		return _credential.getOAuthAccessToken();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthClientId() {
		if (_credential == null) {
			return null;
		}

		return _credential.getOAuthClientId();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthClientSecret() {
		if (_credential == null) {
			return null;
		}

		return _credential.getOAuthClientSecret();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthConsumerKey() {
		if (_credential == null) {
			return null;
		}

		return _credential.getOAuthConsumerKey();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthConsumerSecret() {
		if (_credential == null) {
			return null;
		}

		return _credential.getOAuthConsumerSecret();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthOwnerEmailAddress() {
		if (_credential == null) {
			return null;
		}

		Credential.OAuthOwner oAuthOwner = _credential.getOAuthOwner();

		if (oAuthOwner == null) {
			return null;
		}

		return oAuthOwner.getEmailAddress();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthOwnerName() {
		if (_credential == null) {
			return null;
		}

		Credential.OAuthOwner oAuthOwner = _credential.getOAuthOwner();

		if (oAuthOwner == null) {
			return null;
		}

		return oAuthOwner.getName();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getOAuthRefreshToken() {
		if (_credential == null) {
			return null;
		}

		return _credential.getOAuthRefreshToken();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getPassword() {
		if (_credential == null) {
			return null;
		}

		return _credential.getPassword();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getPrivateKey() {
		if (_credential == null) {
			return null;
		}

		return _credential.getPrivateKey();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getProviderType() {
		if (_provider == null) {
			return null;
		}

		return _provider.getType();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getPublicKey() {
		if (_credential == null) {
			return null;
		}

		return _credential.getPublicKey();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Boolean getSitesSelected() {
		if (_detail == null) {
			return null;
		}

		return _detail.getSitesSelected();
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getState() {
		return _state;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getStatus() {
		return _status;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getURL() {
		return _url;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getWorkspaceURL() {
		return _workspaceURL;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_author, _createDate, _credential, _deletionDate, _detail,
			_faroBackendSecuritySignature, _id, _modifiedDate, _name, _provider,
			_state, _status, _url, _workspaceURL);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setAccountsSelected(Boolean accountsSelected) {
		if (accountsSelected == null) {
			return;
		}

		if (_detail == null) {
			_detail = new Detail();
		}

		_detail.setAccountsSelected(accountsSelected);
	}

	public void setAuthorId(Long authorId) {
		if (authorId == null) {
			return;
		}

		if (_author == null) {
			_author = new Author();
		}

		_author.setId(authorId);
	}

	public void setAuthorName(String authorName) {
		if (authorName == null) {
			return;
		}

		if (_author == null) {
			_author = new Author();
		}

		_author.setName(authorName);
	}

	public void setCommerceChannelsSelected(Boolean commerceChannelsSelected) {
		if (commerceChannelsSelected == null) {
			return;
		}

		if (_detail == null) {
			_detail = new Detail();
		}

		_detail.setCommerceChannelsSelected(commerceChannelsSelected);
	}

	public void setContactsSelected(Boolean contactsSelected) {
		if (contactsSelected == null) {
			return;
		}

		if (_detail == null) {
			_detail = new Detail();
		}

		_detail.setContactsSelected(contactsSelected);
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setCredentialType(String credentialType) {
		if (credentialType == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setType(credentialType);
	}

	public void setDataSourceOrganizations(
		Set<DataSourceOrganization> dataSourceOrganizations) {

		if ((dataSourceOrganizations == null) ||
			dataSourceOrganizations.isEmpty()) {

			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			contactsConfiguration = new ContactsConfiguration();
		}

		contactsConfiguration.setDataSourceOrganizations(
			dataSourceOrganizations);

		_provider.setContactsConfiguration(contactsConfiguration);
	}

	public void setDataSourceSites(Set<DataSourceSite> dataSourceSites) {
		if ((dataSourceSites == null) || dataSourceSites.isEmpty()) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		AnalyticsConfiguration analyticsConfiguration =
			_provider.getAnalyticsConfiguration();

		if (analyticsConfiguration == null) {
			analyticsConfiguration = new AnalyticsConfiguration();
		}

		analyticsConfiguration.setDataSourceSites(dataSourceSites);

		_provider.setAnalyticsConfiguration(analyticsConfiguration);
	}

	public void setDataSourceUserGroups(
		Set<DataSourceUserGroup> dataSourceUserGroups) {

		if ((dataSourceUserGroups == null) || dataSourceUserGroups.isEmpty()) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			contactsConfiguration = new ContactsConfiguration();
		}

		contactsConfiguration.setDataSourceUserGroups(dataSourceUserGroups);

		_provider.setContactsConfiguration(contactsConfiguration);
	}

	public void setDeletionDate(Date deletionDate) {
		if (deletionDate != null) {
			_deletionDate = new Date(deletionDate.getTime());
		}
	}

	public void setEnableAllAccounts(Boolean enableAllAccounts) {
		if (enableAllAccounts == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		AccountsConfiguration accountsConfiguration =
			_provider.getAccountsConfiguration();

		if (accountsConfiguration == null) {
			accountsConfiguration = new AccountsConfiguration();
		}

		accountsConfiguration.setEnableAllAccounts(enableAllAccounts);

		_provider.setAccountsConfiguration(accountsConfiguration);
	}

	public void setEnableAllContacts(Boolean enableAllContacts) {
		if (enableAllContacts == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			contactsConfiguration = new ContactsConfiguration();
		}

		contactsConfiguration.setEnableAllContacts(enableAllContacts);

		_provider.setContactsConfiguration(contactsConfiguration);
	}

	public void setEnableAllLeads(Boolean enableAllLeads) {
		if (enableAllLeads == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		ContactsConfiguration contactsConfiguration =
			_provider.getContactsConfiguration();

		if (contactsConfiguration == null) {
			contactsConfiguration = new ContactsConfiguration();
		}

		contactsConfiguration.setEnableAllLeads(enableAllLeads);

		_provider.setContactsConfiguration(contactsConfiguration);
	}

	public void setEnableAllSites(Boolean enableAllSites) {
		if (enableAllSites == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		AnalyticsConfiguration analyticsConfiguration =
			_provider.getAnalyticsConfiguration();

		if (analyticsConfiguration == null) {
			analyticsConfiguration = new AnalyticsConfiguration();
		}

		analyticsConfiguration.setEnableAllSites(enableAllSites);

		_provider.setAnalyticsConfiguration(analyticsConfiguration);
	}

	public void setFaroBackendSecuritySignature(
		String faroBackendSecuritySignature) {

		_faroBackendSecuritySignature = faroBackendSecuritySignature;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setLogin(String login) {
		if (login == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setLogin(login);
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setOAuthAccessSecret(String oAuthAccessSecret) {
		if (oAuthAccessSecret == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setOAuthAccessSecret(oAuthAccessSecret);
	}

	public void setOAuthAccessToken(String oAuthAccessToken) {
		if (oAuthAccessToken == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setOAuthAccessToken(oAuthAccessToken);
	}

	public void setOAuthClientId(String oAuthClientId) {
		if (oAuthClientId == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setOAuthClientId(oAuthClientId);
	}

	public void setOAuthClientSecret(String oAuthClientSecret) {
		if (oAuthClientSecret == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setOAuthClientSecret(oAuthClientSecret);
	}

	public void setOAuthConsumerKey(String oAuthConsumerKey) {
		if (oAuthConsumerKey == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setOAuthConsumerKey(oAuthConsumerKey);
	}

	public void setOAuthConsumerSecret(String oAuthConsumerSecret) {
		if (oAuthConsumerSecret == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setOAuthConsumerSecret(oAuthConsumerSecret);
	}

	public void setOAuthOwnerEmailAddress(
		String oAuthOwnerOAuthOwnerEmailAddress) {

		if (oAuthOwnerOAuthOwnerEmailAddress == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		Credential.OAuthOwner oAuthOwner = _credential.getOAuthOwner();

		if (oAuthOwner == null) {
			oAuthOwner = new Credential.OAuthOwner();
		}

		oAuthOwner.setEmailAddress(oAuthOwnerOAuthOwnerEmailAddress);

		_credential.setOAuthOwner(oAuthOwner);
	}

	public void setOAuthOwnerName(String oAuthOwnerOAuthOwnerName) {
		if (oAuthOwnerOAuthOwnerName == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		Credential.OAuthOwner oAuthOwner = _credential.getOAuthOwner();

		if (oAuthOwner == null) {
			oAuthOwner = new Credential.OAuthOwner();
		}

		oAuthOwner.setName(oAuthOwnerOAuthOwnerName);

		_credential.setOAuthOwner(oAuthOwner);
	}

	public void setOAuthRefreshToken(String oAuthRefreshToken) {
		if (oAuthRefreshToken == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setOAuthRefreshToken(oAuthRefreshToken);
	}

	public void setPassword(String password) {
		if (password == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setPassword(password);
	}

	public void setPrivateKey(String privateKey) {
		if (privateKey == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setPrivateKey(privateKey);
	}

	public void setProvider(Provider provider) {
		_provider = provider;
	}

	public void setProviderType(String providerType) {
		if (providerType == null) {
			return;
		}

		if (_provider == null) {
			_provider = new Provider();
		}

		_provider.setType(providerType);
	}

	public void setPublicKey(String publicKey) {
		if (publicKey == null) {
			return;
		}

		if (_credential == null) {
			_credential = new Credential();
		}

		_credential.setPublicKey(publicKey);
	}

	public void setSitesSelected(Boolean sitesSelected) {
		if (sitesSelected == null) {
			return;
		}

		if (_detail == null) {
			_detail = new Detail();
		}

		_detail.setSitesSelected(sitesSelected);
	}

	public void setState(String state) {
		_state = state;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public void setURL(String url) {
		_url = url;
	}

	public void setWorkspaceURL(String workspaceURL) {
		_workspaceURL = workspaceURL;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class AnalyticsConfiguration {

		public void addDataSourceSite(DataSourceSite dataSourceSite) {
			if (_dataSourceSites == null) {
				_dataSourceSites = new HashSet<>();
			}

			_dataSourceSites.add(dataSourceSite);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof AnalyticsConfiguration)) {
				return false;
			}

			AnalyticsConfiguration analyticsConfiguration =
				(AnalyticsConfiguration)obj;

			if (Objects.equals(
					_dataSourceSites,
					analyticsConfiguration._dataSourceSites) &&
				Objects.equals(
					_enableAllSites, analyticsConfiguration._enableAllSites)) {

				return true;
			}

			return false;
		}

		@JsonProperty("sites")
		public Set<DataSourceSite> getDataSourceSites() {
			return _dataSourceSites;
		}

		@JsonProperty("enableAllSites")
		public Boolean getEnableAllSites() {
			return _enableAllSites;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_dataSourceSites, _enableAllSites);
		}

		public void setDataSourceSites(Set<DataSourceSite> dataSourceSites) {
			_dataSourceSites = dataSourceSites;
		}

		public void setEnableAllSites(Boolean enableAllSites) {
			_enableAllSites = enableAllSites;
		}

		private Set<DataSourceSite> _dataSourceSites;
		private Boolean _enableAllSites;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ContactsConfiguration {

		public void addDataSourceOrganization(
			DataSourceOrganization dataSourceOrganization) {

			if (_dataSourceOrganizations == null) {
				_dataSourceOrganizations = new HashSet<>();
			}

			_dataSourceOrganizations.add(dataSourceOrganization);
		}

		public void addDataSourceUserGroup(
			DataSourceUserGroup dataSourceUserGroup) {

			if (_dataSourceUserGroups == null) {
				_dataSourceUserGroups = new HashSet<>();
			}

			_dataSourceUserGroups.add(dataSourceUserGroup);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof ContactsConfiguration)) {
				return false;
			}

			ContactsConfiguration contactsConfiguration =
				(ContactsConfiguration)obj;

			if (Objects.equals(
					_dataSourceOrganizations,
					contactsConfiguration._dataSourceOrganizations) &&
				Objects.equals(
					_dataSourceUserGroups,
					contactsConfiguration._dataSourceUserGroups) &&
				Objects.equals(
					_enableAllContacts,
					contactsConfiguration._enableAllContacts) &&
				Objects.equals(
					_enableAllLeads, contactsConfiguration._enableAllLeads)) {

				return true;
			}

			return false;
		}

		@JsonProperty("organizations")
		public Set<DataSourceOrganization> getDataSourceOrganizations() {
			return _dataSourceOrganizations;
		}

		@JsonProperty("userGroups")
		public Set<DataSourceUserGroup> getDataSourceUserGroups() {
			return _dataSourceUserGroups;
		}

		public Boolean getEnableAllContacts() {
			return _enableAllContacts;
		}

		public Boolean getEnableAllLeads() {
			return _enableAllLeads;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_dataSourceOrganizations, _dataSourceUserGroups,
				_enableAllContacts, _enableAllLeads);
		}

		public void setDataSourceOrganizations(
			Set<DataSourceOrganization> dataSourceOrganizations) {

			_dataSourceOrganizations = dataSourceOrganizations;
		}

		public void setDataSourceUserGroups(
			Set<DataSourceUserGroup> dataSourceUserGroups) {

			_dataSourceUserGroups = dataSourceUserGroups;
		}

		public void setEnableAllContacts(Boolean enableAllContacts) {
			_enableAllContacts = enableAllContacts;
		}

		public void setEnableAllLeads(Boolean enableAllLeads) {
			_enableAllLeads = enableAllLeads;
		}

		private Set<DataSourceOrganization> _dataSourceOrganizations;
		private Set<DataSourceUserGroup> _dataSourceUserGroups;
		private Boolean _enableAllContacts;
		private Boolean _enableAllLeads;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Provider {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Provider)) {
				return false;
			}

			Provider provider = (Provider)obj;

			if (Objects.equals(
					_accountsConfiguration, provider._accountsConfiguration) &&
				Objects.equals(
					_analyticsConfiguration,
					provider._analyticsConfiguration) &&
				Objects.equals(
					_contactsConfiguration, provider._contactsConfiguration) &&
				Objects.equals(_type, provider._type)) {

				return true;
			}

			return false;
		}

		public AccountsConfiguration getAccountsConfiguration() {
			return _accountsConfiguration;
		}

		public AnalyticsConfiguration getAnalyticsConfiguration() {
			return _analyticsConfiguration;
		}

		public ContactsConfiguration getContactsConfiguration() {
			return _contactsConfiguration;
		}

		public String getType() {
			return _type;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_accountsConfiguration, _analyticsConfiguration,
				_contactsConfiguration, _type);
		}

		public void setAccountsConfiguration(
			AccountsConfiguration accountsConfiguration) {

			_accountsConfiguration = accountsConfiguration;
		}

		public void setAnalyticsConfiguration(
			AnalyticsConfiguration analyticsConfiguration) {

			_analyticsConfiguration = analyticsConfiguration;
		}

		public void setContactsConfiguration(
			ContactsConfiguration contactsConfiguration) {

			_contactsConfiguration = contactsConfiguration;
		}

		public void setType(String type) {
			_type = type;
		}

		private AccountsConfiguration _accountsConfiguration;
		private AnalyticsConfiguration _analyticsConfiguration;
		private ContactsConfiguration _contactsConfiguration;
		private String _type;

	}

	@JsonProperty("author")
	protected Author getAuthor() {
		return _author;
	}

	@JsonProperty("credentials")
	protected Credential getCredential() {
		return _credential;
	}

	@JsonProperty("details")
	protected Detail getDetail() {
		return _detail;
	}

	@JsonProperty("provider")
	protected Provider getProvider() {
		return _provider;
	}

	protected void setAuthor(Author author) {
		_author = author;
	}

	@JsonProperty("credentials")
	protected void setCredential(Credential credential) {
		_credential = credential;
	}

	protected void setDetail(Detail detail) {
		_detail = detail;
	}

	@Transient
	private Author _author;

	@Transient
	private Date _createDate;

	@Transient
	private Credential _credential;

	@Transient
	private Date _deletionDate;

	@Transient
	private Detail _detail;

	@Transient
	private String _faroBackendSecuritySignature;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private Date _modifiedDate;

	@Transient
	private String _name;

	@Transient
	private Provider _provider;

	@Transient
	private String _state;

	@Transient
	private String _status;

	@Transient
	private String _url;

	@Transient
	private String _workspaceURL;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class AccountsConfiguration {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof AccountsConfiguration)) {
				return false;
			}

			AccountsConfiguration accountsConfiguration =
				(AccountsConfiguration)obj;

			return Objects.equals(
				_enableAllAccounts, accountsConfiguration._enableAllAccounts);
		}

		@JsonProperty("enableAllAccounts")
		public Boolean getEnableAllAccounts() {
			return _enableAllAccounts;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_enableAllAccounts);
		}

		public void setEnableAllAccounts(Boolean enableAllAccounts) {
			_enableAllAccounts = enableAllAccounts;
		}

		@Transient
		private Boolean _enableAllAccounts;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class Author {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Author)) {
				return false;
			}

			Author author = (Author)obj;

			if (Objects.equals(_id, author._id) &&
				Objects.equals(_name, author._name)) {

				return true;
			}

			return false;
		}

		@JsonSerialize(using = ToStringSerializer.class)
		public Long getId() {
			return _id;
		}

		public String getName() {
			return _name;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_id, _name);
		}

		public void setId(Long id) {
			_id = id;
		}

		public void setName(String name) {
			_name = name;
		}

		private Long _id;
		private String _name;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class Credential {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Credential)) {
				return false;
			}

			Credential credential = (Credential)obj;

			if (Objects.equals(_login, credential._login) &&
				Objects.equals(
					_oAuthAccessSecret, credential._oAuthAccessSecret) &&
				Objects.equals(
					_oAuthAccessToken, credential._oAuthAccessToken) &&
				Objects.equals(_oAuthClientId, credential._oAuthClientId) &&
				Objects.equals(
					_oAuthClientSecret, credential._oAuthClientSecret) &&
				Objects.equals(
					_oAuthConsumerKey, credential._oAuthConsumerKey) &&
				Objects.equals(
					_oAuthConsumerSecret, credential._oAuthConsumerSecret) &&
				Objects.equals(_oAuthOwner, credential._oAuthOwner) &&
				Objects.equals(
					_oAuthRefreshToken, credential._oAuthRefreshToken) &&
				Objects.equals(_password, credential._password) &&
				Objects.equals(_privateKey, credential._privateKey) &&
				Objects.equals(_publicKey, credential._publicKey) &&
				Objects.equals(_type, credential._type)) {

				return true;
			}

			return false;
		}

		public String getLogin() {
			return _login;
		}

		@JsonProperty("oAuthAccessSecret")
		public String getOAuthAccessSecret() {
			return _oAuthAccessSecret;
		}

		@JsonProperty("oAuthAccessToken")
		public String getOAuthAccessToken() {
			return _oAuthAccessToken;
		}

		@JsonProperty("oAuthClientId")
		public String getOAuthClientId() {
			return _oAuthClientId;
		}

		@JsonProperty("oAuthClientSecret")
		public String getOAuthClientSecret() {
			return _oAuthClientSecret;
		}

		@JsonProperty("oAuthConsumerKey")
		public String getOAuthConsumerKey() {
			return _oAuthConsumerKey;
		}

		@JsonProperty("oAuthConsumerSecret")
		public String getOAuthConsumerSecret() {
			return _oAuthConsumerSecret;
		}

		@JsonProperty("oAuthOwner")
		public OAuthOwner getOAuthOwner() {
			return _oAuthOwner;
		}

		@JsonProperty("oAuthRefreshToken")
		public String getOAuthRefreshToken() {
			return _oAuthRefreshToken;
		}

		public String getPassword() {
			return _password;
		}

		public String getPrivateKey() {
			return _privateKey;
		}

		public String getPublicKey() {
			return _publicKey;
		}

		public String getType() {
			return _type;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_login, _oAuthAccessSecret, _oAuthAccessToken, _oAuthClientId,
				_oAuthClientSecret, _oAuthConsumerKey, _oAuthConsumerSecret,
				_oAuthOwner, _oAuthRefreshToken, _password, _privateKey,
				_publicKey, _type);
		}

		public void setLogin(String login) {
			_login = login;
		}

		public void setOAuthAccessSecret(String oAuthAccessSecret) {
			_oAuthAccessSecret = oAuthAccessSecret;
		}

		public void setOAuthAccessToken(String oAuthAccessToken) {
			_oAuthAccessToken = oAuthAccessToken;
		}

		public void setOAuthClientId(String oAuthClientId) {
			_oAuthClientId = oAuthClientId;
		}

		public void setOAuthClientSecret(String oAuthClientSecret) {
			_oAuthClientSecret = oAuthClientSecret;
		}

		public void setOAuthConsumerKey(String oAuthConsumerKey) {
			_oAuthConsumerKey = oAuthConsumerKey;
		}

		public void setOAuthConsumerSecret(String oAuthConsumerSecret) {
			_oAuthConsumerSecret = oAuthConsumerSecret;
		}

		public void setOAuthOwner(OAuthOwner oAuthOwner) {
			_oAuthOwner = oAuthOwner;
		}

		public void setOAuthRefreshToken(String oAuthRefreshToken) {
			_oAuthRefreshToken = oAuthRefreshToken;
		}

		public void setPassword(String password) {
			_password = password;
		}

		public void setPrivateKey(String privateKey) {
			_privateKey = privateKey;
		}

		public void setPublicKey(String publicKey) {
			_publicKey = publicKey;
		}

		public void setType(String type) {
			_type = type;
		}

		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class OAuthOwner {

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}

				if (!(obj instanceof OAuthOwner)) {
					return false;
				}

				OAuthOwner oAuthOwner = (OAuthOwner)obj;

				if (Objects.equals(_emailAddress, oAuthOwner._emailAddress) &&
					Objects.equals(_name, oAuthOwner._name)) {

					return true;
				}

				return false;
			}

			public String getEmailAddress() {
				return _emailAddress;
			}

			public String getName() {
				return _name;
			}

			@Override
			public int hashCode() {
				return Objects.hash(_emailAddress, _name);
			}

			public void setEmailAddress(String emailAddress) {
				_emailAddress = emailAddress;
			}

			public void setName(String name) {
				_name = name;
			}

			private String _emailAddress;
			private String _name;

		}

		private String _login;
		private String _oAuthAccessSecret;
		private String _oAuthAccessToken;
		private String _oAuthClientId;
		private String _oAuthClientSecret;
		private String _oAuthConsumerKey;
		private String _oAuthConsumerSecret;
		private OAuthOwner _oAuthOwner;
		private String _oAuthRefreshToken;
		private String _password;
		private String _privateKey;
		private String _publicKey;
		private String _type;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class Detail {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Detail)) {
				return false;
			}

			Detail detail = (Detail)obj;

			if (Objects.equals(_accountsSelected, detail._accountsSelected) &&
				Objects.equals(
					_commerceChannelsSelected,
					detail._commerceChannelsSelected) &&
				Objects.equals(_contactsSelected, detail._contactsSelected) &&
				Objects.equals(_sitesSelected, detail._sitesSelected)) {

				return true;
			}

			return false;
		}

		@JsonProperty("accountsSelected")
		public Boolean getAccountsSelected() {
			return _accountsSelected;
		}

		@JsonProperty("commerceChannelsSelected")
		public Boolean getCommerceChannelsSelected() {
			return _commerceChannelsSelected;
		}

		@JsonProperty("contactsSelected")
		public Boolean getContactsSelected() {
			return _contactsSelected;
		}

		@JsonProperty("sitesSelected")
		public Boolean getSitesSelected() {
			return _sitesSelected;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_accountsSelected, _commerceChannelsSelected, _contactsSelected,
				_sitesSelected);
		}

		public void setAccountsSelected(Boolean accountsSelected) {
			_accountsSelected = accountsSelected;
		}

		public void setCommerceChannelsSelected(
			Boolean commerceChannelsSelected) {

			_commerceChannelsSelected = commerceChannelsSelected;
		}

		public void setContactsSelected(Boolean contactsSelected) {
			_contactsSelected = contactsSelected;
		}

		public void setSitesSelected(Boolean sitesSelected) {
			_sitesSelected = sitesSelected;
		}

		private Boolean _accountsSelected;
		private Boolean _commerceChannelsSelected;
		private Boolean _contactsSelected;
		private Boolean _sitesSelected;

	}

}