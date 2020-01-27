<%@ page import="com.liferay.commerce.google.merchant.sftp.web.portlet.action.GoogleMerchantSftpMVCActionCommand" %>

<%@ include file="/init.jsp" %>

<liferay-portlet:actionURL name="<%= GoogleMerchantSftpMVCActionCommand.MVC_COMMAND_NAME %>" var="actionURL" />

<aui:form method="post" action="<%= actionURL %>">
	<aui:button-row>
		<aui:button name="put-file" type="submit" />
	</aui:button-row>
</aui:form>