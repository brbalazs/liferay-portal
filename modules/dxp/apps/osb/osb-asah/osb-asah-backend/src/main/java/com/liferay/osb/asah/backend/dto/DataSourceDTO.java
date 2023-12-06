/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.backend.dto.DataSourceDTO.ProviderDTO.AccountsConfigurationDTO;
import com.liferay.osb.asah.backend.dto.DataSourceDTO.ProviderDTO.AnalyticsConfigurationDTO;
import com.liferay.osb.asah.backend.dto.DataSourceDTO.ProviderDTO.ContactsConfigurationDTO;
import com.liferay.osb.asah.backend.dto.DataSourceDTO.ProviderDTO.DetailDTO;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.DataSourceOrganization;
import com.liferay.osb.asah.common.entity.DataSourceSite;
import com.liferay.osb.asah.common.entity.DataSourceUserGroup;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Inácio Nery
 */
@GraphQLType("DataSource")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("data-sources")
public class DataSourceDTO {

	public DataSourceDTO() {
	}

	public DataSourceDTO(DataSource dataSource) {
		AuthorDTO authorDTO = new AuthorDTO(dataSource);

		if (!authorDTO.isEmpty()) {
			_authorDTO = authorDTO;
		}

		_createDate = dataSource.getCreateDate();

		CredentialDTO credentialDTO = new CredentialDTO(dataSource);

		if (!credentialDTO.isEmpty()) {
			_credentialDTO = credentialDTO;
		}

		_deletionDate = dataSource.getDeletionDate();

		DetailDTO contactsSyncDetailDTO = new DetailDTO(
			dataSource.getContactsSelected());

		if (!contactsSyncDetailDTO.isEmpty()) {
			_contactsSyncDetailDTO = contactsSyncDetailDTO;
		}

		_faroBackendSecuritySignature =
			dataSource.getFaroBackendSecuritySignature();
		_id = StringUtil.get(dataSource.getId(), null);
		_modifiedDate = dataSource.getModifiedDate();
		_name = dataSource.getName();

		ProviderDTO providerDTO = new ProviderDTO(dataSource);

		if (!providerDTO.isEmpty()) {
			_providerDTO = providerDTO;
		}

		DetailDTO sitesSyncDetailDTO = new DetailDTO(
			dataSource.getSitesSelected());

		if (!sitesSyncDetailDTO.isEmpty()) {
			_sitesSyncDetailDTO = sitesSyncDetailDTO;
		}

		_state = dataSource.getState();
		_status = dataSource.getStatus();
		_url = dataSource.getURL();
		_workspaceURL = dataSource.getWorkspaceURL();
	}

	public DataSourceDTO(List<DataSource> dataSources) {
		_dataSourceDTOs = SetUtil.map(dataSources, DataSourceDTO::new);
	}

	@JsonProperty("accountsConfiguration")
	public AccountsConfigurationDTO getAccountsConfigurationDTO() {
		return _accountsConfigurationDTO;
	}

	@JsonProperty("analyticsConfiguration")
	public AnalyticsConfigurationDTO getAnalyticsConfigurationDTO() {
		return _analyticsConfigurationDTO;
	}

	@JsonProperty("author")
	public AuthorDTO getAuthorDTO() {
		return _authorDTO;
	}

	@JsonProperty("channelId")
	public String getChannelId() {
		return _channelId;
	}

	@JsonProperty("contactsConfiguration")
	public ContactsConfigurationDTO getContactsConfigurationDTO() {
		return _contactsConfigurationDTO;
	}

	@JsonProperty("contactsSyncDetails")
	public DetailDTO getContactsSyncDetailDTO() {
		return _contactsSyncDetailDTO;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@JsonProperty("credentials")
	public CredentialDTO getCredentialDTO() {
		return _credentialDTO;
	}

	@JsonProperty("data-sources")
	public Set<DataSourceDTO> getDataSourceDTOs() {
		return _dataSourceDTOs;
	}

	@JsonProperty("dataSourceId")
	public String getDataSourceId() {
		return _dataSourceId;
	}

	@JsonProperty("dataSourceState")
	public String getDataSourceState() {
		return _dataSourceState;
	}

	@JsonProperty("dataSourceStatus")
	public String getDataSourceStatus() {
		return _dataSourceStatus;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("deletionDate")
	public Date getDeletionDate() {
		if (_deletionDate == null) {
			return null;
		}

		return new Date(_deletionDate.getTime());
	}

	@JsonProperty("existingDataSourceId")
	public String getExistingDataSourceId() {
		return _existingDataSourceId;
	}

	@JsonProperty("faroBackendSecuritySignature")
	public String getFaroBackendSecuritySignature() {
		return _faroBackendSecuritySignature;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@JsonProperty("name")
	public String getName() {
		return _name;
	}

	@JsonProperty("provider")
	public ProviderDTO getProviderDTO() {
		return _providerDTO;
	}

	@JsonProperty("sitesSyncDetails")
	public DetailDTO getSitesSyncDetailDTO() {
		return _sitesSyncDetailDTO;
	}

	@JsonProperty("state")
	public String getState() {
		return _state;
	}

	@JsonProperty("status")
	public String getStatus() {
		return _status;
	}

	@GraphQLProperty("url")
	@JsonProperty("url")
	public String getURL() {
		return _url;
	}

	@JsonProperty("workspaceURL")
	public String getWorkspaceURL() {
		return _workspaceURL;
	}

	public void setAccountsConfigurationDTO(
		AccountsConfigurationDTO accountsConfigurationDTO) {

		_accountsConfigurationDTO = accountsConfigurationDTO;
	}

	public void setAnalyticsConfigurationDTO(
		AnalyticsConfigurationDTO analyticsConfigurationDTO) {

		_analyticsConfigurationDTO = analyticsConfigurationDTO;
	}

	public void setAuthorDTO(AuthorDTO authorDTO) {
		_authorDTO = authorDTO;
	}

	public void setChannelId(String channelId) {
		_channelId = channelId;
	}

	public void setContactsConfigurationDTO(
		ContactsConfigurationDTO contactsConfigurationDTO) {

		_contactsConfigurationDTO = contactsConfigurationDTO;
	}

	public void setContactsSyncDetailDTO(DetailDTO detailDTO) {
		_contactsSyncDetailDTO = detailDTO;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
		else {
			_createDate = null;
		}
	}

	public void setCredentialDTO(CredentialDTO credentialDTO) {
		_credentialDTO = credentialDTO;
	}

	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDataSourceState(String dataSourceState) {
		_dataSourceState = dataSourceState;
	}

	public void setDataSourceStatus(String dataSourceStatus) {
		_dataSourceStatus = dataSourceStatus;
	}

	public void setDeletionDate(Date deletionDate) {
		if (deletionDate != null) {
			_deletionDate = new Date(deletionDate.getTime());
		}
		else {
			_deletionDate = null;
		}
	}

	public void setExistingDataSourceId(String existingDataSourceId) {
		_existingDataSourceId = existingDataSourceId;
	}

	public void setFaroBackendSecuritySignature(
		String faroBackendSecuritySignature) {

		_faroBackendSecuritySignature = faroBackendSecuritySignature;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
		else {
			_modifiedDate = null;
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setProviderDTO(ProviderDTO providerDTO) {
		_providerDTO = providerDTO;
	}

	public void setSitesSyncDetailDTO(DetailDTO detailDTO) {
		_sitesSyncDetailDTO = detailDTO;
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
	public static class AuthorDTO {

		public AuthorDTO() {
		}

		public AuthorDTO(DataSource dataSource) {
			_id = StringUtil.get(dataSource.getAuthorId(), null);
			_name = dataSource.getAuthorName();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof AuthorDTO)) {
				return false;
			}

			AuthorDTO authorDTO = (AuthorDTO)obj;

			if (Objects.equals(_id, authorDTO._id) &&
				Objects.equals(_name, authorDTO._name)) {

				return true;
			}

			return false;
		}

		@JsonProperty("id")
		public String getId() {
			return _id;
		}

		@JsonProperty("name")
		public String getName() {
			return _name;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_id, _name);
		}

		@JsonIgnore
		public boolean isEmpty() {
			return equals(new AuthorDTO());
		}

		public void setId(String id) {
			_id = id;
		}

		public void setName(String name) {
			_name = name;
		}

		private String _id;
		private String _name;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class CredentialDTO {

		public CredentialDTO() {
		}

		public CredentialDTO(DataSource dataSource) {
			_credentialType = dataSource.getCredentialType();
			_login = dataSource.getLogin();
			_oAuthAccessSecret = dataSource.getOAuthAccessSecret();
			_oAuthAccessToken = dataSource.getOAuthAccessToken();
			_oAuthClientId = dataSource.getOAuthClientId();
			_oAuthClientSecret = dataSource.getOAuthClientSecret();
			_oAuthConsumerKey = dataSource.getOAuthConsumerKey();
			_oAuthConsumerSecret = dataSource.getOAuthConsumerSecret();

			OAuthOwnerDTO oAuthOwnerDTO = new OAuthOwnerDTO(dataSource);

			if (!oAuthOwnerDTO.isEmpty()) {
				_oAuthOwnerDTO = oAuthOwnerDTO;
			}

			_oAuthRefreshToken = dataSource.getOAuthRefreshToken();
			_password = dataSource.getPassword();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof CredentialDTO)) {
				return false;
			}

			CredentialDTO credentialDTO = (CredentialDTO)obj;

			if (Objects.equals(
					_credentialType, credentialDTO._credentialType) &&
				Objects.equals(_login, credentialDTO._login) &&
				Objects.equals(
					_oAuthAccessSecret, credentialDTO._oAuthAccessSecret) &&
				Objects.equals(
					_oAuthAccessToken, credentialDTO._oAuthAccessToken) &&
				Objects.equals(_oAuthClientId, credentialDTO._oAuthClientId) &&
				Objects.equals(
					_oAuthClientSecret, credentialDTO._oAuthClientSecret) &&
				Objects.equals(
					_oAuthConsumerKey, credentialDTO._oAuthConsumerKey) &&
				Objects.equals(
					_oAuthConsumerSecret, credentialDTO._oAuthConsumerSecret) &&
				Objects.equals(_oAuthOwnerDTO, credentialDTO._oAuthOwnerDTO) &&
				Objects.equals(
					_oAuthRefreshToken, credentialDTO._oAuthRefreshToken) &&
				Objects.equals(_password, credentialDTO._password) &&
				Objects.equals(_privateKey, credentialDTO._privateKey) &&
				Objects.equals(_publicKey, credentialDTO._publicKey)) {

				return true;
			}

			return false;
		}

		@JsonProperty("type")
		public String getCredentialType() {
			return _credentialType;
		}

		@JsonProperty("login")
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
		public OAuthOwnerDTO getOAuthOwnerDTO() {
			return _oAuthOwnerDTO;
		}

		@JsonProperty("oAuthRefreshToken")
		public String getOAuthRefreshToken() {
			return _oAuthRefreshToken;
		}

		@JsonProperty("password")
		public String getPassword() {
			return _password;
		}

		@JsonProperty("privateKey")
		public String getPrivateKey() {
			return _privateKey;
		}

		@JsonProperty("publicKey")
		public String getPublicKey() {
			return _publicKey;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_credentialType, _login, _oAuthAccessSecret, _oAuthAccessToken,
				_oAuthClientId, _oAuthClientSecret, _oAuthConsumerKey,
				_oAuthConsumerSecret, _oAuthOwnerDTO, _oAuthRefreshToken,
				_password, _privateKey, _publicKey);
		}

		@JsonIgnore
		public boolean isEmpty() {
			return equals(new CredentialDTO());
		}

		public void setCredentialType(String credentialType) {
			_credentialType = credentialType;
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

		public void setOAuthOwnerDTO(OAuthOwnerDTO oAuthOwnerDTO) {
			_oAuthOwnerDTO = oAuthOwnerDTO;
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

		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class OAuthOwnerDTO {

			public OAuthOwnerDTO() {
			}

			public OAuthOwnerDTO(DataSource dataSource) {
				_emailAddress = dataSource.getOAuthOwnerEmailAddress();
				_name = dataSource.getOAuthOwnerName();
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}

				if (!(obj instanceof OAuthOwnerDTO)) {
					return false;
				}

				OAuthOwnerDTO oAuthOwnerDTO = (OAuthOwnerDTO)obj;

				if (Objects.equals(
						_emailAddress, oAuthOwnerDTO._emailAddress) &&
					Objects.equals(_name, oAuthOwnerDTO._name)) {

					return true;
				}

				return false;
			}

			@JsonProperty("emailAddress")
			public String getEmailAddress() {
				return _emailAddress;
			}

			@JsonProperty("name")
			public String getName() {
				return _name;
			}

			@Override
			public int hashCode() {
				return Objects.hash(_emailAddress, _name);
			}

			@JsonIgnore
			public boolean isEmpty() {
				return equals(new OAuthOwnerDTO());
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

		private String _credentialType;
		private String _login;
		private String _oAuthAccessSecret;
		private String _oAuthAccessToken;
		private String _oAuthClientId;
		private String _oAuthClientSecret;
		private String _oAuthConsumerKey;
		private String _oAuthConsumerSecret;
		private OAuthOwnerDTO _oAuthOwnerDTO;
		private String _oAuthRefreshToken;
		private String _password;
		private String _privateKey = "";
		private String _publicKey = "";

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ProviderDTO {

		public ProviderDTO() {
		}

		public ProviderDTO(DataSource dataSource) {
			AccountsConfigurationDTO accountsConfigurationDTO =
				new AccountsConfigurationDTO(dataSource);

			if (!accountsConfigurationDTO.isEmpty()) {
				_accountsConfigurationDTO = accountsConfigurationDTO;
			}

			AnalyticsConfigurationDTO analyticsConfigurationDTO =
				new AnalyticsConfigurationDTO(dataSource);

			if (!analyticsConfigurationDTO.isEmpty()) {
				_analyticsConfigurationDTO = analyticsConfigurationDTO;
			}

			ContactsConfigurationDTO contactsConfigurationDTO =
				new ContactsConfigurationDTO(dataSource);

			if (!contactsConfigurationDTO.isEmpty()) {
				_contactsConfigurationDTO = contactsConfigurationDTO;
			}

			_providerType = dataSource.getProviderType();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof ProviderDTO)) {
				return false;
			}

			ProviderDTO providerDTO = (ProviderDTO)obj;

			if (Objects.equals(
					_accountsConfigurationDTO,
					providerDTO._accountsConfigurationDTO) &&
				Objects.equals(
					_analyticsConfigurationDTO,
					providerDTO._analyticsConfigurationDTO) &&
				Objects.equals(
					_contactsConfigurationDTO,
					providerDTO._contactsConfigurationDTO) &&
				Objects.equals(_providerType, providerDTO._providerType)) {

				return true;
			}

			return false;
		}

		@JsonProperty("accountsConfiguration")
		public AccountsConfigurationDTO getAccountsConfigurationDTO() {
			return _accountsConfigurationDTO;
		}

		@JsonProperty("analyticsConfiguration")
		public AnalyticsConfigurationDTO getAnalyticsConfigurationDTO() {
			return _analyticsConfigurationDTO;
		}

		@JsonProperty("contactsConfiguration")
		public ContactsConfigurationDTO getContactsConfigurationDTO() {
			return _contactsConfigurationDTO;
		}

		@JsonProperty("type")
		public String getProviderType() {
			return _providerType;
		}

		@Override
		public int hashCode() {
			return Objects.hash(
				_accountsConfigurationDTO, _analyticsConfigurationDTO,
				_contactsConfigurationDTO, _providerType);
		}

		@JsonIgnore
		public boolean isEmpty() {
			return equals(new ProviderDTO());
		}

		public void setAccountsConfigurationDTO(
			AccountsConfigurationDTO accountsConfigurationDTO) {

			_accountsConfigurationDTO = accountsConfigurationDTO;
		}

		public void setAnalyticsConfigurationDTO(
			AnalyticsConfigurationDTO analyticsConfigurationDTO) {

			_analyticsConfigurationDTO = analyticsConfigurationDTO;
		}

		public void setContactsConfigurationDTO(
			ContactsConfigurationDTO contactsConfigurationDTO) {

			_contactsConfigurationDTO = contactsConfigurationDTO;
		}

		public void setProviderType(String providerType) {
			_providerType = providerType;
		}

		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class AccountsConfigurationDTO {

			public AccountsConfigurationDTO() {
			}

			public AccountsConfigurationDTO(DataSource dataSource) {
				_enableAllAccounts = dataSource.getEnableAllAccounts();
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}

				if (!(obj instanceof AccountsConfigurationDTO)) {
					return false;
				}

				AccountsConfigurationDTO accountsConfigurationDTO =
					(AccountsConfigurationDTO)obj;

				return Objects.equals(
					_enableAllAccounts,
					accountsConfigurationDTO._enableAllAccounts);
			}

			@JsonProperty("enableAllAccounts")
			public Boolean getEnableAllAccounts() {
				return _enableAllAccounts;
			}

			@Override
			public int hashCode() {
				return Objects.hash(_enableAllAccounts);
			}

			@JsonIgnore
			public boolean isEmpty() {
				return equals(new AccountsConfigurationDTO());
			}

			public void setEnableAllAccounts(Boolean enableAllAccounts) {
				_enableAllAccounts = enableAllAccounts;
			}

			private Boolean _enableAllAccounts;

		}

		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class AnalyticsConfigurationDTO {

			public AnalyticsConfigurationDTO() {
			}

			public AnalyticsConfigurationDTO(DataSource dataSource) {
				_enableAllSites = dataSource.getEnableAllSites();

				List<SiteDTO> siteDTOs = ListUtil.map(
					dataSource.getDataSourceSites(), SiteDTO::new);

				if (!siteDTOs.isEmpty()) {
					_siteDTOs = siteDTOs;
				}
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}

				if (!(obj instanceof AnalyticsConfigurationDTO)) {
					return false;
				}

				AnalyticsConfigurationDTO analyticsConfigurationDTO =
					(AnalyticsConfigurationDTO)obj;

				if (Objects.equals(
						_enableAllSites,
						analyticsConfigurationDTO._enableAllSites) &&
					Objects.equals(
						_siteDTOs, analyticsConfigurationDTO._siteDTOs)) {

					return true;
				}

				return false;
			}

			@JsonProperty("enableAllSites")
			public Boolean getEnableAllSites() {
				return _enableAllSites;
			}

			@JsonProperty("sites")
			public List<SiteDTO> getSiteDTOs() {
				return _siteDTOs;
			}

			@Override
			public int hashCode() {
				return Objects.hash(_enableAllSites, _siteDTOs);
			}

			@JsonIgnore
			public boolean isEmpty() {
				return equals(new AnalyticsConfigurationDTO());
			}

			public void setEnableAllSites(Boolean enableAllSites) {
				_enableAllSites = enableAllSites;
			}

			public void setSiteDTOs(List<SiteDTO> siteDTOs) {
				_siteDTOs = siteDTOs;
			}

			@JsonInclude(JsonInclude.Include.NON_NULL)
			public static class SiteDTO {

				public SiteDTO() {
				}

				public SiteDTO(DataSourceSite dataSourceSite) {
					_enableAllChildren = dataSourceSite.getEnableAllChildren();
					_siteId = StringUtil.get(dataSourceSite.getSiteId(), null);
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj) {
						return true;
					}

					if (!(obj instanceof SiteDTO)) {
						return false;
					}

					SiteDTO siteDTO = (SiteDTO)obj;

					if (Objects.equals(
							_enableAllChildren, siteDTO._enableAllChildren) &&
						Objects.equals(_siteId, siteDTO._siteId)) {

						return true;
					}

					return false;
				}

				@JsonProperty("enableAllChildren")
				public Boolean getEnableAllChildren() {
					return _enableAllChildren;
				}

				@JsonProperty("id")
				public String getSiteId() {
					return _siteId;
				}

				@Override
				public int hashCode() {
					return Objects.hash(_enableAllChildren, _siteId);
				}

				@JsonIgnore
				public boolean isEmpty() {
					return equals(new SiteDTO());
				}

				public void setEnableAllChildren(Boolean enableAllChildren) {
					_enableAllChildren = enableAllChildren;
				}

				public void setSiteId(String id) {
					_siteId = id;
				}

				private Boolean _enableAllChildren;
				private String _siteId;

			}

			private Boolean _enableAllSites;
			private List<SiteDTO> _siteDTOs;

		}

		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class ContactsConfigurationDTO {

			public ContactsConfigurationDTO() {
			}

			public ContactsConfigurationDTO(DataSource dataSource) {
				_enableAllContacts = dataSource.getEnableAllContacts();
				_enableAllLeads = dataSource.getEnableAllLeads();

				List<OrganizationDTO> organizationDTOs = ListUtil.map(
					dataSource.getDataSourceOrganizations(),
					OrganizationDTO::new);

				if (!organizationDTOs.isEmpty()) {
					_organizationDTOs = organizationDTOs;
				}

				List<UserGroupDTO> userGroupDTOs = ListUtil.map(
					dataSource.getDataSourceUserGroups(), UserGroupDTO::new);

				if (!userGroupDTOs.isEmpty()) {
					_userGroupDTOs = userGroupDTOs;
				}
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}

				if (!(obj instanceof ContactsConfigurationDTO)) {
					return false;
				}

				ContactsConfigurationDTO contactsConfigurationDTO =
					(ContactsConfigurationDTO)obj;

				if (Objects.equals(
						_enableAllContacts,
						contactsConfigurationDTO._enableAllContacts) &&
					Objects.equals(
						_enableAllLeads,
						contactsConfigurationDTO._enableAllLeads) &&
					Objects.equals(
						_organizationDTOs,
						contactsConfigurationDTO._organizationDTOs) &&
					Objects.equals(
						_userGroupDTOs,
						contactsConfigurationDTO._userGroupDTOs)) {

					return true;
				}

				return false;
			}

			@JsonProperty("enableAllContacts")
			public Boolean getEnableAllContacts() {
				return _enableAllContacts;
			}

			@JsonProperty("enableAllLeads")
			public Boolean getEnableAllLeads() {
				return _enableAllLeads;
			}

			@JsonProperty("organizations")
			public List<OrganizationDTO> getOrganizationDTOs() {
				return _organizationDTOs;
			}

			@JsonProperty("userGroups")
			public List<UserGroupDTO> getUserGroupDTOs() {
				return _userGroupDTOs;
			}

			@Override
			public int hashCode() {
				return Objects.hash(
					_enableAllContacts, _enableAllLeads, _organizationDTOs,
					_userGroupDTOs);
			}

			@JsonIgnore
			public boolean isEmpty() {
				return equals(new ContactsConfigurationDTO());
			}

			public void setEnableAllContacts(Boolean enableAllContacts) {
				_enableAllContacts = enableAllContacts;
			}

			public void setEnableAllLeads(Boolean enableAllLeads) {
				_enableAllLeads = enableAllLeads;
			}

			public void setOrganizationDTOs(
				List<OrganizationDTO> organizationDTOs) {

				_organizationDTOs = organizationDTOs;
			}

			public void setUserGroupDTOs(List<UserGroupDTO> userGroupDTOs) {
				_userGroupDTOs = userGroupDTOs;
			}

			@JsonInclude(JsonInclude.Include.NON_NULL)
			public static class OrganizationDTO {

				public OrganizationDTO() {
				}

				public OrganizationDTO(
					DataSourceOrganization dataSourceOrganization) {

					_enableAllChildren =
						dataSourceOrganization.getEnableAllChildren();
					_organizationId =
						dataSourceOrganization.getOrganizationId();
					_organizationIds =
						dataSourceOrganization.getOrganizationIds();
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj) {
						return true;
					}

					if (!(obj instanceof OrganizationDTO)) {
						return false;
					}

					OrganizationDTO organizationDTO = (OrganizationDTO)obj;

					if (Objects.equals(
							_enableAllChildren,
							organizationDTO._enableAllChildren) &&
						Objects.equals(
							_organizationId, organizationDTO._organizationId) &&
						Objects.equals(
							_organizationIds,
							organizationDTO._organizationIds)) {

						return true;
					}

					return false;
				}

				@JsonProperty("enableAllChildren")
				public Boolean getEnableAllChildren() {
					return _enableAllChildren;
				}

				@JsonProperty("id")
				public Long getOrganizationId() {
					return _organizationId;
				}

				@JsonProperty("organizationIds")
				public Set<Long> getOrganizationIds() {
					return _organizationIds;
				}

				@Override
				public int hashCode() {
					return Objects.hash(
						_enableAllChildren, _organizationId, _organizationIds);
				}

				@JsonIgnore
				public boolean isEmpty() {
					return equals(new OrganizationDTO());
				}

				public void setEnableAllChildren(Boolean enableAllChildren) {
					_enableAllChildren = enableAllChildren;
				}

				public void setOrganizationId(Long organizationId) {
					_organizationId = organizationId;
				}

				public void setOrganizationIds(Set<Long> organizationIds) {
					_organizationIds = organizationIds;
				}

				private Boolean _enableAllChildren;
				private Long _organizationId;
				private Set<Long> _organizationIds;

			}

			@JsonInclude(JsonInclude.Include.NON_NULL)
			public static class UserGroupDTO {

				public UserGroupDTO() {
				}

				public UserGroupDTO(DataSourceUserGroup dataSourceUserGroup) {
					_enableAllChildren =
						dataSourceUserGroup.getEnableAllChildren();
					_userGroupId = dataSourceUserGroup.getUserGroupId();
					_userGroupIds = dataSourceUserGroup.getUserGroupIds();
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj) {
						return true;
					}

					if (!(obj instanceof UserGroupDTO)) {
						return false;
					}

					UserGroupDTO userGroupDTO = (UserGroupDTO)obj;

					if (Objects.equals(
							_enableAllChildren,
							userGroupDTO._enableAllChildren) &&
						Objects.equals(
							_userGroupId, userGroupDTO._userGroupId) &&
						Objects.equals(
							_userGroupIds, userGroupDTO._userGroupIds)) {

						return true;
					}

					return false;
				}

				@JsonProperty("enableAllChildren")
				public Boolean getEnableAllChildren() {
					return _enableAllChildren;
				}

				@JsonProperty("id")
				public Long getUserGroupId() {
					return _userGroupId;
				}

				@JsonProperty("userGroupIds")
				public Set<Long> getUserGroupIds() {
					return _userGroupIds;
				}

				@Override
				public int hashCode() {
					return Objects.hash(
						_enableAllChildren, _userGroupId, _userGroupIds);
				}

				@JsonIgnore
				public boolean isEmpty() {
					return equals(new UserGroupDTO());
				}

				public void setEnableAllChildren(Boolean enableAllChildren) {
					_enableAllChildren = enableAllChildren;
				}

				public void setUserGroupId(Long userGroupId) {
					_userGroupId = userGroupId;
				}

				public void setUserGroupIds(Set<Long> userGroupIds) {
					_userGroupIds = userGroupIds;
				}

				private Boolean _enableAllChildren;
				private Long _userGroupId;
				private Set<Long> _userGroupIds;

			}

			private Boolean _enableAllContacts;
			private Boolean _enableAllLeads;
			private List<OrganizationDTO> _organizationDTOs;
			private List<UserGroupDTO> _userGroupDTOs;

		}

		@JsonInclude(JsonInclude.Include.NON_NULL)
		public static class DetailDTO {

			public DetailDTO() {
			}

			public DetailDTO(Boolean selected) {
				_selected = selected;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) {
					return true;
				}

				if (!(obj instanceof DetailDTO)) {
					return false;
				}

				DetailDTO detailDTO = (DetailDTO)obj;

				if (Objects.equals(_selected, detailDTO._selected)) {
					return true;
				}

				return false;
			}

			@JsonProperty("selected")
			public Boolean getSelected() {
				return _selected;
			}

			@Override
			public int hashCode() {
				return Objects.hash(_selected);
			}

			@JsonIgnore
			public boolean isEmpty() {
				return equals(new DetailDTO());
			}

			private Boolean _selected;

		}

		private AccountsConfigurationDTO _accountsConfigurationDTO;
		private AnalyticsConfigurationDTO _analyticsConfigurationDTO;
		private ContactsConfigurationDTO _contactsConfigurationDTO;
		private String _providerType;

	}

	private AccountsConfigurationDTO _accountsConfigurationDTO;
	private AnalyticsConfigurationDTO _analyticsConfigurationDTO;
	private AuthorDTO _authorDTO;
	private String _channelId;
	private ContactsConfigurationDTO _contactsConfigurationDTO;
	private DetailDTO _contactsSyncDetailDTO;
	private Date _createDate;
	private CredentialDTO _credentialDTO;
	private Set<DataSourceDTO> _dataSourceDTOs;
	private String _dataSourceId;
	private String _dataSourceState;
	private String _dataSourceStatus;
	private Date _deletionDate;
	private String _existingDataSourceId;
	private String _faroBackendSecuritySignature;
	private String _id;
	private Date _modifiedDate;
	private String _name;
	private ProviderDTO _providerDTO;
	private DetailDTO _sitesSyncDetailDTO;
	private String _state;
	private String _status;
	private String _url;
	private String _workspaceURL;

}