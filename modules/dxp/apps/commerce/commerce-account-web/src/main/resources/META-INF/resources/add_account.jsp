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
long commerceAccountId = ParamUtil.getLong(request, "commerceAccountId");
%>

<portlet:actionURL name="editCommerceAccount" var="editCommerceAccountActionURL" />

<aui:form action="<%= editCommerceAccountActionURL %>" method="post" name="commerceAccountFm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
	<aui:input name="active" type="hidden" value="<%= true %>" />
	<aui:input name="parentCommerceAccountId" type="hidden" value="<%= commerceAccountId %>" />

	<liferay-ui:error exception="<%= UserEmailAddressException.MustValidate.class %>" message="please-enter-a-valid-email-address" />

	<div class="lfr-form-content">
		<aui:input label="name" name="name" type="text" />

		<aui:input label="administrator-id" name="userIds" type="text" />

		<aui:input label="administrator-email" name="emailAddresses" type="text" />
	</div>

	<aui:button-row>
		<aui:button name="saveButton" onClick='<%= renderResponse.getNamespace() + "submitFm();" %>' primary="<%= true %>" value="save" />

		<aui:button name="cancelButton" onClick='<%= renderResponse.getNamespace() + "closeDialog();" %>' value="cancel" />
	</aui:button-row>
</aui:form>

<aui:script>
	function <portlet:namespace />closeDialog() {
		Liferay.Util.getOpener().<portlet:namespace />closePopup('commerceAccountDialog');
	}

	Liferay.provide(
		window,
		'<portlet:namespace />submitFm',
		function() {
			var A = AUI();

			var loadingMask = new A.LoadingMask(
				{
					'strings.loading': '<%= UnicodeLanguageUtil.get(request, "account-is-being-created") %>',
					target: A.getBody()
				}
			);

			loadingMask.show();

			var url = '<%= editCommerceAccountActionURL.toString() %>';

			A.io.request(
				url,
				{
					form: {
						id: '<portlet:namespace />commerceAccountFm'
					},
					method: 'POST',
					on: {
						success: function() {
							loadingMask.hide();

							Liferay.Util.getOpener().<portlet:namespace />closePopup('commerceAccountDialog');
							Liferay.Util.getOpener().<portlet:namespace />refreshPortlet();
						}
					}
				}
			);
		},
		['aui-io-request', 'aui-loading-mask-deprecated']
	);
</aui:script>