<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/message_boards/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

MBMessage message = (MBMessage)request.getAttribute(WebKeys.MESSAGE_BOARDS_MESSAGE);

long categoryId = MBUtil.getCategoryId(request, message);

MBCategory category = MBCategoryLocalServiceUtil.getCategory(categoryId);

category = category.toEscapedModel();

MBThread thread = message.getThread();

MBMessage curParentMessage = null;

String body = StringPool.BLANK;
boolean quote = false;
boolean splitThread = false;

boolean portletTitleBasedNavigation = GetterUtil.getBoolean(portletConfig.getInitParameter("portlet-title-based-navigation"));

String headerTitle = LanguageUtil.get(request, "move-thread");

if (portletTitleBasedNavigation) {
	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(redirect);

	renderResponse.setTitle(headerTitle);
}
%>

<div <%= portletTitleBasedNavigation ? "class=\"container-fluid-1280\"" : StringPool.BLANK %>>
	<portlet:actionURL name="/message_boards/move_thread" var="moveThreadURL">
		<portlet:param name="mvcRenderCommandName" value="/message_boards/move_thread" />
	</portlet:actionURL>

	<aui:form action="<%= moveThreadURL %>" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "moveThread();" %>'>
		<aui:input name="threadId" type="hidden" value="<%= thread.getThreadId() %>" />
		<aui:input name="mbCategoryId" type="hidden" value="<%= categoryId %>" />

		<c:if test="<%= !portletTitleBasedNavigation %>">
			<h3><%= headerTitle %></h3>
		</c:if>

		<liferay-ui:error exception="<%= MessageBodyException.class %>" message="please-enter-a-valid-message" />
		<liferay-ui:error exception="<%= MessageSubjectException.class %>" message="please-enter-a-valid-subject" />
		<liferay-ui:error exception="<%= NoSuchCategoryException.class %>" message="please-enter-a-valid-category" />

		<aui:fieldset-group markupView="lexicon">
			<aui:fieldset>
				<div class="form-group">
					<aui:input label="category[message-board]" name="categoryName" type="resource" value='<%= ((categoryId != MBCategoryConstants.DEFAULT_PARENT_CATEGORY_ID) && (categoryId != MBCategoryConstants.DISCUSSION_CATEGORY_ID)) ? category.getName() : LanguageUtil.get(request, "home") %>' />

					<aui:button name="selectCategoryButton" value="select" />
				</div>

				<aui:input disabled="<%= thread.isLocked() %>" helpMessage='<%= thread.isLocked() ? LanguageUtil.get(request, "unlock-thread-to-add-an-explanation-post") : StringPool.BLANK %>' label="add-explanation-post" name="addExplanationPost" onClick='<%= renderResponse.getNamespace() + "toggleExplanationPost();" %>' type="checkbox" />

				<div id="<portlet:namespace />explanationPost" style="display: none;">
					<aui:input maxlength="<%= ModelHintsConstants.TEXT_MAX_LENGTH %>" name="subject" style="width: 350px;" value="">
						<aui:validator name="required">
							function() {
								return AUI.$('#<portlet:namespace />addExplanationPost').prop('checked');
							}
						</aui:validator>
					</aui:input>

					<aui:field-wrapper label="body">
						<%@ include file="/message_boards/bbcode_editor.jspf" %>

						<aui:input name="body" type="hidden" />
					</aui:field-wrapper>
				</div>
			</aui:fieldset>
		</aui:fieldset-group>

		<aui:button-row>
			<aui:button type="submit" value="move" />

			<aui:button href="<%= redirect %>" type="cancel" />
		</aui:button-row>
	</aui:form>
</div>

<aui:script>
	function <portlet:namespace />moveThread() {
		document.<portlet:namespace />fm.<portlet:namespace />body.value = <portlet:namespace />getHTML();

		submitForm(document.<portlet:namespace />fm);
	}

	function <portlet:namespace />toggleExplanationPost() {
		if (
			document.getElementById('<portlet:namespace />addExplanationPost')
				.checked
		) {
			document.getElementById(
				'<portlet:namespace />explanationPost'
			).style.display = '';
		} else {
			document.getElementById(
				'<portlet:namespace />explanationPost'
			).style.display = 'none';
		}
	}
</aui:script>

<aui:script sandbox="<%= true %>">
	$('#<portlet:namespace />selectCategoryButton').on('click', function(event) {
		Liferay.Util.selectEntity(
			{
				dialog: {
					constrain: true,
					modal: true,
					width: 680
				},
				id: '<portlet:namespace />selectCategory',
				title: '<liferay-ui:message arguments="category" key="select-x" />',
				uri:
					'<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcRenderCommandName" value="/message_boards/select_category" /><portlet:param name="mbCategoryId" value="<%= String.valueOf(category.getParentCategoryId()) %>" /></portlet:renderURL>'
			},
			function(event) {
				var form = $(document.<portlet:namespace />fm);

				form.fm('mbCategoryId').val(event.categoryid);

				form.fm('categoryName').val(Liferay.Util.unescape(event.name));
			}
		);
	});
</aui:script>