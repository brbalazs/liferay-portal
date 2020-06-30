<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect", currentURL);

String version = PrefsPropsUtil.getString(themeDisplay.getCompanyId(), UpgradeConstants.REPOSITORY_SHA);
int threadCount = PrefsPropsUtil.getInteger(themeDisplay.getCompanyId(), UpgradeConstants.UPGRADE_THREAD_COUNT);
%>

<liferay-portlet:actionURL var="configurationActionURL" />

<aui:form action="<%= configurationActionURL %>" cssClass="container-fluid container-fluid-max-xl container-form-lg" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "updatePreferences();" %>'>
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />

	<div class="sheet sheet-lg">
		<div class="sheet-section">
			<div class="form-group-autofit">
				<div class="form-group-item">
					<aui:input label="version" name="version" type="text" value="<%= version %>" wrapperCssClass="lfr-input-text-container" />
				</div>

				<div class="form-group-item">
					<aui:input label="thread-count" name="threadCount" type="text" value="<%= threadCount %>" wrapperCssClass="lfr-input-text-container">
						<aui:validator name="digits" />
					</aui:input>
				</div>
			</div>
		</div>

		<div class="sheet-footer">
			<div class="btn-group">
				<div class="btn-group-item">
					<clay:button
						label='<%= LanguageUtil.get(request, "save") %>'
						style="primary"
						type="submit"
					/>
				</div>
			</div>
		</div>
	</div>
</aui:form>

<aui:script>
	function <portlet:namespace />updatePreferences() {
		submitForm(
			document.<portlet:namespace />fm,
			'<portlet:actionURL name="updatePreferences" />'
		);
	}
</aui:script>