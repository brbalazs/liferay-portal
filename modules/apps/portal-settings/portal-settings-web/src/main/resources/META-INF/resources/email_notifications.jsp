<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<h3><liferay-ui:message key="email-notifications" /></h3>

<%
String adminEmailFromName = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_NAME);
String adminEmailFromAddress = PrefsPropsUtil.getString(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_FROM_ADDRESS);

PortletPreferences companyPortletPreferences = PrefsPropsUtil.getPreferences(company.getCompanyId(), true);
%>

<liferay-ui:error-marker
	key="<%= WebKeys.ERROR_SECTION %>"
	value="email-notifications"
/>

<liferay-ui:tabs
	names="sender,account-created-notification,email-verification-notification,password-changed-notification,password-reset-notification"
	refresh="<%= false %>"
>
	<liferay-ui:section>
		<aui:fieldset>
			<liferay-ui:error key="emailFromName" message="please-enter-a-valid-name" />

			<aui:input cssClass="lfr-input-text-container" label="name" name='<%= "settings--" + PropsKeys.ADMIN_EMAIL_FROM_NAME + "--" %>' type="text" value="<%= adminEmailFromName %>" />

			<liferay-ui:error exception="<%= EmailAddressException.class %>" message="please-enter-a-valid-email-address" />

			<aui:input cssClass="lfr-input-text-container" label="address" name='<%= "settings--" + PropsKeys.ADMIN_EMAIL_FROM_ADDRESS + "--" %>' required="<%= true %>" type="text" value="<%= adminEmailFromAddress %>">
				<aui:validator name="email" />
			</aui:input>
		</aui:fieldset>
	</liferay-ui:section>

	<liferay-ui:section>
		<aui:fieldset>
			<aui:input label="enabled" name='<%= "settings--" + PropsKeys.ADMIN_EMAIL_USER_ADDED_ENABLED + "--" %>' type="checkbox" value="<%= PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.ADMIN_EMAIL_USER_ADDED_ENABLED) %>" />

			<liferay-ui:error key="emailUserAddedSubject" message="please-enter-a-valid-subject" />

			<aui:field-wrapper label="subject">
				<liferay-ui:input-localized
					fieldPrefix="settings"
					fieldPrefixSeparator="--"
					name="adminEmailUserAddedSubject"
					xml='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailUserAddedSubject", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_USER_ADDED_SUBJECT)) %>'
				/>
			</aui:field-wrapper>

			<liferay-ui:error key="adminEmailUserAddedResetPasswordBody" message="please-enter-a-valid-body" />

			<liferay-frontend:email-notification-settings
				bodyLabel='<%= LanguageUtil.get(resourceBundle, "body-with-password-link") %>'
				emailBody='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailUserAddedResetPasswordBody", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_USER_ADDED_RESET_PASSWORD_BODY)) %>'
				emailParam="adminEmailUserAddedResetPassword"
				fieldPrefix="settings"
				helpMessage='<%= LanguageUtil.get(resourceBundle, "account-created-notification-body-with-password-reset-help") %>'
				showEmailEnabled="<%= false %>"
				showSubject="<%= false %>"
			/>

			<liferay-ui:error key="emailUserAddedBody" message="please-enter-a-valid-body" />

			<liferay-frontend:email-notification-settings
				bodyLabel="body-with-password"
				emailBody='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailUserAddedBody", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_USER_ADDED_BODY)) %>'
				emailParam="adminEmailUserAdded"
				fieldPrefix="settings"
				helpMessage="account-created-notification-body-with-password-help"
				showEmailEnabled="<%= false %>"
				showSubject="<%= false %>"
			/>

			<liferay-ui:error key="emailUserAddedNoPasswordBody" message="please-enter-a-valid-body" />

			<liferay-frontend:email-notification-settings
				bodyLabel="body-without-password"
				emailBody='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailUserAddedNoPasswordBody", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_USER_ADDED_NO_PASSWORD_BODY)) %>'
				emailParam="adminEmailUserAddedNoPassword"
				fieldPrefix="settings"
				helpMessage="account-created-notification-body-without-password-help"
				showEmailEnabled="<%= false %>"
				showSubject="<%= false %>"
			/>

			<aui:fieldset cssClass="definition-of-terms email-user-add terms" label="definition-of-terms">
				<%@ include file="/definition_of_terms.jspf" %>
			</aui:fieldset>
		</aui:fieldset>
	</liferay-ui:section>

	<liferay-ui:section>
		<liferay-ui:error key="emailVerificationSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailVerificationBody" message="please-enter-a-valid-body" />

		<liferay-frontend:email-notification-settings
			emailBody='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailVerificationBody", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_VERIFICATION_BODY)) %>'
			emailParam="adminEmailVerification"
			emailSubject='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailVerificationSubject", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_VERIFICATION_SUBJECT)) %>'
			fieldPrefix="settings"
			showEmailEnabled="<%= false %>"
		/>

		<aui:fieldset cssClass="definition-of-terms email-verification terms" label="definition-of-terms">
			<%@ include file="/definition_of_terms.jspf" %>
		</aui:fieldset>
	</liferay-ui:section>

	<liferay-ui:section>
		<liferay-ui:error key="emailPasswordSentSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailPasswordSentBody" message="please-enter-a-valid-body" />

		<liferay-frontend:email-notification-settings
			bodyLabel="email-with-password"
			emailBody='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailPasswordSentBody", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_PASSWORD_SENT_BODY)) %>'
			emailParam="adminEmailPasswordSent"
			emailSubject='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailPasswordSentSubject", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_PASSWORD_SENT_SUBJECT)) %>'
			fieldPrefix="settings"
			showEmailEnabled="<%= false %>"
		/>

		<liferay-frontend:email-notification-settings
			bodyLabel="email-without-password"
			emailBody='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailPasswordChangedBody", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_PASSWORD_CHANGED_BODY)) %>'
			emailParam="adminEmailPasswordChanged"
			emailSubject='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailPasswordChangedSubject", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_PASSWORD_CHANGED_SUBJECT)) %>'
			fieldPrefix="settings"
			showEmailEnabled="<%= false %>"
		/>

		<aui:fieldset cssClass="definition-of-terms email-verification terms" label="definition-of-terms">
			<%@ include file="/definition_of_terms.jspf" %>
		</aui:fieldset>
	</liferay-ui:section>

	<liferay-ui:section>
		<liferay-ui:error key="emailPasswordResetSubject" message="please-enter-a-valid-subject" />
		<liferay-ui:error key="emailPasswordResetBody" message="please-enter-a-valid-body" />

		<liferay-frontend:email-notification-settings
			emailBody='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailPasswordResetBody", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_PASSWORD_RESET_BODY)) %>'
			emailParam="adminEmailPasswordReset"
			emailSubject='<%= LocalizationUtil.getLocalizationXmlFromPreferences(companyPortletPreferences, renderRequest, "adminEmailPasswordResetSubject", "settings", ContentUtil.get(PortalClassLoaderUtil.getClassLoader(), PropsValues.ADMIN_EMAIL_PASSWORD_RESET_SUBJECT)) %>'
			fieldPrefix="settings"
			showEmailEnabled="<%= false %>"
		/>

		<aui:fieldset cssClass="definition-of-terms email-verification terms" label="definition-of-terms">
			<%@ include file="/definition_of_terms.jspf" %>
		</aui:fieldset>
	</liferay-ui:section>
</liferay-ui:tabs>