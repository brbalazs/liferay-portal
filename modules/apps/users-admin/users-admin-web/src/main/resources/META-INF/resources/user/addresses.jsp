<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%
User selUser = (User)request.getAttribute(UsersAdminWebKeys.SELECTED_USER);

Contact selContact = null;

if (selUser != null) {
	selContact = selUser.getContact();
}

request.setAttribute("addresses.className", Contact.class.getName());

if (selContact != null) {
	request.setAttribute("addresses.classPK", selContact.getContactId());
}
else {
	request.setAttribute("addresses.classPK", 0L);
}
%>

<%@ include file="/common/addresses.jsp" %>