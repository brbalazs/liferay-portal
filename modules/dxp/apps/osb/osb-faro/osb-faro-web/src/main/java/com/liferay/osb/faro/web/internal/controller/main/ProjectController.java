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

package com.liferay.osb.faro.web.internal.controller.main;

import com.liferay.osb.faro.constants.FaroUserConstants;
import com.liferay.osb.faro.contacts.model.constants.JSONConstants;
import com.liferay.osb.faro.contacts.service.ContactsCardTemplateLocalService;
import com.liferay.osb.faro.contacts.service.ContactsLayoutTemplateLocalService;
import com.liferay.osb.faro.engine.client.HubSpotEngineClient;
import com.liferay.osb.faro.engine.client.model.LCPProject;
import com.liferay.osb.faro.engine.client.model.Workspace;
import com.liferay.osb.faro.engine.client.model.WorkspaceService;
import com.liferay.osb.faro.exception.EmailAddressDomainException;
import com.liferay.osb.faro.model.FaroProject;
import com.liferay.osb.faro.model.FaroProjectEmailAddressDomain;
import com.liferay.osb.faro.provisioning.client.ProvisioningClient;
import com.liferay.osb.faro.provisioning.client.constants.CorpProjectConstants;
import com.liferay.osb.faro.provisioning.client.constants.ProductConstants;
import com.liferay.osb.faro.provisioning.client.model.OSBAccountEntry;
import com.liferay.osb.faro.provisioning.client.model.OSBOfferingEntry;
import com.liferay.osb.faro.service.FaroProjectEmailAddressDomainLocalService;
import com.liferay.osb.faro.service.FaroProjectLocalService;
import com.liferay.osb.faro.service.FaroUserLocalService;
import com.liferay.osb.faro.web.internal.constants.FaroConstants;
import com.liferay.osb.faro.web.internal.constants.ProjectConstants;
import com.liferay.osb.faro.web.internal.controller.BaseFaroController;
import com.liferay.osb.faro.web.internal.controller.contacts.FieldMappingController;
import com.liferay.osb.faro.web.internal.exception.FaroException;
import com.liferay.osb.faro.web.internal.exception.FaroValidationException;
import com.liferay.osb.faro.web.internal.model.display.contacts.JoinableProjectDisplay;
import com.liferay.osb.faro.web.internal.model.display.contacts.ProjectDisplay;
import com.liferay.osb.faro.web.internal.model.display.main.FaroSubscriptionDisplay;
import com.liferay.osb.faro.web.internal.model.display.main.WorkspaceServiceDisplay;
import com.liferay.osb.faro.web.internal.param.FaroParam;
import com.liferay.osb.faro.web.internal.util.ContactsLayoutUtil;
import com.liferay.osb.faro.web.internal.util.JSONUtil;
import com.liferay.osb.faro.web.internal.util.StreamUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.GroupFriendlyURLException;
import com.liferay.portal.kernel.exception.LayoutFriendlyURLException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserConstants;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.comparator.GroupNameComparator;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Matthew Kong
 */
@Component(immediate = true, service = ProjectController.class)
@Path("/project")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectController extends BaseFaroController {

	@Path("/{groupId}/activate")
	@POST
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public void activate(@PathParam("groupId") long groupId) throws Exception {
		FaroProject faroProject =
			_faroProjectLocalService.getFaroProjectByGroupId(groupId);

		if (!StringUtil.equals(
				faroProject.getState(), ProjectConstants.STATE_DEACTIVATED)) {

			return;
		}

		faroProject.setState(ProjectConstants.STATE_ACTIVATING);

		faroProjectLocalService.updateFaroProject(faroProject);

		workspaceEngineClient.updateWorkspace(
			faroProject.getWeDeployKey(), null, faroProject.isTrial());
	}

	@Path("/state")
	@POST
	@RolesAllowed(StringPool.BLANK)
	public void addGlobalState(
		@FormParam("keys") FaroParam<List<String>> keysFaroParam,
		@FormParam("state") String state,
		@FormParam("startDate") Date startDate,
		@FormParam("endDate") Date endDate) {

		Map<String, Object> stateMap = new HashMap<>();

		stateMap.put("endDate", endDate);
		stateMap.put("startDate", startDate);
		stateMap.put("state", state);

		projectUtil.addGlobalState(keysFaroParam.getValue(), stateMap);
	}

	@Path("/{groupId}/ip_addresses")
	@POST
	@RolesAllowed(StringPool.BLANK)
	public void addIPAddresses(
			@PathParam("groupId") long groupId,
			@FormParam("ipAddresses")
				FaroParam<List<String>> ipAddressesFaroParam)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.fetchFaroProjectByGroupId(groupId);

		faroProject.setIpAddresses(
			JSONUtil.writeValueAsString(ipAddressesFaroParam.getValue()));

		_faroProjectLocalService.updateFaroProject(faroProject);
	}

	@Path("/{groupId}/configure")
	@PUT
	@RolesAllowed(RoleConstants.SITE_ADMINISTRATOR)
	public ProjectDisplay configure(
			@FormParam("friendlyURL") String friendlyURL,
			@PathParam("groupId") long groupId,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY)
			@FormParam("emailAddressDomains")
				FaroParam<List<String>> emailAddressDomainsFaroParam,
			@FormParam("name") String name)
		throws Exception {

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(groupId);

		User user = getUser();

		_provisioningClient.addCorpProjectUsers(
			faroProject.getCorpProjectUuid(),
			new String[] {user.getUserUuid()});

		_provisioningClient.addUserCorpProjectRoles(
			faroProject.getCorpProjectUuid(), new String[] {user.getUserUuid()},
			CorpProjectConstants.ROLE_OWNER);

		_hubSpotEngineClient.submitWorkspaceUserForm(
			faroProject,
			_faroUserLocalService.getFaroUser(
				faroProject.getGroupId(), user.getUserId()),
			true);

		faroProject.setState(ProjectConstants.STATE_NOT_READY);

		faroProjectLocalService.updateFaroProject(faroProject);

		return update(friendlyURL, groupId, emailAddressDomainsFaroParam, name);
	}

	@POST
	public ProjectDisplay create(
			@FormParam("name") String name,
			@FormParam("corpProjectUuid") String corpProjectUuid,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY)
			@FormParam("emailAddressDomains")
				FaroParam<List<String>> emailAddressDomainsFaroParam,
			@FormParam("serverLocation") String serverLocation,
			@FormParam("friendlyURL") String friendlyURL)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.fetchFaroProjectByCorpProjectUuid(
				corpProjectUuid);

		if (faroProject != null) {
			return new ProjectDisplay(
				faroProject, cerebroEngineClient, contactsEngineClient,
				_provisioningClient);
		}

		validateCorpProjectUuid(corpProjectUuid);

		validateFriendlyURL(friendlyURL);

		User user = getUser();

		_provisioningClient.addCorpProjectUsers(
			corpProjectUuid, new String[] {user.getUserUuid()});

		_provisioningClient.addUserCorpProjectRoles(
			corpProjectUuid, new String[] {user.getUserUuid()},
			CorpProjectConstants.ROLE_OWNER);

		_hubSpotEngineClient.submitWorkspaceUserForm(
			faroProject,
			_faroUserLocalService.getFaroUser(
				faroProject.getGroupId(), user.getUserId()),
			true);

		faroProject = _create(
			corpProjectUuid, name, emailAddressDomainsFaroParam.getValue(),
			friendlyURL, serverLocation, ProjectConstants.STATE_NOT_READY);

		Role role = _roleLocalService.getRole(
			user.getCompanyId(), RoleConstants.SITE_OWNER);

		_faroUserLocalService.addFaroUser(
			user.getUserId(), faroProject.getGroupId(), user.getUserId(),
			role.getRoleId(), user.getEmailAddress(),
			FaroUserConstants.STATUS_APPROVED, false);

		return new ProjectDisplay(faroProject, friendlyURL);
	}

	@Path("/provisioned")
	@POST
	@RolesAllowed(StringPool.BLANK)
	public ProjectDisplay createProvisioned(
			@FormParam("corpProjectUuid") String corpProjectUuid,
			@FormParam("ownerEmailAddress") String ownerEmailAddress,
			@FormParam("serverLocation") String serverLocation)
		throws Exception {

		User user = getUser();

		FaroProject faroProject = _create(
			corpProjectUuid, null, null, null, serverLocation,
			ProjectConstants.STATE_UNCONFIGURED);

		Role role = _roleLocalService.getRole(
			user.getCompanyId(), RoleConstants.SITE_OWNER);

		_faroUserLocalService.addFaroUser(
			getUserId(), faroProject.getGroupId(), 0, role.getRoleId(),
			ownerEmailAddress, FaroUserConstants.STATUS_PENDING, false);

		return new ProjectDisplay(faroProject);
	}

	@Path("/trial")
	@POST
	public ProjectDisplay createTrial(
			@FormParam("name") String name,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY)
			@FormParam("emailAddressDomains")
				FaroParam<List<String>> emailAddressDomainsFaroParam,
			@FormParam("friendlyURL") String friendlyURL,
			@FormParam("serverLocation") String serverLocation)
		throws Exception {

		List<FaroProject> faroProjects =
			_faroProjectLocalService.getFaroProjectsByUserId(getUserId());

		Stream<FaroProject> stream = faroProjects.stream();

		FaroProject faroProject = stream.filter(
			FaroProject::isTrial
		).findAny(
		).orElse(
			null
		);

		if (faroProject != null) {
			throw new FaroValidationException(
				null,
				getLocalizedMessage("this-user-already-owns-a-trial-project"));
		}

		return _createUnprovisioned(
			name, null, null, null, null, emailAddressDomainsFaroParam,
			friendlyURL, serverLocation, true);
	}

	@Path("/unprovisioned")
	@POST
	@RolesAllowed(StringPool.BLANK)
	public ProjectDisplay createUnprovisioned(
			@FormParam("name") String name,
			@FormParam("accountKey") String accountKey,
			@FormParam("accountName") String accountName,
			@FormParam("corpProjectName") String corpProjectName,
			@FormParam("corpProjectUuid") String corpProjectUuid,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY)
			@FormParam("emailAddressDomains")
				FaroParam<List<String>> emailAddressDomainsFaroParam,
			@FormParam("friendlyURL") String friendlyURL,
			@FormParam("serverLocation") String serverLocation,
			@FormParam("trial") boolean trial)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.fetchFaroProjectByCorpProjectUuid(
				corpProjectUuid);

		if (faroProject != null) {
			return new ProjectDisplay(
				faroProject, cerebroEngineClient, contactsEngineClient,
				_provisioningClient);
		}

		return _createUnprovisioned(
			name, accountKey, accountName, corpProjectName, corpProjectUuid,
			emailAddressDomainsFaroParam, friendlyURL, serverLocation, trial);
	}

	@DELETE
	@Path("/{groupId}")
	@RolesAllowed(StringPool.BLANK)
	public ProjectDisplay delete(@PathParam("groupId") long groupId)
		throws Exception {

		_contactsCardTemplateLocalService.deleteContactsCardTemplates(groupId);
		_contactsLayoutTemplateLocalService.deleteContactsLayoutTemplates(
			groupId);

		return new ProjectDisplay(
			_faroProjectLocalService.deleteFaroProjectByGroupId(groupId));
	}

	@DELETE
	@Path("/state")
	@RolesAllowed(StringPool.BLANK)
	public void deleteGlobalState(
		@FormParam("keys") FaroParam<List<String>> keysFaroParam) {

		projectUtil.deleteGlobalStates(keysFaroParam.getValue());
	}

	@Path("/{groupId}/recommendations/disable")
	@POST
	@RolesAllowed(StringPool.BLANK)
	public void disableRecommendations(@PathParam("groupId") long groupId) {
		FaroProject faroProject =
			_faroProjectLocalService.fetchFaroProjectByGroupId(groupId);

		faroProject.setRecommendationsEnabled(false);

		_faroProjectLocalService.updateFaroProject(faroProject);
	}

	@Path("/{groupId}/recommendations/enable")
	@POST
	@RolesAllowed(StringPool.BLANK)
	public void enableRecommendations(@PathParam("groupId") long groupId) {
		FaroProject faroProject =
			_faroProjectLocalService.fetchFaroProjectByGroupId(groupId);

		faroProject.setRecommendationsEnabled(true);

		_faroProjectLocalService.updateFaroProject(faroProject);
	}

	@GET
	@Path("/{groupId}")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public ProjectDisplay get(
			@PathParam("groupId") long groupId,
			@QueryParam("forceUpdate") boolean forceUpdate,
			@DefaultValue("true") @QueryParam("updateLastAccess")
				boolean updateLastAccess)
		throws Exception {

		FaroProject faroProject =
			_faroProjectLocalService.getFaroProjectByGroupId(groupId);

		long now = System.currentTimeMillis();

		if (forceUpdate) {
			faroProject.setModifiedTime(now);

			LCPProject lcpProject = workspaceEngineClient.getLCPProject(
				faroProject.getWeDeployKey());

			faroProject.setServerLocation(lcpProject.getCluster());

			if (!StringUtil.equals(
					faroProject.getCorpProjectUuid(), _PROJECT_ID)) {

				faroProject.setServices(
					JSONUtil.writeValueAsString(
						StreamUtil.toList(
							workspaceEngineClient.getWorkspaceServices(
								faroProject.getWeDeployKey()),
							WorkspaceServiceDisplay::new)));
			}

			if (!faroProject.isTrial()) {
				faroProject.setSubscription(
					JSONUtil.writeValueAsString(
						new FaroSubscriptionDisplay(
							_provisioningClient.getOSBAccountEntry(
								faroProject.getCorpProjectUuid()))));
			}

			_faroProjectLocalService.updateFaroProject(faroProject);
		}

		if (updateLastAccess &&
			((now - faroProject.getLastAccessTime()) > Time.DAY)) {

			faroProject.setLastAccessTime(now);

			_faroProjectLocalService.updateFaroProject(faroProject);
		}

		return getProjectDisplay(faroProject);
	}

	@GET
	@Path("/corpProjectUuid/{corpProjectUuid}")
	public ProjectDisplay get(
		@PathParam("corpProjectUuid") String corpProjectUuid) {

		validateCorpProjectUuid(corpProjectUuid);

		FaroProject faroProject =
			_faroProjectLocalService.fetchFaroProjectByCorpProjectUuid(
				corpProjectUuid);

		if (faroProject == null) {
			return new ProjectDisplay(
				_provisioningClient.getOSBAccountEntry(corpProjectUuid));
		}

		return new ProjectDisplay(
			faroProject, cerebroEngineClient, contactsEngineClient,
			_provisioningClient);
	}

	@GET
	@Path("/{groupId}/email_address_domains")
	@RolesAllowed(RoleConstants.SITE_MEMBER)
	public List<String> getEmailAddressDomains(
		@PathParam("groupId") long groupId) {

		List<FaroProjectEmailAddressDomain> faroProjectEmailAddressDomains =
			_faroProjectEmailAddressDomainLocalService.
				getFaroProjectEmailAddressDomainsByGroupId(groupId);

		Stream<FaroProjectEmailAddressDomain> emailAddressDomainStream =
			faroProjectEmailAddressDomains.stream();

		return emailAddressDomainStream.map(
			FaroProjectEmailAddressDomain::getEmailAddressDomain
		).collect(
			Collectors.toList()
		);
	}

	@GET
	@Path("/joinable")
	public List<JoinableProjectDisplay> getJoinableProjects()
		throws PortalException {

		List<FaroProject> faroProjects =
			_faroProjectLocalService.getJoinableFaroProjects(getUser());

		Stream<FaroProject> faroProjectsStream = faroProjects.stream();

		return faroProjectsStream.map(
			faroProject -> new JoinableProjectDisplay(
				faroProject.getGroupId(), faroProject.getName(),
				Objects.nonNull(
					_faroUserLocalService.fetchFaroUser(
						faroProject.getGroupId(), getUserId())))
		).collect(
			Collectors.toList()
		);
	}

	@GET
	public List<ProjectDisplay> getProjects() throws Exception {
		User user = getUser();

		List<Group> groups = _groupLocalService.getUserGroups(
			user.getUserId(), QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			new GroupNameComparator(true));

		Stream<Group> groupsStream = groups.stream();

		List<ProjectDisplay> projectDisplays = groupsStream.filter(
			group -> StringUtil.equals(
				group.getClassName(), FaroProject.class.getName())
		).map(
			Group::getGroupId
		).map(
			_faroProjectLocalService::fetchFaroProjectByGroupId
		).map(
			faroProject -> {
				try {
					return getProjectDisplay(faroProject);
				}
				catch (Exception e) {
					if (_log.isWarnEnabled()) {
						String project = "";

						if (faroProject != null) {
							project = faroProject.getName();
						}

						_log.warn("Could not load project " + project, e);
					}

					return null;
				}
			}
		).filter(
			Objects::nonNull
		).collect(
			Collectors.toList()
		);

		List<OSBAccountEntry> osbAccountEntries =
			_provisioningClient.getOSBAccountEntries(
				user.getUserUuid(), ProductConstants.getProductEntryIds());

		Stream<OSBAccountEntry> osbAccountEntryStream =
			osbAccountEntries.stream();

		osbAccountEntryStream.filter(
			osbAccountEntry -> {
				if (osbAccountEntry.getStatus() !=
						FaroUserConstants.STATUS_APPROVED) {

					return false;
				}

				if (_faroProjectLocalService.fetchFaroProjectByCorpProjectUuid(
						osbAccountEntry.getCorpProjectUuid()) != null) {

					return false;
				}

				return true;
			}
		).map(
			ProjectDisplay::new
		).filter(
			projectDisplay -> {
				FaroSubscriptionDisplay faroSubscriptionDisplay =
					projectDisplay.getFaroSubscriptionDisplay();

				return faroSubscriptionDisplay.isActive();
			}
		).forEach(
			projectDisplays::add
		);

		return projectDisplays;
	}

	@Path("/{groupId}")
	@PUT
	@RolesAllowed(RoleConstants.SITE_ADMINISTRATOR)
	public ProjectDisplay update(
			@FormParam("friendlyURL") String friendlyURL,
			@PathParam("groupId") long groupId,
			@DefaultValue(JSONConstants.NULL_JSON_ARRAY)
			@FormParam("emailAddressDomains")
				FaroParam<List<String>> emailAddressDomainsFaroParam,
			@FormParam("name") String name)
		throws Exception {

		if ((friendlyURL == null) || Validator.isBlank(friendlyURL.trim())) {
			Group group = _groupLocalService.getGroup(groupId);

			group.setFriendlyURL(null);

			_groupLocalService.updateGroup(group);
		}
		else {
			validateFriendlyURL(friendlyURL);

			try {
				_groupLocalService.updateFriendlyURL(groupId, friendlyURL);
			}
			catch (GroupFriendlyURLException gfurle) {
				_log.error(gfurle, gfurle);

				throw new FaroValidationException(
					"friendlyURL",
					getFriendlyURLErrorMessage(gfurle.getType(), getUser()));
			}
		}

		FaroProject faroProject =
			faroProjectLocalService.getFaroProjectByGroupId(groupId);

		faroProject.setName(name);

		try {
			_faroProjectEmailAddressDomainLocalService.
				addFaroProjectEmailAddressDomains(
					groupId, faroProject.getFaroProjectId(),
					emailAddressDomainsFaroParam.getValue());
		}
		catch (EmailAddressDomainException eade) {
			throw new FaroValidationException(
				"emailAddressDomains",
				getEmailAddressDomainsErrorMessage(
					eade.getInvalidEmailAddressDomains()));
		}

		return getProjectDisplay(
			faroProjectLocalService.updateFaroProject(faroProject));
	}

	protected void addDefaultContactsEntities(long groupId) throws Exception {
		for (int type : FaroConstants.getContactsTypes()) {
			_contactsLayoutTemplateLocalService.addContactsLayoutTemplate(
				groupId, UserConstants.USER_ID_DEFAULT,
				_contactsLayoutUtil.addHeaderContactsCardTemplateIds(
					groupId, type),
				"default",
				_contactsLayoutUtil.addContactsCardTemplateIds(groupId, type),
				type);
		}
	}

	protected String getEmailAddressDomainsErrorMessage(
		Collection<String> invalidEmailAddressDomains) {

		User user = getUser();

		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", user.getLocale(), getClass());

		String pattern = "x-is-not-allowed-to-be-set-as-an-email-domain";

		if (invalidEmailAddressDomains.size() > 1) {
			pattern = "x-are-not-allowed-to-be-set-as-email-domains";
		}

		return language.format(
			resourceBundle, pattern,
			StringUtil.merge(invalidEmailAddressDomains));
	}

	protected String getFriendlyURLErrorMessage(int type, User user) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", user.getLocale(), getClass());

		if (type == LayoutFriendlyURLException.ADJACENT_SLASHES) {
			return language.get(
				resourceBundle,
				"please-enter-a-friendly-url-that-does-not-have-adjacent-" +
					"slashes");
		}
		else if (type == LayoutFriendlyURLException.DOES_NOT_START_WITH_SLASH) {
			return language.get(
				resourceBundle,
				"please-enter-a-friendly-url-that-begins-with-a-slash");
		}
		else if ((type == LayoutFriendlyURLException.DUPLICATE) ||
				 (type == LayoutFriendlyURLException.KEYWORD_CONFLICT) ||
				 (type == LayoutFriendlyURLException.POSSIBLE_DUPLICATE)) {

			return language.get(
				resourceBundle, "this-friendly-url-is-already-in-use");
		}
		else if (type == LayoutFriendlyURLException.ENDS_WITH_DASH) {
			return language.get(
				resourceBundle,
				"please-enter-a-friendly-url-that-does-not-end-with-a-dash");
		}
		else if (type == LayoutFriendlyURLException.ENDS_WITH_SLASH) {
			return language.get(
				resourceBundle,
				"please-enter-a-friendly-url-that-does-not-end-with-a-slash");
		}
		else if (type == LayoutFriendlyURLException.INVALID_CHARACTERS) {
			return language.get(
				resourceBundle,
				"please-enter-a-friendly-url-with-valid-characters");
		}
		else if (type == LayoutFriendlyURLException.TOO_SHORT) {
			return language.get(
				resourceBundle,
				"please-enter-a-friendly-url-that-is-at-least-two-characters-" +
					"long");
		}
		else if (type == LayoutFriendlyURLException.TOO_DEEP) {
			return language.get(
				resourceBundle, "the-friendly-url-has-too-many-slashes");
		}
		else if (type == LayoutFriendlyURLException.TOO_LONG) {
			return language.get(
				resourceBundle,
				"please-enter-a-friendly-url-that-is-at-least-two-characters-" +
					"long");
		}

		return language.get(resourceBundle, "invalid-friendly-url");
	}

	protected ProjectDisplay getProjectDisplay(FaroProject faroProject)
		throws Exception {

		if (StringUtil.equals(
				faroProject.getState(), ProjectConstants.STATE_UNCONFIGURED)) {

			return new ProjectDisplay(faroProject);
		}

		if (StringUtil.equals(
				faroProject.getState(), ProjectConstants.STATE_NOT_READY)) {

			try {
				refreshProjectState(faroProject);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn(
						"Could not refresh project " + faroProject.getName(),
						e);
				}
			}
		}
		else if (StringUtil.equals(
					faroProject.getState(),
					ProjectConstants.STATE_ACTIVATING) &&
				 _isWorkspaceHealthy(faroProject)) {

			faroProject.setState(ProjectConstants.STATE_READY);

			faroProject = _faroProjectLocalService.updateFaroProject(
				faroProject);
		}

		ProjectDisplay projectDisplay = new ProjectDisplay(
			faroProject, cerebroEngineClient, contactsEngineClient,
			_provisioningClient);

		Group group = _groupLocalService.getGroup(faroProject.getGroupId());

		projectDisplay.setFriendlyURL(group.getFriendlyURL());

		Map<String, Object> globalStateMap = projectUtil.getGlobalStateMap(
			faroProject);

		if (globalStateMap == null) {
			return projectDisplay;
		}

		String state = (String)globalStateMap.get("state");

		if (StringUtil.equals(
				projectDisplay.getState(),
				ProjectConstants.STATE_UNAVAILABLE) &&
			StringUtil.equals(state, ProjectConstants.STATE_SCHEDULED)) {

			return projectDisplay;
		}
		else if (StringUtil.equals(
					projectDisplay.getState(), ProjectConstants.STATE_READY) &&
				 StringUtil.equals(state, ProjectConstants.STATE_MAINTENANCE) &&
				 contactsEngineClient.isLatestVersion(faroProject)) {

			projectUtil.deleteGlobalState(faroProject.getGroupId());

			return projectDisplay;
		}

		projectDisplay.setState(state);
		projectDisplay.setStateEndDate((Date)globalStateMap.get("endDate"));
		projectDisplay.setStateStartDate((Date)globalStateMap.get("startDate"));

		return projectDisplay;
	}

	protected FaroProject initializeFaroProject(FaroProject faroProject)
		throws Exception {

		long groupId = faroProject.getGroupId();

		if (_initializingGroupIds.contains(groupId)) {
			faroProject.setState(ProjectConstants.STATE_READY);

			return faroProject;
		}

		try {
			_initializingGroupIds.add(groupId);

			_fieldMappingController.addDefaultFieldMappings(groupId);

			if (!StringUtil.equals(
					faroProject.getCorpProjectUuid(), _PROJECT_ID)) {

				faroProject.setServices(
					JSONUtil.writeValueAsString(
						StreamUtil.toList(
							workspaceEngineClient.getWorkspaceServices(
								faroProject.getWeDeployKey()),
							WorkspaceServiceDisplay::new)));
			}

			faroProject.setState(ProjectConstants.STATE_READY);

			_faroProjectLocalService.updateFaroProject(faroProject);

			return faroProject;
		}
		finally {
			_initializingGroupIds.remove(groupId);
		}
	}

	protected void refreshProjectState(FaroProject faroProject)
		throws Exception {

		if (StringUtil.equals(faroProject.getCorpProjectUuid(), _PROJECT_ID)) {
			initializeFaroProject(faroProject);

			return;
		}

		Workspace workspace = workspaceEngineClient.getWorkspace(
			faroProject.getWeDeployKey());

		if (workspace.isReady()) {
			initializeFaroProject(faroProject);
		}
	}

	protected void validateCorpProjectUuid(String corpProjectUuid) {
		if (isOmniadmin()) {
			return;
		}

		User user = getUser();

		List<OSBAccountEntry> osbAccountEntries =
			_provisioningClient.getOSBAccountEntries(
				user.getUserUuid(), ProductConstants.getProductEntryIds());

		for (OSBAccountEntry osbAccountEntry : osbAccountEntries) {
			if (StringUtil.equals(
					osbAccountEntry.getCorpProjectUuid(), corpProjectUuid)) {

				return;
			}
		}

		throw new FaroException(
			"You do not have the required permissions",
			Response.Status.FORBIDDEN);
	}

	protected void validateFriendlyURL(String friendlyURL) {
		if ((friendlyURL == null) || Validator.isBlank(friendlyURL)) {
			return;
		}

		if (friendlyURL.matches("^/\\d*$")) {
			throw new FaroValidationException(
				"friendlyURL", getFriendlyURLErrorMessage(0, getUser()));
		}
	}

	private FaroProject _create(
			String corpProjectUuid, String name,
			List<String> emailAddressDomains, String friendlyURL,
			String serverLocation, String state)
		throws Exception {

		OSBAccountEntry osbAccountEntry =
			_provisioningClient.getOSBAccountEntry(corpProjectUuid);

		FaroSubscriptionDisplay faroSubscriptionDisplay =
			new FaroSubscriptionDisplay(osbAccountEntry);

		User user = getUser();

		FaroProject faroProject = null;

		try {
			faroProject = _faroProjectLocalService.addFaroProject(
				user.getUserId(), name, osbAccountEntry.getDossieraAccountKey(),
				osbAccountEntry.getCorpEntryName(), osbAccountEntry.getName(),
				corpProjectUuid, emailAddressDomains, friendlyURL,
				serverLocation, JSONConstants.NULL_JSON_ARRAY, state,
				JSONUtil.writeValueAsString(faroSubscriptionDisplay), null);
		}
		catch (EmailAddressDomainException eade) {
			throw new FaroValidationException(
				"emailAddressDomains",
				getEmailAddressDomainsErrorMessage(
					eade.getInvalidEmailAddressDomains()));
		}
		catch (GroupFriendlyURLException gfurle) {
			_log.error(gfurle, gfurle);

			throw new FaroValidationException(
				"friendlyURL",
				getFriendlyURLErrorMessage(gfurle.getType(), user));
		}

		String weDeployKey = null;

		if (corpProjectUuid.equals(_PROJECT_ID)) {
			weDeployKey = _DEFAULT_WE_DEPLOY_KEY;
		}
		else {
			Workspace workspace = workspaceEngineClient.createWorkspace(
				serverLocation, faroProject.isTrial());

			weDeployKey = workspace.getWeDeployKey();
		}

		faroProject.setWeDeployKey(weDeployKey);

		return _faroProjectLocalService.updateFaroProject(faroProject);
	}

	private ProjectDisplay _createUnprovisioned(
			String name, String accountKey, String accountName,
			String corpProjectName, String corpProjectUuid,
			FaroParam<List<String>> emailAddressDomainsFaroParam,
			String friendlyURL, String serverLocation, boolean trial)
		throws Exception {

		validateFriendlyURL(friendlyURL);

		FaroSubscriptionDisplay faroSubscriptionDisplay =
			new FaroSubscriptionDisplay(
				new OSBAccountEntry() {
					{
						OSBOfferingEntry osbOfferingEntry =
							new OSBOfferingEntry();

						if (trial) {
							osbOfferingEntry.setProductEntryId(
								ProductConstants.BASIC_PRODUCT_ENTRY_ID);
						}
						else {
							osbOfferingEntry.setProductEntryId(
								ProductConstants.ENTERPRISE_PRODUCT_ENTRY_ID);
						}

						osbOfferingEntry.setQuantity(1);

						setOfferingEntries(
							Collections.singletonList(osbOfferingEntry));
					}
				});

		FaroProject faroProject = null;

		try {
			faroProject = _faroProjectLocalService.addFaroProject(
				getUserId(), name, accountKey, accountName, corpProjectName,
				corpProjectUuid, emailAddressDomainsFaroParam.getValue(),
				friendlyURL, serverLocation, JSONConstants.NULL_JSON_ARRAY,
				ProjectConstants.STATE_NOT_READY,
				JSONUtil.writeValueAsString(faroSubscriptionDisplay), null);
		}
		catch (EmailAddressDomainException eade) {
			throw new FaroValidationException(
				"emailAddressDomains",
				getEmailAddressDomainsErrorMessage(
					eade.getInvalidEmailAddressDomains()));
		}
		catch (GroupFriendlyURLException gfurle) {
			_log.error(gfurle, gfurle);

			throw new FaroValidationException(
				"friendlyURL",
				getFriendlyURLErrorMessage(gfurle.getType(), getUser()));
		}

		Workspace workspace = workspaceEngineClient.createWorkspace(
			serverLocation, faroProject.isTrial());

		faroProject.setWeDeployKey(workspace.getWeDeployKey());

		return new ProjectDisplay(
			_faroProjectLocalService.updateFaroProject(faroProject),
			friendlyURL);
	}

	private boolean _isWorkspaceHealthy(FaroProject faroProject) {
		for (WorkspaceService workspaceService :
				workspaceEngineClient.getWorkspaceServices(
					faroProject.getWeDeployKey())) {

			if (!workspaceService.isReady() ||
				!StringUtil.equals(
					workspaceService.getHealth(),
					Workspace.Health.healthy.name())) {

				return false;
			}

			try {
				contactsEngineClient.getIndividuals(
					faroProject, (String)null, false, 1, 0, null);
			}
			catch (Exception e) {
				return false;
			}
		}

		return true;
	}

	private static final String _DEFAULT_WE_DEPLOY_KEY = System.getenv(
		"FARO_DEFAULT_WE_DEPLOY_KEY");

	private static final String _PROJECT_ID = System.getenv("FARO_PROJECT_ID");

	private static final Log _log = LogFactoryUtil.getLog(
		ProjectController.class);

	private static final CopyOnWriteArraySet<Long> _initializingGroupIds =
		new CopyOnWriteArraySet<>();

	@Reference
	private ContactsCardTemplateLocalService _contactsCardTemplateLocalService;

	@Reference
	private ContactsLayoutTemplateLocalService
		_contactsLayoutTemplateLocalService;

	@Reference
	private ContactsLayoutUtil _contactsLayoutUtil;

	@Reference
	private FaroProjectEmailAddressDomainLocalService
		_faroProjectEmailAddressDomainLocalService;

	@Reference
	private FaroProjectLocalService _faroProjectLocalService;

	@Reference
	private FaroUserLocalService _faroUserLocalService;

	@Reference
	private FieldMappingController _fieldMappingController;

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private HubSpotEngineClient _hubSpotEngineClient;

	@Reference(
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY
	)
	private volatile ProvisioningClient _provisioningClient;

	@Reference
	private RoleLocalService _roleLocalService;

}