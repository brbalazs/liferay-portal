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

<%@ page import="com.liferay.portal.kernel.util.PortalUtil" %>
<%@ taglib prefix="aui" uri="http://liferay.com/tld/aui" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>

<%@ include file="/init.jsp" %>

<%
    String segmentEditRootElementId = renderResponse.getNamespace() + "-org-chart-root";
%>

<div class="orgchart-module" id="<%= segmentEditRootElementId %>">
    <div class="inline-item my-5 p-5 w-100">
        <span aria-hidden="true" class="loading-animation"></span>
    </div>
</div>

<aui:script require="commerce-bom-admin-web@1.0.0/js/index.es as CarPartsFinder">

    CarPartsFinder.default(
        '<%= segmentEditRootElementId %>',
        {
            assetsPath: '<%= PortalUtil.getPathContext(request) + "/assets" %>',
            namespace: '<portlet:namespace/>',
            spritemap: '<%= themeDisplay.getPathThemeImages() + "/lexicon/icons.svg" %>'
        }
    );
</aui:script>