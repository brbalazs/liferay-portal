<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/admin/init.jsp" %>

<%
OAuth2Application oAuth2Application = oAuth2AdminPortletDisplayContext.getOAuth2Application();
%>

<aui:model-context bean="<%= oAuth2Application %>" model="<%= OAuth2Application.class %>" />

<aui:fieldset>
	<aui:input helpMessage="application-name-help" label="application-name" name="name" required="<%= true %>" />

	<aui:input helpMessage="home-page-url-help" name="homePageURL" />

	<c:if test="<%= oAuth2Application != null %>">
		<aui:input helpMessage="application-description-help" label="application-description" name="description" />
	</c:if>

	<aui:input helpMessage="redirect-uris-help" label="redirect-uris" name="redirectURIs" required="<%= true %>" />

	<c:if test="<%= oAuth2Application != null %>">
		<aui:input helpMessage="privacy-policy-url-help" name="privacyPolicyURL" />
	</c:if>

	<aui:select name="clientProfile">

		<%
		ClientProfile[] clientProfiles = oAuth2AdminPortletDisplayContext.getSortedClientProfiles();

		for (ClientProfile clientProfile : clientProfiles) {
		%>

			<aui:option label="<%= clientProfile.name() %>" value="<%= clientProfile.id() %>" />

		<%
		}
		%>

	</aui:select>

	<aui:fieldset label="allowed-grant-types">
		<aui:field-wrapper>
			<div id="<portlet:namespace />allowedGrantTypes">

				<%
				List<GrantType> allowedGrantTypesList = new ArrayList<>();

				if (oAuth2Application != null) {
					allowedGrantTypesList = oAuth2Application.getAllowedGrantTypesList();
				}

				List<GrantType> oAuth2Grants = oAuth2AdminPortletDisplayContext.getGrantTypes(portletPreferences);

				for (GrantType grantType : oAuth2Grants) {
					Set<String> cssClasses = new HashSet<>();

					for (ClientProfile clientProfile : ClientProfile.values()) {
						Set<GrantType> grantTypes = clientProfile.grantTypes();

						if (grantTypes.contains(grantType)) {
							cssClasses.add("client-profile-" + clientProfile.id());
						}
					}

					String cssClassesStr = StringUtil.merge(cssClasses, StringPool.SPACE);

					boolean checked = false;

					if ((oAuth2Application == null) || allowedGrantTypesList.contains(grantType)) {
						checked = true;
					}

					String name = "grant-" + grantType.name();

					checked = ParamUtil.getBoolean(request, name, checked);

					Map<String, Object> data = new HashMap<>();

					data.put("isredirect", grantType.isRequiresRedirectURI());
					data.put("issupportsconfidentialclients", grantType.isSupportsConfidentialClients());
					data.put("issupportspublicclients", grantType.isSupportsPublicClients());
				%>

					<div class="allowedGrantType <%= cssClassesStr %>">
						<aui:input checked="<%= checked %>" data="<%= data %>" label="<%= grantType.name() %>" name="<%= name %>" type="checkbox" />
					</div>

					<c:if test="<%= grantType.isRequiresRedirectURI() %>">
						<aui:script use="aui-base">
							$('#<portlet:namespace /><%= name %>').on(
								'click',
								function(event) {
									<portlet:namespace />requiredRedirectURIs();
								}
							);
						</aui:script>
					</c:if>

				<%
				}
				%>

			</div>
		</aui:field-wrapper>
	</aui:fieldset>

	<c:if test="<%= oAuth2Application != null %>">
		<aui:fieldset label="supported-features">
			<aui:field-wrapper>

				<%
				List<String> oAuth2ApplicationFeaturesList = new ArrayList<>();

				if (oAuth2Application != null) {
					oAuth2ApplicationFeaturesList = oAuth2Application.getFeaturesList();
				}

				String[] oAuth2Features = oAuth2AdminPortletDisplayContext.getOAuth2Features(portletPreferences);

				for (String oAuth2Feature : oAuth2Features) {
					boolean checked = false;

					if ((oAuth2Application != null) && oAuth2ApplicationFeaturesList.contains(oAuth2Feature)) {
						checked = true;
					}

					String name = "feature-" + oAuth2Feature;

					checked = ParamUtil.getBoolean(request, name, checked);
				%>

					<div class="supportedFeature">
						<aui:input checked="<%= checked %>" label="<%= HtmlUtil.escape(oAuth2Feature) %>" name="<%= name %>" type="checkbox" />
					</div>

				<%
				}
				%>

			</aui:field-wrapper>
		</aui:fieldset>
	</c:if>
</aui:fieldset>